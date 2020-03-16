package networking;

import domain.ArtistDTO;
import domain.ShowSearchDTO;
import domain.User;
import networking.dto.*;
import show.services.IShowObserver;
import show.services.IShowServer;
import show.services.ShowException;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

public class ShowServerProxy implements IShowServer {

    private String host;
    private int port;
    private Socket connection;
    private BufferedWriter output;
    private BufferedReader input;
    private IShowObserver client;

    private BlockingQueue<Response> qresponses;
    private volatile boolean finished;

    public ShowServerProxy(String host, int port) {
        this.host = host;
        this.port = port;
        this.finished = false;
        this.qresponses = new LinkedBlockingDeque<>();
    }

    public boolean logIn(User user, IShowObserver client) {
        this.client = client;
        initializeConnection();
        UserDTO userDTO = DTOUtils.getDTO(user);
        Request request = new Request().build(Request.Type.LOGIN).setObject(userDTO);
        sendRequest(request);
        Optional<Response> response = readResponse();
        boolean answer = false;
        if (response.isPresent()) {
            if (response.get().getRsvp().equals("accepted")) {
                answer = true;
            } else {
                closeConnection();
            }
        }
        return answer;
    }

    public void buy(int showid, String name, int noTickets) throws ShowException {
        BuyDTO buyDTO = DTOUtils.getDTO(showid, name, noTickets);
        Request request = new Request().build(Request.Type.BUY).setObject(buyDTO);
        sendRequest(request);
    }

    @Override
    public List<ShowSearchDTO> search(LocalDate date) {
        DateDTO dateDTO = DTOUtils.getDTO(date);
        Request request = new Request().build(Request.Type.SEARCH).setObject(dateDTO);
        sendRequest(request);
        Optional<Response> response = readResponse();
        List<ShowSearchDTO> list = new ArrayList<>();
        if (response.isPresent()) {
            list = showFromResponseRsvp((String) response.get().getRsvp());
        }

        return list;
    }

    @Override
    public List<ArtistDTO> findAllShows() {
        Request request = new Request().build(Request.Type.FINDALL);
        sendRequest(request);
        Optional<Response> response = readResponse();
        List<ArtistDTO> list = new ArrayList<>();
        if (response.isPresent()) {
            list = artistFromResponseRsvp((String) response.get().getRsvp());
        }

        return list;
    }

    @Override
    public void logOut(IShowObserver client) {
        Request request = new Request().build(Request.Type.LOGOUT);
        sendRequest(request);
        closeConnection();
    }

    private void sendRequest(Request request) {
        try {
            output.write(request.toString());
            output.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Optional<Response> readResponse() {
        Response response;
        try {
            response = qresponses.take();
            return Optional.of(response);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    private void initializeConnection() {
        try {
            connection = new Socket(host, port);
            output = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
            input = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            finished = false;
            startReader();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void closeConnection() {
        finished = true;
        try {
//            input.close();
//            output.close();
            connection.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<ShowSearchDTO> showFromResponseRsvp(String rsvp) {
        List<ShowSearchDTO> list = new ArrayList<>();
        if(rsvp.length() != 0) {
            String[] elems = rsvp.split("%");
            for (String elem : elems) {
                String[] fields = elem.split("#");
                list.add(new ShowSearchDTO(Integer.parseInt(fields[0]), fields[1], fields[2], fields[3],
                        fields[4], Integer.parseInt(fields[5])));
            }
        }
        return list;
    }

    private List<ArtistDTO> artistFromResponseRsvp(String rsvp) {
        String[] elems = rsvp.split("%");
        List<ArtistDTO> list = new ArrayList<>();
        for (String elem : elems) {
            String[] fields = elem.split("#");
            list.add(new ArtistDTO(fields[0], fields[1], LocalDate.parse(fields[2]), fields[3],
                    Integer.parseInt(fields[4]), Integer.parseInt(fields[5])));
        }
        return list;
    }

    private void startReader() {
        Thread thread = new Thread(new ReaderThread());
        thread.start();
    }

    private boolean isUpdate(Response response) {
        return response.getType().equals(Request.Type.BUY);
    }

    private void handleUpdate(Response response) {
        client.updateData();
    }

    private class ReaderThread implements Runnable {

        @Override
        public void run() {
            while (!finished) {
                try {
                    String[] string = input.readLine().split("`");

                    Response response;
                    try{
                        response =new Response().build(string[0]).setResponse(string[1]);
                    } catch (Exception e){
                        response = new Response().build(string[0]).setResponse("");
                    }
                    if (isUpdate(response)) {
                        handleUpdate(response);
                    } else {
                        qresponses.put(response);
                    }
                } catch (SocketException se) {
                    return;
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

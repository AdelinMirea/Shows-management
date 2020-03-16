package networking.utils;

import domain.ArtistDTO;
import domain.ShowSearchDTO;
import domain.User;
import networking.dto.Response;
import show.services.IShowObserver;
import show.services.IShowServer;
import show.services.ShowException;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ShowClientRpcWorker implements Runnable, IShowObserver {
    private boolean connected;
    private BufferedReader input;
    private BufferedWriter output;

    private IShowServer server;
    private Socket connection;


    public ShowClientRpcWorker(IShowServer showServer, Socket connection) {
        this.server = showServer;
        this.connection = connection;
        try {
            output = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
            input = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            connected = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (connected) {
            try {
                String request = input.readLine();
                Response response = null;
                if(request!=null) {
                    response = handleRequest(request);
                }
                if (response != null) {
                    sendResponse(response);
                }
            }catch (SocketException e){
                break;
            } catch (IOException e) {
                e.printStackTrace();
            }
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
        }
        try {
            input.close();
            output.close();
            connection.close();
        } catch (IOException e) {
            System.out.println("Error " + e);
        }
    }

    private Response handleRequest(String request) {
        String[] attr = request.split("`");
        switch (attr[0]) {
            case "LOGIN":
                String username = attr[1];
                String password = attr[2];
                boolean ans = server.logIn(new User(username, password), this);
                if (ans) {
                    return new Response().build("LOGIN").setResponse("accepted");
                }
                return new Response().build("LOGIN").setResponse("denied");
            case "LOGOUT":
                try {
                    server.logOut(this);
//                    input.close();
//                    output.close();
                    connection.close();
                    connected = false;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            case "BUY":
                int id = Integer.parseInt(attr[1]);
                String name = attr[2];
                int noTickets = Integer.parseInt(attr[3]);

                try {
                    server.buy(id, name, noTickets);
                    return new Response().build("BUY").setResponse("OK");
                } catch (ShowException e) {
                    return new Response().build("BUY").setResponse("ABORT");
                }
                //GRIJA AICI search sau find
            case "SEARCH":
                LocalDate date = LocalDate.parse(attr[1]);
                List<ShowSearchDTO> listSearch = server.search(date);
                return new Response().build("SEARCH").setResponse(convertSearchToString(listSearch));
            case "FINDALL":
                List<ArtistDTO> list = server.findAllShows();
                return new Response().build("FINDALL").setResponse(convertToString(list));
            default:
                return null;
        }
    }

    private void sendResponse(Response response) {
        try {
            output.write(response.getType()+"`" + response.getRsvp() + "\n");
            output.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateData() {
        sendResponse(new Response().build("BUY").setResponse("OK"));
    }

    private String convertToString(List<ArtistDTO> list) {
        List<String> result = new ArrayList<>();
        for (ArtistDTO artist : list) {
            result.add(artist.getName() + "#" + artist.getShowname() + "#" + artist.getDate() + "#" +
                    artist.getLocation() + "#" + artist.getAvailableTickets() + "#" + artist.getSoldTickets());
        }
        return String.join("%", result);
    }

    private String convertSearchToString(List<ShowSearchDTO> list){
        List<String> result = new ArrayList<>();
        for (ShowSearchDTO show : list) {
            result.add(show.getShowId() + "#" + show.getArtist() + "#" + show.getShow() + "#" + show.getLocation()
                        + "#" + show.getHour() + "#" + show.getSeats());
        }
        return String.join("%", result);
    }
}

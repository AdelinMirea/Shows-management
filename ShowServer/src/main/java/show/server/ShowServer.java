package show.server;

import domain.*;
import service.Service;
import show.services.IShowObserver;
import show.services.IShowServer;
import show.services.ShowException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ShowServer implements IShowServer {
    private Service service;
    private List<IShowObserver> observers;

    public ShowServer(Service service){
        this.service = service;
        this.observers = new ArrayList<>();
    }


    @Override
    public boolean logIn(User user, IShowObserver client) {
        this.observers.add(client);
        return service.logIn(user.getUsername(), user.getPassword());
    }

    @Override
    public void buy(int showid, String name, int noTickets) throws ShowException {
        try {
            service.buy(showid, name, noTickets);
            notifyObservers();
        } catch (Exception e) {
            throw new ShowException();
        }
    }

    @Override
    public List<ShowSearchDTO> search(LocalDate date) {
        return service.search(date);
    }

    @Override
    public List<ArtistDTO> findAllShows() {
        return service.findAllShows();
    }

    @Override
    public void logOut(IShowObserver client) {
        observers.remove(client);
    }

    private void notifyObservers(){
        ExecutorService executorService = Executors.newCachedThreadPool();
        for(IShowObserver observer : observers){
            executorService.execute(observer::updateData);
        }
    }
}

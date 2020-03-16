package show.services;

import domain.ArtistDTO;
import domain.ShowSearchDTO;
import domain.User;

import java.time.LocalDate;
import java.util.List;

public interface IShowServer {
    boolean logIn(User user, IShowObserver client);
    void buy(int showid, String name, int noTickets) throws ShowException;
    List<ShowSearchDTO> search(LocalDate date);
    List<domain.ArtistDTO> findAllShows();
    void logOut(IShowObserver client);
}

package service;

import domain.*;
import repository.RepositoryShow;
import repository.RepositoryUser;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import repository.*;


public class Service {

    private RepositoryUser repoUser;
    private RepositoryShow repoShow;
    private RepositoryTicket repoTicket;
    private RepositoryArtist repoArtist;
    private RepositoryLineUp repoLineUp;

    public Service(RepositoryUser repoUser, RepositoryShow repoShow, RepositoryTicket repoTicket, RepositoryArtist repoArtist, RepositoryLineUp repoLineUp) {
        this.repoUser = repoUser;
        this.repoShow = repoShow;
        this.repoTicket = repoTicket;
        this.repoArtist = repoArtist;
        this.repoLineUp = repoLineUp;
    }

    public List<ArtistDTO> findAllShows(){
        return repoLineUp.findAll().stream().map(this::createArtistORM).collect(Collectors.toList());
    }

    private ArtistDTO createArtistORM(LineUp lineUp){
        String name = repoArtist.find(lineUp.getArtistId()).getName();
        Show show = repoShow.find(lineUp.getShowId());
        String showname = show.getName();
        String location = show.getLocation();
        LocalDate date = show.getDate();
        int available = show.getAvailableTickets() - show.getSoldTickets();
        int sold = show.getSoldTickets();
        return new ArtistDTO(name, showname, date, location, available, sold);
    }

    public void onClose(){
    }

    public boolean logIn(String username, String pass){
        return repoUser.allowUser(username, pass);
    }

    public void buy(int showId, String name, int noPlaces) throws Exception {
        Ticket ticket = new Ticket(name, noPlaces, showId);
        Show show = repoShow.find(showId);
        if(show.getSoldTickets() + noPlaces <= show.getAvailableTickets()) {
            show.sellTickets(noPlaces);
            repoShow.update(show);
            repoTicket.save(ticket);
        }else{
            throw new Exception("Not enough available tickets   ");
        }
    }

    public List<ShowSearchDTO> search(LocalDate date){
//        List<Show> shows = repoShow.findAll().stream().filter((c) -> c.getDate().equals(date)).collect(Collectors.toList());
        List<Show> shows = repoShow.findByDate(date);
        List<ShowSearchDTO> lineUps = new ArrayList<>();
        for(Show show : shows){
            List<LineUp> lines = repoLineUp.findByShow(show.getId());
            for(LineUp lineUp : lines){
                lineUps.add(new ShowSearchDTO(
                        show.getId(),
                        repoArtist.find(lineUp.getArtistId()).getName(),
                        show.getName(),
                        show.getLocation(),
                        lineUp.getHour(),
                        show.getAvailableTickets() - show.getSoldTickets()));
            }
        }
        return lineUps;
    }
}


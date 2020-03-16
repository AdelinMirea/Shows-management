package domain;

import java.time.LocalDate;

public class ArtistDTO {

    private String name;
    private String showname;
    private LocalDate date;
    private String location;
    private int availableTickets;
    private int soldTickets;

    public ArtistDTO(String name, String showName, LocalDate date, String location, int availableTickets, int soldTickets) {
        this.name = name;
        this.showname = showName;
        this.date = date;
        this.location = location;
        this.availableTickets = availableTickets;
        this.soldTickets = soldTickets;
    }

    public String getShowname() {
        return showname;
    }

    public void setShowname(String showname) {
        this.showname = showname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getAvailableTickets() {
        return availableTickets;
    }

    public void setAvailableTickets(int availableTickets) {
        this.availableTickets = availableTickets;
    }

    public int getSoldTickets() {
        return soldTickets;
    }

    public void setSoldTickets(int soldTickets) {
        this.soldTickets = soldTickets;
    }
}

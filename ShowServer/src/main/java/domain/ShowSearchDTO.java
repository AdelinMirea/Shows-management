package domain;

public class ShowSearchDTO {

    private int showId;
    private String artist;
    private String show;
    private String location;
    private String hour;
    private int seats;

    public ShowSearchDTO(int showId, String artist, String show, String location, String hour, int seats) {
        this.showId = showId;
        this.artist = artist;
        this.show = show;
        this.location = location;
        this.hour = hour;
        this.seats = seats;
    }

    public int getShowId(){
        return this.showId;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getShow() {
        return show;
    }

    public void setShow(String show) {
        this.show = show;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public int getSeats() {
        return seats;
    }

    public void setSeats(int seats) {
        this.seats = seats;
    }
}

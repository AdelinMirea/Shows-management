package domain;

public class LineUp implements HasID<Integer> {

    private Integer id;
    private Integer artistId;
    private Integer showId;
    private String hour;

    public LineUp(Integer id, Integer artistId, Integer showId, String hour) {
        this.id = id;
        this.artistId = artistId;
        this.showId = showId;
        this.hour = hour;
    }

    public Integer getArtistId() {
        return artistId;
    }

    public void setArtistId(Integer artistId) {
        this.artistId = artistId;
    }

    public Integer getShowId() {
        return showId;
    }

    public void setShowId(Integer showId) {
        this.showId = showId;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer integer) {
        this.id = integer;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    @Override
    public String toString() {
        return "LineUp{" +
                "id=" + id +
                ", artistId=" + artistId +
                ", showId=" + showId +
                '}';
    }
}

package networking.dto;

public class BuyDTO implements Requestable{
    private int showID;
    private String name;
    private int noTickets;

    public BuyDTO(int showID, String name, int noTickets) {
        this.showID = showID;
        this.name = name;
        this.noTickets = noTickets;
    }

    public int getShowID() {
        return showID;
    }

    public void setShowID(int showID) {
        this.showID = showID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNoTickets() {
        return noTickets;
    }

    public void setNoTickets(int noTickets) {
        this.noTickets = noTickets;
    }

    @Override
    public String toRequest(){
        return showID + "`" + name + "`" + noTickets;
    }
}

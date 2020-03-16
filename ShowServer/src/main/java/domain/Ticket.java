package domain;

public class Ticket implements HasID<Integer> {

    private Integer id;
    private String customerName;
    private Integer noPlaces;
    private Integer idShow;

    public Ticket(String customerName, Integer noPlaces, Integer idShow) {
        this.id = -1;
        this.customerName = customerName;
        this.noPlaces = noPlaces;
        this.idShow = idShow;
    }

    public Ticket(Integer id, String customerName, Integer noPlaces, Integer idShow) {
        this.id = id;
        this.customerName = customerName;
        this.noPlaces = noPlaces;
        this.idShow = idShow;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Integer getNoPlaces() {
        return noPlaces;
    }

    public void setNoPlaces(Integer noPlaces) {
        this.noPlaces = noPlaces;
    }

    public Integer getIdShow() {
        return idShow;
    }

    public void setIdShow(Integer idShow) {
        this.idShow = idShow;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer integer) {
        this.id = integer;
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "id=" + id +
                ", customerName='" + customerName + '\'' +
                ", noPlaces=" + noPlaces +
                ", idShow=" + idShow +
                '}';
    }
}

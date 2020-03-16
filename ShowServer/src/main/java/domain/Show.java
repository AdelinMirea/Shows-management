package domain;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "shows")
public class Show implements HasID<Integer>, Cloneable{

    @Id
    @GeneratedValue
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "location")
    private String location;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "availableTickets")
    private int availableTickets; //total number of tickets for a show

    @Column(name = "soldTickets")
    private int soldTickets;      //no. of sold tickets for a show. Must be <= available tickets

    public Show(){
        id=0;
        location="";
        date=LocalDate.parse("2010-10-10");
        availableTickets=0;
        soldTickets=0;
    }

    public Show(int id, String name, String location, String date, int availableTickets, int soldTickets) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.date = LocalDate.parse(date);
        this.availableTickets = availableTickets;
        this.soldTickets = soldTickets;
    }

    public Integer getId() {
        return id;
    }

    public String getName(){
        return name;
    }

    public String getLocation() {
        return location;
    }

    public LocalDate getDate() {
        return date;
    }

    public int getAvailableTickets() {
        return availableTickets;
    }

    public int getSoldTickets() {
        return soldTickets;
    }

    synchronized public void setId(Integer id) {
        this.id = id;
    }

    synchronized public void setName(String name) {
        this.name = name;
    }

    synchronized public void setLocation(String location) {
        this.location = location;
    }

    synchronized public void setDate(String date) {
        this.date = LocalDate.parse(date);
    }

    synchronized public void setAvailableTickets(int availableTickets) {
        this.availableTickets = availableTickets;
    }

    synchronized public void setSoldTickets(int soldTickets) {
        if(availableTickets-soldTickets >= 0){
            this.soldTickets = soldTickets;
        }
    }

    synchronized public void sellTickets(int tickets){
        if(this.availableTickets - (this.soldTickets + tickets) >= 0){
            this.soldTickets = this.soldTickets + tickets;
        }
    }

    @Override
    public String toString() {
        return "Show{" +
                "id=" + id +
                ", location='" + location + '\'' +
                ", date=" + date +
                ", availableTickets=" + availableTickets +
                ", soldTickets=" + soldTickets +
                '}';
    }
}

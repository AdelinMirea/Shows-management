package repository;

import domain.Ticket;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import rest.utils.ConnectionFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
public class RepoTicketDB implements RepositoryTicket {

    private ConnectionFactory connectionFactory;
    private Connection connection;
    private Logger logger = LogManager.getLogger(RepoTicketDB.class.getName());

    @Autowired
    public RepoTicketDB(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
        this.connection = connectionFactory.getConnection();
    }

    @Override
    public Ticket save(Ticket ticket) {
        logger.traceEntry(ticket.toString());
        try (PreparedStatement statement = connection.prepareStatement("INSERT into tickets(customerName, numberTickets, idShow) values (?, ?, ?)", Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, ticket.getCustomerName());
            statement.setInt(2, ticket.getNoPlaces());
            statement.setInt(3, ticket.getIdShow());
            int idTicket = statement.executeUpdate();
            ticket.setId(idTicket);
            return ticket;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        throw new IllegalStateException("Cannot save");
    }

    @Override
    public void delete(Integer id) {
        try(PreparedStatement statement = connection.prepareStatement("DELETE FROM tickets WHERE id=?")){
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Collection<Ticket> findAll() {
        List<Ticket> tickets = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement("SELECT * from tickets")) {
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    tickets.add(new Ticket(resultSet.getInt("id"),
                            resultSet.getString("customerName"),
                            resultSet.getInt("numberTickets"),
                            resultSet.getInt("idShow")));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tickets;
    }
}

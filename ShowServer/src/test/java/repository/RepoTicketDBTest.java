package repository;

import domain.Ticket;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import rest.utils.ConnectionFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

class RepoTicketDBTest {

    private static ConnectionFactory factory;
    private static RepositoryTicket repo;

    @AfterAll
    public static void afterAll(){
        factory.closeConnections();
    }
    @BeforeAll
    public static void beforeAll(){
        factory = new ConnectionFactory();
        repo = new RepoTicketDB(factory);
    }
    @Test
    void save() {
        Ticket ticket = repo.save(new Ticket("Customer", 1, 1));
        Assertions.assertTrue(ticket.getId()!=-1);
        repo.delete(ticket.getId());
    }

    @Test
    void findAll() {
        Assertions.assertTrue(repo.findAll().size() > 0);
    }
}
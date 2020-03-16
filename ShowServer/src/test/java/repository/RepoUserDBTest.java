package repository;

import domain.User;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import rest.utils.ConnectionFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.Properties;

class RepoUserDBTest {

    private static ConnectionFactory factory;
    private static RepositoryUser repo;

    @AfterAll
    public static void afterAll(){
        factory.closeConnections();
    }

    @BeforeAll
    public static void beforeAll(){
        factory = new ConnectionFactory();
        repo = new RepoUserDB(factory);
    }

    @Test
    void findAll() {
        Collection<User> users = repo.findAll();
        Assertions.assertTrue(users.stream().anyMatch((user)->user.getUsername().equals("admin")));
    }
}
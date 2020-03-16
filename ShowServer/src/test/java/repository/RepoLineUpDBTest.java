package repository;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import rest.utils.ConnectionFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

class RepoLineUpDBTest {

    private static ConnectionFactory factory;
    private static RepositoryLineUp repo;

    @AfterAll
    public static void afterAll(){
        factory.closeConnections();
    }

    @BeforeAll
    public static void beforeAll(){
        factory = new ConnectionFactory();
        repo = new RepoLineUpDB(factory);
    }

    @Test
    void findAll() {
        Assertions.assertTrue(repo.findAll().size() != 0);
    }

    @Test
    void findLineUp() {
        Assertions.assertNotNull(repo.findLineUp(1, 1));
    }
}
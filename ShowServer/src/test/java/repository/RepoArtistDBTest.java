package repository;

import domain.Artist;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import rest.utils.ConnectionFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.Properties;

class RepoArtistDBTest {

    private static ConnectionFactory factory;
    private static RepositoryArtist repo;


    @BeforeAll
    public static void beforeAll(){
        factory = new ConnectionFactory();
        repo = new RepoArtistDB(factory);
    }

    @AfterAll
    public static void afterall(){
        factory.closeConnections();
    }

    @Test
    void findAll() {
        Collection<Artist> artists = repo.findAll();
        Assertions.assertEquals(artists.stream().filter(artist -> artist.getId()<=3).count(), 3);
    }

    @Test()
    void find() {
        Assertions.assertThrows(IllegalArgumentException.class, ()->{repo.find(-1);});
    }
}
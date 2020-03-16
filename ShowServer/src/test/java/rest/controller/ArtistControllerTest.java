package rest.controller;

import domain.Artist;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import rest.persistance.RepoArtist;
import rest.utils.ConnectionFactory;

import java.security.PrivateKey;
import java.util.Collection;
import java.util.concurrent.Callable;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ArtistControllerTest {

    public static final String URL = "http://localhost:8080/show";
    private RestTemplate restTemplate = new RestTemplate();

    @Autowired
    private RepoArtist repo;

    private <T> T execute(Callable<T> callable) {
        try {
            return callable.call();
        } catch (ResourceAccessException | HttpClientErrorException e) {
            throw new IllegalArgumentException(e);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Test
    void findAll() {
        Collection<Artist> artists = execute(()->restTemplate.getForObject(String.format("%s/%s", URL, "artists"), Collection.class));
        assertTrue(artists.size()!=0);
    }

    @Test
    void getById() {
        Artist entity = execute(()->restTemplate.getForObject(String.format("%s/%s", URL, "artist/1"), Artist.class));
        assertEquals(1, (int) entity.getId());
    }

    @Test
    void delete() {
        Artist artist = new Artist(1000, "ana are mere");
        repo.save(artist);
        boolean result = execute(()->{restTemplate.delete(String.format("%s/%s", URL, "artist/1000")); return true;});
        assertTrue(result);
    }

    @Test
    void add() {
        Artist artist = new Artist(1000, "ana are mere");
        Artist result = execute(()->restTemplate.postForObject(String.format("%s/%s", URL, "artist"), artist, Artist.class));
        assertEquals(1000, (int) result.getId());
        repo.delete(1000);
    }

    @Test
    void update() {
        Artist artist = new Artist(1000, "ana are mere");
        repo.save(artist);
        boolean result = execute(()->{restTemplate.put(String.format("%s/%s", URL, "artist/1000"), artist);return true;});
        assertTrue(result);
        repo.delete(artist.getId());
    }
}
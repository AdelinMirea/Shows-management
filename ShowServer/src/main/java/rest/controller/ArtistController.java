package rest.controller;

import domain.Artist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import repository.RepoArtistDB;
import rest.persistance.RepoArtist;

import javax.servlet.http.HttpServletResponse;
import java.util.Collection;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/show")
public class ArtistController {

    @Autowired
    private RepoArtist repo;

    @RequestMapping(value = "/artists", method= RequestMethod.GET)
    public Collection<Artist> findAll() {
        return repo.findAll();
    }

    @RequestMapping(value = "/artist/{id}", method=RequestMethod.GET)
    public ResponseEntity<?> getById(@PathVariable String id) {
        try {
            Artist artist = repo.find(Integer.parseInt(id));
            return new ResponseEntity<Artist>(artist, HttpStatus.OK);
        }catch (Exception e){
            return new  ResponseEntity<String>("No entity found!", HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/artist/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<String> delete(@PathVariable String id, HttpServletResponse response){
        Artist artist = repo.find(Integer.parseInt(id));
        try {
            repo.delete(Integer.parseInt(id));
        }catch (Exception e){
            return new ResponseEntity<String>("Artist not found!", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>("OK!", HttpStatus.OK);
    }

    @RequestMapping(value = "/artist",method = RequestMethod.POST)
    public ResponseEntity<?> add(@RequestBody Artist artist){
        try {
            repo.save(artist);
        }catch (Exception e){
            return new ResponseEntity<String>("Artist not saved!", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<Artist>(artist, HttpStatus.OK);
    }

    @RequestMapping(value = "/artist/{id}", method = RequestMethod.PUT)
    public ResponseEntity<?> update(@RequestBody Artist artist){
        try {
            repo.update(artist.getId(), artist);
        }catch (Exception e){
            return new ResponseEntity<String>("Artist not updated!", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<String>("OK!", HttpStatus.OK);
    }
}

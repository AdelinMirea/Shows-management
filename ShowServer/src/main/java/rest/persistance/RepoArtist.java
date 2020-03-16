package rest.persistance;
import domain.Artist;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import rest.utils.ConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
public class RepoArtist implements repository.RepositoryArtist {

    private ConnectionFactory connectionFactory;
    private Connection connection;
    private Logger logger = LogManager.getLogger(RepoArtist.class.getName());

    @Autowired
    public RepoArtist(ConnectionFactory connectionFactory){
        logger.info("Created");
        this.connectionFactory = connectionFactory;
        connection = connectionFactory.getConnection();
    }

    @Override
    public Collection<Artist> findAll() {
        logger.traceEntry();
        List<Artist> artists = new ArrayList<>();
        try(PreparedStatement statement = connection.prepareStatement("SELECT * from artists")){
            try(ResultSet resultSet = statement.executeQuery()){
                while(resultSet.next()){
                    artists.add(new Artist(resultSet.getInt("id"), resultSet.getString("name")));
                }
            }
        } catch (SQLException e) {
            logger.catching(e);
            e.printStackTrace();
        }
        return logger.traceExit(artists);
    }

    @Override
    public Artist find(Integer integer) {
        logger.traceEntry(integer.toString());
        Artist artist = null;
        try(PreparedStatement statement = connection.prepareStatement("SELECT * from artists where id=?")){
            statement.setInt(1, integer);
            try(ResultSet resultSet = statement.executeQuery()){
                while(resultSet.next()){
                    artist = new Artist(resultSet.getInt("id"), resultSet.getString("name"));
                }
            }
        } catch (SQLException e) {
            logger.catching(e);
            e.printStackTrace();
        }
        if(artist == null){
            throw logger.throwing(new IllegalArgumentException("No item found"));
        }
        return logger.traceExit(artist);
    }

    public void delete(Integer id){
        logger.traceEntry(id.toString());

        try(PreparedStatement statement = connection.prepareStatement("DELETE FROM Artists WHERE id=?")){
            statement.setInt(1, id);
            if(statement.executeUpdate() < 1){
                logger.throwing(new IllegalArgumentException("No entity found"));
                throw new IllegalArgumentException("No entity found");
            }
        } catch (SQLException e) {
            logger.catching(e);
            e.printStackTrace();
        }
        logger.traceExit();
    }

    public void save(Artist artist){
        logger.traceEntry(artist.toString());

        try(PreparedStatement statement = connection.prepareStatement("INSERT INTO artists (id, name) VALUES (?, ?)")){
            statement.setInt(1, artist.getId());
            statement.setString(2, artist.getName());
            if(statement.executeUpdate()<1){
                logger.throwing(new IllegalArgumentException("Not saved"));
                throw new IllegalArgumentException("Not saved");
            }
        } catch (SQLException e) {
            logger.catching(e);
            e.printStackTrace();
        }
        logger.traceExit();
    }

    public void update(Integer id, Artist artist){
        logger.traceEntry(artist.toString());

        try(PreparedStatement statement = connection.prepareStatement("UPDATE artists SET name=? WHERE id=?")){
            statement.setString(1, artist.getName());
            statement.setInt(2, artist.getId());
            if(statement.executeUpdate()<1){
                logger.throwing(new IllegalArgumentException("Not updated"));
                throw new IllegalArgumentException("Not updated");
            }
        } catch (SQLException e) {
            logger.catching(e);
            e.printStackTrace();
        }
        logger.traceExit();
    }
}

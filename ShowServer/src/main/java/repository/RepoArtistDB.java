package repository;

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
public class RepoArtistDB implements repository.RepositoryArtist {

    private ConnectionFactory connectionFactory;
    private Connection connection;
    private Logger logger = LogManager.getLogger(RepoArtistDB.class.getName());

    @Autowired
    public RepoArtistDB(ConnectionFactory connectionFactory){
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
}

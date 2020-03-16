package repository;

import domain.LineUp;
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
public class RepoLineUpDB implements RepositoryLineUp {

    private ConnectionFactory connectionFactory;
    private Connection connection;
    private Logger logger = LogManager.getLogger(RepoUserDB.class.getName());

    @Autowired
    public RepoLineUpDB(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
        this.connection = connectionFactory.getConnection();
    }

    @Override
    public Collection<LineUp> findAll() {
        logger.traceEntry();
        List<LineUp> lineUps = new ArrayList<>();
        try(PreparedStatement statement = connection.prepareStatement("SELECT * from lineup")){
            try(ResultSet resultSet = statement.executeQuery()){
                while(resultSet.next()){
                    lineUps.add(new LineUp(resultSet.getInt("id"),
                                            resultSet.getInt("idArtist"),
                                            resultSet.getInt("idShow"),
                                            resultSet.getString("hour")));
                }
            }
        } catch (SQLException e) {
            logger.catching(e);
            e.printStackTrace();
        }
        return logger.traceExit(lineUps);
    }

    @Override
    public LineUp findLineUp(int artistId, int showId) {
        LineUp lineUp = null;
        try(PreparedStatement statement = connection.prepareStatement("SELECT * from lineup where idArtist=? and idShow=?")){
            statement.setInt(1, artistId);
            statement.setInt(2, showId);
            try(ResultSet resultSet = statement.executeQuery()){
                while(resultSet.next()){
                    lineUp = new LineUp(resultSet.getInt("id"),
                            resultSet.getInt("idArtist"),
                            resultSet.getInt("idShow"),
                            resultSet.getString("hour"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if(lineUp == null){
            throw new IllegalArgumentException("No item found");
        }
        return lineUp;
    }

    @Override
    public List<LineUp> findByShow(int idShow) {
        List<LineUp> lineUps = new ArrayList<>();
        try(PreparedStatement statement = connection.prepareStatement("select * from lineup where idShow=?")){
            statement.setInt(1, idShow);
            try(ResultSet resultSet = statement.executeQuery()){
                while (resultSet.next()){
                    lineUps.add(new LineUp(resultSet.getInt("id"),
                                            resultSet.getInt("idArtist"),
                                            resultSet.getInt("idShow"),
                                            resultSet.getString("hour")));
                }
            }
        } catch (SQLException e) {
            logger.catching(e);
            e.printStackTrace();
        }
        return logger.traceExit(lineUps);
    }
}

package repository;

import domain.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Repository;
import rest.utils.ConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Repository
@ComponentScan("rest.utils")
public class RepoUserDB implements RepositoryUser {

    private ConnectionFactory connectionFactory;
    private Connection connection;
    private Logger logger = LogManager.getLogger(RepoUserDB.class.getName());

    @Autowired
    public RepoUserDB(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
        connection = connectionFactory.getConnection();
    }

    @Override
    public Collection<User> findAll() {
        logger.traceEntry();
        List<User> users = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement("SELECT * from users")) {
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    users.add(new User(resultSet.getInt("id"),
                            resultSet.getString("username"),
                                resultSet.getString("pass")));
                }
            }
        } catch (SQLException e) {
            logger.catching(e);
            e.printStackTrace();
        }
        return logger.traceExit(users);
    }

    @Override
    public boolean allowUser(String id, String pass) {
        boolean allow = false;
        try(PreparedStatement ps = connection.prepareStatement("select * from users where username=? and pass=?")){
            ps.setString(1, id);
            ps.setString(2, pass);
            try(ResultSet rs = ps.executeQuery()){
                if(rs.next()){
                    allow = true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return allow;
    }
}

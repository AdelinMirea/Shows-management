package rest.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
@Component
public class ConnectionFactory {

    private List<Connection> connections = new ArrayList<>();
    private Logger logger = LogManager.getLogger(Connection.class.getName());
    private Properties properties;

//    @Value("${jdbc.user}")
    private String user = "root";
//    @Value("${jdbc.pass}")
    private String pass = "!@#$";
//    @Value("${jdbc.url}")
    private String url = "jdbc:mysql://localhost/Entertainment?useSSL=false";
//    @Value("${jdbc.driver}")
    private String driver = "com.mysql.jdbc.Driver";
//    public ConnectionFactory(Properties properties) {
//        this.properties = properties;
//        createConnections();
//    }
    public ConnectionFactory(){
        this.properties = new Properties();

//        properties.setProperty("jdbc.driver", "com.mysql.jdbc.Driver");
//        properties.setProperty("jdbc.url", "jdbc:mysql://localhost/Entertainment?useSSL=false");
//        properties.setProperty("jdbc.user", "root");
//        properties.setProperty("jdbc.pass", "!@#$");

//            properties.load(new FileInputStream("../../database.properties"));
    }

    private void createConnections() {
        logger.traceEntry();
        connections.clear();

        try {
//            String driver = properties.getProperty("jdbc.driver");
//            Class.forName(driver);
            if(user == null){
                for (int i = 0; i < 5; i++) {
                    connections.add(DriverManager.getConnection(url));
                }
            }else{
                for (int i = 0; i < 5; i++) {
                    connections.add(DriverManager.getConnection(url, user, pass));
                }
            }
        } catch (SQLException e) {
            logger.catching(e);
            e.printStackTrace();
        }

        logger.traceExit();
    }


    synchronized public Connection getConnection() {
        logger.traceEntry();
        for (Connection connection : connections) {
            try {
                if (!connection.isClosed()) {
                    return connection;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        //if there are no more connections available, re-create them
        createConnections();
        return logger.traceExit(connections.get(0));
    }

    public void closeConnections(){
        try {
            finalize();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    @Override
    protected void finalize() throws Throwable {
        logger.traceEntry();
        for (Connection connection : connections) {
            connection.close();
        }

        super.finalize();
        logger.traceExit();
    }
}

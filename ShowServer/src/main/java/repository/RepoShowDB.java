package repository;

import domain.Show;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.*;

@Component
public class RepoShowDB implements RepositoryShow {

    private static final Logger logger = LogManager.getLogger(RepoShowDB.class.getName());

    private final StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();

    private SessionFactory factory;// = new MetadataSources( registry ).buildMetadata().buildSessionFactory();

    public RepoShowDB(){
        try {
            factory = new MetadataSources( registry ).buildMetadata().buildSessionFactory();
        }
        catch (Exception e) {
            e.printStackTrace();
//            System.out.println("Exceptie "+e);
            StandardServiceRegistryBuilder.destroy( registry );
        }
    }

    @Override
    public void update(Show entity) {
        logger.traceEntry(entity.toString());
        try (Session session = factory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                Show show = session.load(Show.class, entity.getId());
                show.setSoldTickets(entity.getSoldTickets());
                tx.commit();
            } catch (RuntimeException ex) {
                if (tx != null)
                    tx.rollback();
            }
        }
    }

    @Override
    public Show find(int id) {
        logger.traceEntry(String.valueOf(id));
        try(Session session = factory.openSession()){
            session.beginTransaction();
            Show show = (Show) session.createQuery("from Show where id=:id").setParameter("id", id).uniqueResult();
            session.getTransaction().commit();
            return show;
        }
    }

    @Override
    public List<Show> findByDate(LocalDate date) {
        logger.traceEntry(date.toString());
        try(Session session = factory.openSession()){
            session.beginTransaction();
            List result = session.createQuery("from Show where date=:date").setParameter("date", date).list();
            session.getTransaction().commit();
            return (List<Show>) result;
        }
    }

    @Override
    public Collection<Show> findAll() {
        logger.traceEntry();
        try(Session session = factory.openSession()){
            session.beginTransaction();
            List result = session.createQuery("from Show").list();
            session.getTransaction().commit();
            return (List<Show>) result;
        }
    }


//    private ConnectionFactory connectionFactory;
//    private Connection connection;
//
//    @Autowired
//    public RepoShowDB(ConnectionFactory connectionFactory){
//        this.connectionFactory = connectionFactory;
//        connection = connectionFactory.getConnection();
//    }
//
//    public void save(Show entity) {
//        logger.traceEntry(entity.toString());
//        try (PreparedStatement statement = connection.prepareStatement("INSERT INTO Shows(id, name, date, location, availableTickets, soldTickets) values (?, ?, ?, ?, ?, ?)")){
//            statement.setInt(1, entity.getId());
//            statement.setString(2, entity.getName());
//            statement.setString(3, entity.getDate().toString());
//            statement.setString(4, entity.getLocation());
//            statement.setInt(5, entity.getAvailableTickets());
//            statement.setInt(6, entity.getSoldTickets());
//            statement.execute();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        logger.traceExit();
//    }
//
//    @Override
//    public Collection<Show> findAll() {
//        logger.traceEntry();
//        ArrayList<Show> list = new ArrayList<>();
//        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Shows")){
//            ResultSet resultSet = preparedStatement.executeQuery();
//            while(resultSet.next()){
//                list.add(new Show(resultSet.getInt("id"),resultSet.getString("name"), resultSet.getString("location"), resultSet.getString("date"), resultSet.getInt("availableTickets"), resultSet.getInt("soldTickets")));
//            }
//            resultSet.close();
//        } catch (SQLException e) {
//            logger.catching(e);
//            e.printStackTrace();
//        }
//        return logger.traceExit(list);
//    }
//
//    public Show find(Integer integer) {
//        logger.traceEntry(integer.toString());
//        try(PreparedStatement statement = connection.prepareStatement("SELECT * FROM Shows WHERE id=?")){
//            statement.setInt(1, integer);
//            ResultSet resultSet = statement.executeQuery();
//            if(resultSet.next()){
//                Show show = new Show(resultSet.getInt("id"),resultSet.getString("name"), resultSet.getString("location"), resultSet.getString("date"), resultSet.getInt("availableTickets"), resultSet.getInt("soldTickets"));
//                return logger.traceExit(show);
//            }
//            resultSet.close();
//        }catch (SQLException e){
//            logger.catching(e);
//            e.printStackTrace();
//        }
//        throw logger.throwing(new IllegalArgumentException("Item not found"));
//    }
//
//    @Override
//    public void update(Show entity) {
//        logger.traceEntry(entity.toString());
//        try(PreparedStatement statement = connection.prepareStatement("UPDATE Shows SET date=?, location=?, availableTickets=?, soldTickets=?, name=? where id=?")){
//            statement.setString(1, entity.getDate().toString());
//            statement.setString(2, entity.getLocation());
//            statement.setInt(3, entity.getAvailableTickets());
//            statement.setInt(4, entity.getSoldTickets());
//            statement.setString(5, entity.getName());
//            statement.setInt(6, entity.getId());
//            int rows = statement.executeUpdate();
//            logger.log(Level.INFO, "Update success: "+ entity + ". " + rows + " affected");
//        } catch (SQLException e) {
//            logger.catching(e);
//            e.printStackTrace();
//        }
//        logger.traceExit();
//    }
//
//    @Override
//    public Show find(int id) {
//        logger.traceEntry(String.valueOf(id));
//        try (PreparedStatement statement = connection.prepareStatement("SELECT * from shows where id=?")){
//            statement.setInt(1, id);
//            try(ResultSet resultSet = statement.executeQuery()){
//                if(resultSet.next()){
//                    return new Show(resultSet.getInt("id"),
//                            resultSet.getString("name"),
//                            resultSet.getString("location"),
//                            resultSet.getString("date"),
//                            resultSet.getInt("availableTickets"),
//                            resultSet.getInt("soldTickets"));
//                }
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        throw new IllegalStateException("Nu exista valoarea");
//    }
//
//    @Override
//    public List<Show> findByDate(LocalDate date) {
//        logger.traceEntry(date.toString());
//        List<Show> shows = new ArrayList<>();
//        try(PreparedStatement statement = connection.prepareStatement("SELECT * from shows where date=?")){
//            statement.setString(1, date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
//            try(ResultSet resultSet = statement.executeQuery()){
//                while (resultSet.next()){
//                    shows.add(new Show(resultSet.getInt("id"),
//                            resultSet.getString("name"),
//                            resultSet.getString("location"),
//                            resultSet.getString("date"),
//                            resultSet.getInt("availableTickets"),
//                            resultSet.getInt("soldTickets")));
//                }
//            }
//        } catch (SQLException e) {
//            logger.catching(e);
//            e.printStackTrace();
//        }
//        return logger.traceExit(shows);
//    }
//
//    public void delete(Integer integer) {
//        logger.traceEntry(integer.toString());
//        try(PreparedStatement statement = connection.prepareStatement("DELETE FROM Shows where id=?")){
//            statement.setInt(1, integer);
//            int rows = statement.executeUpdate();
//            logger.log(Level.INFO, "Delete success: "+ integer + ". " + rows + " affected");
//        } catch (SQLException e) {
//            logger.catching(e);
//            e.printStackTrace();
//        }
//        logger.traceExit();
//    }
//
//    private void load(){
//        logger.traceEntry();
//
//        logger.traceExit();
//    }
//
//    public void closeConnection(){
//        logger.traceEntry();
//        try {
//            connection.close();
//            logger.info("Connection closed.");
//        } catch (SQLException e) {
//            logger.catching(e);
//            e.printStackTrace();
//        }
//        logger.traceExit();
//    }
}

//package repository;
//
//import domain.Show;
//import org.junit.jupiter.api.*;
//import rest.utils.ConnectionFactory;
//
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.Properties;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//public class RepoShowTest {
//
//    private static ConnectionFactory factory;
//    private static RepoShowDB repository;
//    private int id=100;
//    private Show s = new Show(id++,"Name" , "Location", "2010-10-10", 10, 10);
//
//    @BeforeEach
//    public void before(){
//        repository.save(s);
//    }
//
//    @AfterEach
//    public void after(){
//        ArrayList<Integer> ids = new ArrayList<>();
//        repository.findAll().forEach(el -> {
//            if(el.getId()>=100){
//                ids.add(el.getId());
//            }
//        });
//        ids.forEach(id -> repository.delete(id));
//    }
//
//    @BeforeAll
//    public static void beforeAll(){
//        Properties properties = new Properties();
//        try {
//            properties.load(new FileInputStream("src/main/resources/database.properties"));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        factory = new ConnectionFactory(properties);
//        repository = new RepoShowDB(factory);
//    }
//
//    @AfterAll
//    public static void afterAll(){
//        factory.closeConnections();
//    }
//
//    @Test
//    public void save() {
//        Show s1 = new Show();
//        s1.setAvailableTickets(s.getAvailableTickets());
//        s1.setId(++id);
//        repository.save(s1);
//        Assertions.assertEquals(repository.find(id).getAvailableTickets(), s.getAvailableTickets());
//    }
//
//    @Test
//    public void findAll() {
//        ArrayList<Integer> elem = new ArrayList<>();
//        repository.findAll().forEach(el->{
//            if(el.getId() >=100){
//                elem.add(el.getId());
//            }
//        });
//        Assertions.assertEquals(elem.size(), 1);
//    }
//
//    @Test
//    public void find() {
//        Assertions.assertEquals(repository.find(s.getId()).getLocation(), s.getLocation());
//    }
//
//    @Test
//    public void update() {
//        s.setAvailableTickets(101);
//        repository.update(s);
//        Assertions.assertEquals(repository.find(s.getId()).getAvailableTickets(), 101);
//    }
//
//    @Test
//    public void delete() {
//        int size = repository.findAll().size();
//        repository.delete(100);
//        Assertions.assertEquals(repository.findAll().size(), size-1);
//    }
//}
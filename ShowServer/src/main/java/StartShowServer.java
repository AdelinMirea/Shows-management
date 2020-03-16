import networking.utils.AbstractServer;
import networking.utils.StringConcurrentServer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import service.Service;
import show.server.ShowServer;
import show.services.IShowServer;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class StartShowServer {

    public static void main(String[] args) throws IOException {
        Service service = getFactory().getBean(Service.class);
        Properties properties = new Properties();
        properties.load(new FileInputStream("ShowServer/src/main/resources/showserver.properties"));
        int defaultPort = 8080;
        int port = defaultPort;
        try{
            port = Integer.parseInt(properties.getProperty("show.server.port"));
        }catch (NumberFormatException ex){
            System.out.println("wrong port specification");
            System.out.println("Using:" + defaultPort);
        }

        IShowServer showServer = new ShowServer(service);

        AbstractServer server = new StringConcurrentServer(port, showServer);
        server.start();
    }

    static ApplicationContext getFactory() {
        return new ClassPathXmlApplicationContext("classpath:../resources/spring-events.xml");
    }
}

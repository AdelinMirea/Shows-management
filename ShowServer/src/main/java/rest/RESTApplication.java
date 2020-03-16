package rest;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan("rest")
@SpringBootApplication
public class RESTApplication {
    public static void main(String[] args) {
//        SpringApplication application = new SpringApplication(RESTApplication.class);
        SpringApplication.run(RESTApplication.class, args);
    }
}

package networking.dto;

import domain.User;

import java.time.LocalDate;

public class DTOUtils {

    public static UserDTO getDTO(User user){
        String username = user.getUsername();
        String pass = user.getPassword();
        return new UserDTO(username, pass);
    }

    public static BuyDTO getDTO(int id, String name, int no){
        return new BuyDTO(id, name, no);
    }

    public static DateDTO getDTO(LocalDate date){
        return new DateDTO(date);
    }
}

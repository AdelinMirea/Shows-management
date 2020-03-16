package networking.dto;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateDTO implements Requestable{

    private LocalDate date;

    public DateDTO(LocalDate date) {
        this.date = date;
    }

    @Override
    public String toRequest() {
        return date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }
}

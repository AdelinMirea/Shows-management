package repository;

import domain.Show;

import java.time.LocalDate;
import java.util.List;

public interface RepositoryShow extends Repository<Show, Integer> {
    void update(Show entity);
    Show find(int id);
    List<Show> findByDate(LocalDate date);
}

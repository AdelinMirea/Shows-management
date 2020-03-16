package repository;

import domain.LineUp;

import java.util.List;

public interface RepositoryLineUp extends Repository<LineUp, Integer> {
    LineUp findLineUp(int artistId, int showId);
    List<LineUp> findByShow(int idShow);
}

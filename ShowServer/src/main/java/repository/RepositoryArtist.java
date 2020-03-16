package repository;

import domain.Artist;

public interface RepositoryArtist extends Repository<Artist, Integer> {
    Artist find(Integer id);
}

package repository;

import domain.HasID;
import domain.Show;

import java.util.Collection;

public interface Repository<T extends HasID<ID>, ID> {
    Collection<T> findAll();
}

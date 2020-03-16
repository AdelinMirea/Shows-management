package repository;

import domain.User;

public interface RepositoryUser extends Repository<User, Integer> {

    boolean allowUser(String id, String pass);
}

package repository;

import domain.Ticket;

public interface RepositoryTicket extends Repository<Ticket, Integer> {
    Ticket save(Ticket ticket);
    void delete(Integer id);
}

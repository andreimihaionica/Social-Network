package com.example.social_network.repository;

import com.example.social_network.domain.Entity;

/**
 * Interface of repository
 *
 * @param <ID> -
 * @param <E>  -
 */
public interface Repository<ID, E extends Entity<ID>> {
    /**
     * Find entity by id
     *
     * @param id of entity
     * @return entity with id = id
     */
    E findOne(ID id);

    /**
     * Find all entities
     *
     * @return all entities values
     */
    Iterable<E> findAll();

    /**
     * Save entity in repository
     *
     * @param entity - entity to be saved
     * @return - entity
     */
    E save(E entity);

    /**
     * Delete entity from repository
     *
     * @param id - id of entity
     * @return entity
     */
    E delete(ID id);

    /**
     * Update entity in repository
     *
     * @param entity - entity to update
     * @return entity
     */
    E update(ID id, E entity);
}

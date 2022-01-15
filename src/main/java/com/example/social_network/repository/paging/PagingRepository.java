package com.example.social_network.repository.paging;


import com.example.social_network.domain.Entity;
import com.example.social_network.repository.Repository;

public interface PagingRepository<ID,
        E extends Entity<ID>>
        extends Repository<ID, E> {

    Page<E> findAll(Pageable pageable);
}

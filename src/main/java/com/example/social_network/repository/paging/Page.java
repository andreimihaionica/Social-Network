package com.example.social_network.repository.paging;

import java.util.stream.Stream;

public interface Page<E> {
    Pageable getPageable();

    Stream<E> getContent();
}

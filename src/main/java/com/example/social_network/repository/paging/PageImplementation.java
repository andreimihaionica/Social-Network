package com.example.social_network.repository.paging;

import java.util.stream.Stream;

public class PageImplementation<T> implements Page<T> {
    private final Pageable pageable;
    private final Stream<T> content;

    PageImplementation(Pageable pageable, Stream<T> content) {
        this.pageable = pageable;
        this.content = content;
    }

    @Override
    public Pageable getPageable() {
        return this.pageable;
    }

    @Override
    public Stream<T> getContent() {
        return this.content;
    }
}

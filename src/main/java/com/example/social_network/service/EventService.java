package com.example.social_network.service;

import com.example.social_network.domain.Event;
import com.example.social_network.domain.User;
import com.example.social_network.repository.paging.Page;
import com.example.social_network.repository.paging.Pageable;
import com.example.social_network.repository.paging.PageableImplementation;
import com.example.social_network.repository.paging.PagingRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class EventService {
    PagingRepository<Long, Event> repo;

    public EventService(PagingRepository<Long, Event> repo) {
        this.repo = repo;
    }

    public Event createEvent(User createdBy, String name, String description, String location, String type, LocalDateTime date, List<User> subscribers) {
        return repo.save(new Event(createdBy, name, description, location, type, date, subscribers));
    }

    public void deleteEvent(Long id) {
        repo.delete(id);
    }

    public Event getEvent(String name) {
        for (Event event : repo.findAll())
            if (Objects.equals(event.getName(), name))
                return event;
        return null;
    }

    public void updateEvent(String name, List<User> subscribers) {
        Event event = getEvent(name);
        event.setSubscribers(subscribers);
        repo.update(event.getId(), event);
    }

    public Set<Event> getEventsOnPage(int page) {
        Pageable pageable = new PageableImplementation(page, 1);
        Page<Event> eventsPage = repo.findAll(pageable);
        return eventsPage.getContent().collect(Collectors.toSet());
    }

    public Iterable<Event> getAllEvents() {
        return repo.findAll();
    }

}

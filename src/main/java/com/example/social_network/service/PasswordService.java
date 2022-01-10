package com.example.social_network.service;

import com.example.social_network.domain.Password;
import com.example.social_network.repository.Repository;

public class PasswordService {
    private final Repository<Long, Password> repo;

    public PasswordService(Repository<Long, Password> repo) {
        this.repo = repo;
    }

    public void addPassword(Password password) {
        repo.save(password);
    }

    public Password findOne(Long id) {
        return repo.findOne(id);
    }

    public Iterable<Password> findAll() {
        return repo.findAll();
    }
}

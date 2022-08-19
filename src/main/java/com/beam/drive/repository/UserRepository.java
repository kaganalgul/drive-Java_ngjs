package com.beam.drive.repository;

import com.beam.drive.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    public Optional<User> findByEmail(String email);
}

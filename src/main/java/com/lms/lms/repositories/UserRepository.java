package com.lms.lms.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.lms.lms.model.User;
import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByUsername(String username);
}

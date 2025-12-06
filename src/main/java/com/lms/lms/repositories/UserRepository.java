package com.lms.lms.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.lms.lms.model.User;

public interface UserRepository extends MongoRepository<User, String> {
    
}

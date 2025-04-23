package it.swam.backend.repository;

import it.swam.backend.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface UserRepository extends MongoRepository<User, String> {}

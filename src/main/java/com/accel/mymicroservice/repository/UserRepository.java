package com.accel.mymicroservice.repository;


import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.accel.mymicroservice.model.Users;



@Repository
public interface UserRepository extends MongoRepository<Users, String> {

	
}

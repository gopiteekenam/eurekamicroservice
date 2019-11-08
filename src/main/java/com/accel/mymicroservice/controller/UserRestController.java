package com.accel.mymicroservice.controller;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.accel.mymicroservice.EmployeeNotFoundException;
import com.accel.mymicroservice.model.Users;
import com.accel.mymicroservice.repository.UserRepository;

@RestController

public class UserRestController {
	private final Logger LOG = LoggerFactory.getLogger(getClass());

	private final UserRepository userRepository;

	public UserRestController(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	
	@GetMapping("/users")
	public List<Users> getAllUsers() {
		LOG.info("Getting all users.");
		return userRepository.findAll();
	}
	
	@GetMapping(value = "/user/{login}")
	public Optional<Users> getUser(@PathVariable String login) throws EmployeeNotFoundException {
		LOG.info("Getting user with ID: {}.", login);
		 Optional<Users> users = userRepository.findById(login);
		 if(!users.isPresent()) {
			 throw new EmployeeNotFoundException(login);
		 }
		 return users;
	}
	
	@PostMapping(value = "/user/create")
	public Users addNewUsers(@RequestBody Users user) {
		LOG.info("Saving user.");
		return userRepository.save(user);
	}
	

}

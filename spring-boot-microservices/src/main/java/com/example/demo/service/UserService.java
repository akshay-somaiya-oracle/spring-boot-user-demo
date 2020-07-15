package com.example.demo.service;

import java.net.URI;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.demo.exception.UserNotFoundException;
import com.example.demo.model.User;
import com.example.demo.repo.UserRepository;

@Component
public class UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	public Iterable<User> getAllUsers() {
		return userRepository.findAll();
	}

	public User geUserDetails(int id) {
		return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
	}

	public ResponseEntity<Object> addUser(User user) {
		userRepository.save(user);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(user.getId()).toUri();
		return ResponseEntity.created(location).build();
	}

	public void deleteUserById(int id) {
		if(!userRepository.findById(id).isEmpty()) {
			userRepository.deleteById(id);
		} else {
			throw new UserNotFoundException("User not found with id: " +id);
		}
	}

	public User updateUser(User user, int id) {
		return userRepository.findById(id).map(usr -> { usr.setId(user.getId());
			usr.setName(user.getName());
			usr.setBirthDate(user.getBirthDate());
			return userRepository.save(usr);})
			.orElseThrow(() -> new RuntimeException("User not found with ID " + id));
	}

}

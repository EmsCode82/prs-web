package com.prsweb.web;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.*;

import com.prsweb.business.JsonResponse;
import com.prsweb.business.User;
import com.prsweb.db.UserRepository;

@CrossOrigin()
@RestController
@RequestMapping("/users")
public class UserController {
	@Autowired
	private UserRepository userRepo;

	@GetMapping("/")
	public JsonResponse list() {
		JsonResponse jr = null;
		List<User> users = userRepo.findAll();
		if (users.size() > 0) {
			jr = JsonResponse.getInstance(users);
		} else {
			jr = JsonResponse.getErrorInstance("No user Found.");
		}
		return jr;
	}

	@PostMapping("/login")
	public JsonResponse login(@RequestBody User u) {
		JsonResponse jr = null;
		Optional<User> user = userRepo.findByUserNameAndPassword(u.getUserName(), u.getPassword());
		if (user.isPresent()) {
			jr = JsonResponse.getInstance(user.get());
		} else {
			jr = JsonResponse.getErrorInstance("Invalid username/pwd combination try again");
		}
		return jr;

	}

	@GetMapping("/{id}")
	public JsonResponse get(@PathVariable int id) {
		JsonResponse jr = null;
		Optional<User> user = userRepo.findById(id);
		if (user.isPresent()) {
			jr = JsonResponse.getInstance(user.get());
		} else {
			jr = JsonResponse.getErrorInstance("No user found for Id." + id);
		}
		return jr;
	}

	@PostMapping("/")
	public JsonResponse createUser(@RequestBody User u) {
		JsonResponse jr = null;

		try {
			u = userRepo.save(u);
			jr = JsonResponse.getInstance(u);
		} catch (DataIntegrityViolationException dive) {
			jr = JsonResponse.getErrorInstance(dive.getRootCause().getMessage());
			dive.printStackTrace();
		} catch (Exception e) {
			jr = JsonResponse.getErrorInstance("Error creating user: " + e.getMessage());
			e.printStackTrace();
		}

		return jr;
	}

	@PutMapping("/")
	public JsonResponse updateUser(@RequestBody User u) {
		JsonResponse jr = null;

		try {
			u = userRepo.save(u);
			jr = JsonResponse.getInstance(u);
		} catch (Exception e) {
			jr = JsonResponse.getErrorInstance("Error updating user: " + e.getMessage());
			e.printStackTrace();
		}

		return jr;
	}

	@DeleteMapping("/{id}")
	public JsonResponse deleteUser(@PathVariable int id) {
		JsonResponse jr = null;

		try {
			userRepo.deleteById(id);
			jr = JsonResponse.getInstance("User id: " + id + " deleted successfully");
		} catch (Exception e) {
			jr = JsonResponse.getErrorInstance("Error deleting user: " + e.getMessage());
			e.printStackTrace();
		}

		return jr;
	}
}

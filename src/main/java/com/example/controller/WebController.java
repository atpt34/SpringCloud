package com.example.controller;

//import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.model.User;
import com.example.model.repository.UserRepository;

@RestController
public class WebController {

	@Autowired
	private final JdbcTemplate jdbcTemplate;

	public WebController(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@GetMapping("/")
	public Integer get() {
		return this.jdbcTemplate.queryForObject("SELECT COUNT(*) FROM user_t", Integer.class);
	}

	@GetMapping("/all")
	public String getAll() {
		jdbcTemplate.query("select * from user_t", (rs, n) -> new User(rs.getLong("id"), rs.getString("name"), rs.getString("email"))).forEach(System.out::println);
		return "ok";
	}

	@Autowired
	UserRepository repo;

	@GetMapping("/findAll")
	public String findAll() {
		return repo.findAll().toString();
	}

	@GetMapping("/findByName")
	public String findByName(@RequestParam String name) {
		return repo.findByNameContaining(name).toString();
	}

	@GetMapping("/findByEmail")
	public String findByEmail(@RequestParam String email) {
		return repo.findByEmailContainingOrderByEmail(email).toString();
	}

	@GetMapping("/save")
	public String save(@RequestParam String name, @RequestParam String email) {
		User user = new User();
		user.setName(name);
		user.setEmail(email);
		repo.save(user);
		return "saved " + user.toString();
	}

	@GetMapping("/delete")
	public String delete(@RequestParam String id) {
		repo.deleteById(Long.parseLong(id));
		return "deleted";
	}
}

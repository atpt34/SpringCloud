package com.example.model.repository;

//import org.springframework.data.repository.CrudRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.model.User;

import java.util.List;

//public interface UserRepository extends CrudRepository<User, Long> {
public interface UserRepository extends JpaRepository<User, Long> {
	List<User> findByNameContaining(String name);
	List<User> findByEmailContainingOrderByEmail(String email);
}

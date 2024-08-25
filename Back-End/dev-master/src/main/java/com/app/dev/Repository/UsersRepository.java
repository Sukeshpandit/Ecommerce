package com.app.dev.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.app.dev.Model.Users;

public interface UsersRepository extends JpaRepository<Users, Long> {

	@Query("SELECT u FROM Users u WHERE u.userEmail = :email")
	Users findByUserEmail(@Param("email") String email);

	Users findByVerificationCode(String varificationCode);

	Users save(Optional<Users> user);
}

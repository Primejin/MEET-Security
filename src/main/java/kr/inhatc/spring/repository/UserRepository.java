package kr.inhatc.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.inhatc.spring.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

	public User findByUsername(String username);
}

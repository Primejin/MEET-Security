package kr.inhatc.spring.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Entity
@Data
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable=false, unique=true) //공백안되고 중복 안되게
	private String username;
	
	@Column(nullable=false)
	private String password;
	
	@Column
	private String email;
	
	private String provider;
	private String providerId;
	
	@Builder
	public User(Long id, String username, String password, String email, String provider, String providerId) {
		this.id =id;
		this.username = username;
		this.password = password;
		this.email = email;
		this.provider = provider;
		this.providerId = providerId;
	}
 
}

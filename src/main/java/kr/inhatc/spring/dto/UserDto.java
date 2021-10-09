package kr.inhatc.spring.dto;

import kr.inhatc.spring.entity.User;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserDto {

	private Long id;
	private String username;
	private String password;
	
	public User toEntity() {
		return User.builder()
				.id(id)
				.username(username)
				.password(password)
				.build();
	}
	
	@Builder
	public UserDto(Long id, String username, String password ) {
		this.id = id;
		this.username = username;
		this.password = password;
	}
}

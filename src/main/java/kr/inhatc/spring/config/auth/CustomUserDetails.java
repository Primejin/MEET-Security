package kr.inhatc.spring.config.auth;

import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import kr.inhatc.spring.entity.User;
import lombok.Data;

//로그인 진행이 완료되면 시큐리티 session 만들어줌
@Data //OAuth에서 authentication : 이거땜에
public class CustomUserDetails implements UserDetails, OAuth2User {

	private User user; //콤포지션
	
	private Map<String, Object> attributes;
	
	//일반 로그인
	public CustomUserDetails(User user) {
		this.user = user;
	}
	//OAuth 로그인
	public CustomUserDetails(User user, Map<String, Object> attributes) {
		this.user = user;
		this.attributes = attributes;
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		return user.getUsername();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public Map<String, Object> getAttributes() {
		return attributes;
	}

	@Override
	public String getName() {
		return null;
	}

}

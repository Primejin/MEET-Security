package kr.inhatc.spring.config.auth;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import kr.inhatc.spring.entity.User;
import kr.inhatc.spring.repository.UserRepository;
import lombok.RequiredArgsConstructor;

//시큐리티 설정에서 loginProcessingUrl("/login");
//login 요청이 오면 자동으로 UserDetailsService 타입으로 Ioc되어 있는 loadUserByUsername 함수 실행됨
@RequiredArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {

	private final UserRepository userRepository;
	
	//시큐리티 session(내부 Authentication(내부에 UserDetails)
	//함수 종료될 때 @AuthenticationPrincipal 어노테이션이 만들어짐
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User userEntity = userRepository.findByUsername(username);
		if(userEntity!=null) {
			return new CustomUserDetails(userEntity);
		}
		return null;
	}

}

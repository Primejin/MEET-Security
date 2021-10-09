package kr.inhatc.spring.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.inhatc.spring.entity.User;
import kr.inhatc.spring.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service //1.Ioc 2.트랜잭션 관리
public class AuthService {

	private final UserRepository userRepository;
	
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Transactional //함수가 실행되고 종료될때까지 트랜잭션 관리를 해줌(Write(insert, update, delete)할때 붙여준다) 
	public User signup(User user) { //여기서의 user는 외부에서 통신한 user 데이터
		//회원가입 진행
		String rawPassword = user.getPassword();
		String encPassword = bCryptPasswordEncoder.encode(rawPassword); //암호화시킴
		user.setPassword(encPassword);//password에 암호화된 암호 넣기
		
		User userEntity = userRepository.save(user);
		//userEntity는 데이터베이스에 있는 데이터 user오브젝트를 담은것
		return userEntity;
	}
}

package kr.inhatc.spring.config.oauth;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import kr.inhatc.spring.config.auth.CustomUserDetails;
import kr.inhatc.spring.entity.User;
import kr.inhatc.spring.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CustomOauth2UserService extends DefaultOAuth2UserService {


	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	
	private final UserRepository userRepository;
	
	//구글로부터 받은 userRequest 데이터에 대한 후처리되는 함수
	//함수 종료될 때 @AuthenticationPrincipal 어노테이션이 만들어짐
	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		System.out.println("getClientRegistration:"+userRequest.getClientRegistration());	//registrationId로 어떤 OAuth로 로그인했는지
		System.out.println("getAccessToken"+userRequest.getAccessToken().getTokenValue());
		
		
		OAuth2User oauth2User = super.loadUser(userRequest);
		//구글로그인 완료->code를 리턴(OAuth-client라이브러리)->AccessToken요청
		//userRequest 정보->loadUser 함수 호출->구글로부터 회원프로필 받아줌
		System.out.println("getAttributes"+oauth2User.getAttributes());
			
		//회원가입 강제로 진행
		String provider = userRequest.getClientRegistration().getClientId();//google
		String providerId = oauth2User.getAttribute("sub");
		String username = provider+"_"+providerId; //google_subId~~
		String password = bCryptPasswordEncoder.encode("구글회원비번");
		String email=oauth2User.getAttribute("email");
		
		//해당 아이디로 회원가입 되어있는 확인
		User userEntity = userRepository.findByUsername(username);
		
		if(userEntity==null) {
			System.out.println("구글 로그인 최초임");
			userEntity = User.builder()
					.username(username)
					.password(password)
					.email(email)
					.provider(provider)
					.providerId(providerId)
					.build();
			userRepository.save(userEntity);
		}else {
			System.out.println("구글 로그인 한적 있음. 자동회원가입 되어 있습니다");
		}
		return new CustomUserDetails(userEntity, oauth2User.getAttributes());
	}
}

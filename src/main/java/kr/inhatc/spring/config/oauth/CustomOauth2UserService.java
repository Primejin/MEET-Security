package kr.inhatc.spring.config.oauth;

import java.util.Map;
import java.util.UUID;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import kr.inhatc.spring.config.auth.CustomUserDetails;
import kr.inhatc.spring.config.oauth.provider.GoogleUserInfo;
import kr.inhatc.spring.config.oauth.provider.NaverUserInfo;
import kr.inhatc.spring.config.oauth.provider.OAuth2UserInfo;
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
			
		//일단 구글, 네이버 로그인 비교코드
		OAuth2UserInfo oAuth2UserInfo = null;
		if(userRequest.getClientRegistration().getRegistrationId().equals("google")) {
			System.out.println("구글 로그인 요청");
			oAuth2UserInfo = new GoogleUserInfo(oauth2User.getAttributes());
			//이 attributes가 GoogleUserInfo에 Map옆 attributes에 넘어가서 들어가고 정보들을 뽑아낼 수 있음
		}
		else if(userRequest.getClientRegistration().getRegistrationId().equals("naver")) {
			System.out.println("네이버 로그인 요청");
			oAuth2UserInfo = new NaverUserInfo((Map)oauth2User.getAttributes().get("response"));
		} //네이버는 정보가 response 안에 있기때문에 response 넣어서 접근해서 정보 갖고오고 NaverUserInfo에서 getAttributes로 정보들 갖고옴
		
		
		//회원가입 강제로 진행 (위에 한 UserInfo작업으로 인해 필요 없지만 쓰려면)
		String provider = oAuth2UserInfo.getProvider();
		String providerId = oAuth2UserInfo.getProviderId();
		String username = provider+"_"+providerId; //google_subId~~
		UUID randomPassword = UUID.randomUUID();	//구글로 가입하는 유저마다 비밀번호 다르게 하기위해 랜덤값생성
		String password = bCryptPasswordEncoder.encode("oauth"+randomPassword);
		String email = oAuth2UserInfo.getEmail();
		
		//해당 아이디로 회원가입 되어있는 확인
		User userEntity = userRepository.findByUsername(username);
		
		if(userEntity==null) {
			System.out.println("OAuth 로그인 최초임");
			userEntity = User.builder()
					.username(username)
					.password(password)
					.email(email)
					.provider(provider)
					.providerId(providerId)
					.build();
			userRepository.save(userEntity);
		}else {
			System.out.println("OAUth 로그인 한적 있음. 자동회원가입 되어 있습니다");
		}
		return new CustomUserDetails(userEntity, oauth2User.getAttributes());
	}
}

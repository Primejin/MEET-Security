package kr.inhatc.spring.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.inhatc.spring.config.auth.CustomUserDetails;
import kr.inhatc.spring.dto.UserDto;
import kr.inhatc.spring.entity.User;
import kr.inhatc.spring.repository.UserRepository;
import kr.inhatc.spring.service.AuthService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor //final 필드를 DI할때 사용
//@autowired 대신 쓰는이유 - 순환참조 방지하려고
//필드 주입 방식인 autowired 대신 생성자 주입 방식을 쓰는것, 그리고 final 사용가능(객체 불변성)
@Controller
public class UserController {

	private final AuthService authService;
	
	private final UserRepository userRepository;
	
	@GetMapping("/test/login")
	public @ResponseBody String testLogin(Authentication authentication,
			@AuthenticationPrincipal CustomUserDetails userDetails) { //DI의존성 주입
		System.out.println("/test/login===========");
		CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
		System.out.println("authentication:"+customUserDetails.getUser());
		
		System.out.println("userDetails: " + userDetails.getUser());
		return "세션 정보 확인하기";
	}
	
	@GetMapping("/test/oauth/login")
	public @ResponseBody String testOAuthLogin(Authentication authentication,
			@AuthenticationPrincipal OAuth2User oauth) { //DI의존성 주입
		System.out.println("/test/login===========");
		OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
		System.out.println("authentication:"+oauth2User.getAttributes());
		System.out.println("oauth2User:"+oauth.getAttributes());
		return "세션 정보 확인하기";
	}
	
	//OAuth 로그인, 일반 로그인 둘다 CustomUserDetails
	@GetMapping("/user")
	public @ResponseBody String user(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
		System.out.println("customUserDetails:"+customUserDetails.getUser());
		return "user";
	}
	
	@GetMapping({"/","/main"})
	public String main() {
		return "page/main";
	}
	
	@GetMapping("/signinForm")
	public String signinForm() {
		return "auth/signinForm";
	}
	
	@GetMapping("/signupForm")
	public String signupForm() {
		return "auth/signupForm";
	}
	
	@PostMapping("/signup")
	public String signup(UserDto userDto) {
		User user = userDto.toEntity();
		User userEntity = authService.signup(user);
		System.out.println(userEntity);
		return "auth/signinForm";
	}
}

package kr.inhatc.spring.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
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
	
	// 세션 테스트를 위한 코드 삽입
	@GetMapping({"/","/main"})
	public String main(HttpServletRequest request) {
		// 현재 인증 값 가져오기
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserDetails user = (UserDetails) authentication.getPrincipal();
		// userId 값 가져오기
		String username = user.getUsername();
		System.out.println("username :: " + username);
		// Session 생성
		HttpSession session = request.getSession();
		// 사용자 정보 가져오기
		User userEntity = userRepository.findByUsername(username);
		session.setAttribute("id", userEntity.getId());
		session.setAttribute("username", userEntity.getUsername());
		session.setAttribute("email", userEntity.getEmail());
		return "page/main";
	}
	
	// userEdit 테스트를 위한 코드 삽입
	@GetMapping("/userEdit")
	public String userEdit() {
		return "auth/userEdit";
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

package kr.inhatc.spring.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import kr.inhatc.spring.config.oauth.CustomOauth2UserService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity //스프링 필터체인에 등록
public class SecurityConfig extends WebSecurityConfigurerAdapter{

	private final CustomOauth2UserService customOauth2UserService;
	
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable();
		http.authorizeRequests()
			.antMatchers("/","/user/**").authenticated()
			.anyRequest().permitAll()
			.and()
			.formLogin()
				.loginPage("/signinForm")
				.loginProcessingUrl("/login")
				.defaultSuccessUrl("/")
			.and()
			.logout()
			.logoutSuccessUrl("/")
			.invalidateHttpSession(true).deleteCookies("user")
			.and()
			.oauth2Login()
			.loginPage("/signinForm")
			//구글 로그인 완료된 후처리 필요 (코드X 안받고 액세스토큰+사용자 프로필 정보를 한방에 받음
			//1.코드받기(인증) 2.액세스토큰(권한) 
			//3.사용자프로필 정보 가져옴 4-1.그 정보 토대로 회원가입 자동으로 진행시키기도함 
			//4-2 추가적인 정보 필요한 경우에는 정보추가 회원가입창
			.userInfoEndpoint()
			.userService(customOauth2UserService);
	}
}

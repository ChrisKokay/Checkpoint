package com.checkpointformcleaner.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * 
 * A @Configuration annotáció segítségével megmondjuk a Springnek, h ez az osztály is Bean legyen
 * A @EnableGlobalMethodSecurity segítségével megtudjuk azt mondani, h a Controllerben melyik definiált
 * függvényünket milyen szintű felhasználó érhesse el
 * 
 * @author Kiki
 *
 */

@EnableGlobalMethodSecurity(securedEnabled = true)
@Configuration
public class SecurityConf extends WebSecurityConfigurerAdapter {
		
	@Bean
	public UserDetailsService userDetailsService() {
	    return super.userDetailsService();
	}	
	
	@Autowired
	private UserDetailsService userService;
	
	@Autowired
	public void configureAuth(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userService);
	}
	
	//itt mélyebben bele tudunk nyúlni a szerver viselkedésébe
	@Override
	protected void configure(HttpSecurity httpsec) throws Exception {
		
		httpsec
			.authorizeRequests()
				.antMatchers("/admin/**").hasRole("ADMIN")
				//mindent engedélyezz, ami erre az end-point-ra érkezik
				.antMatchers("/registration").permitAll()
				.antMatchers("/reg").permitAll()
				//itt állítom be, h kötelező legyen autentikálni magunkat
				.anyRequest().authenticated()
				.and()
			//megmondjuk, h ne a default login page-ra vigyen, hanem az általunk készített login oldalra
			.formLogin()
				//megmondjuk, h hová irányítson
				.loginPage("/login")
				//megmondjuk, h bárki elérhesse
				.permitAll()
				//engedélyezzük mindenkinek
				.and()
			//ha kijelentkezünk, akkor egyből irányítson vissza a login oldalra
			.logout()
				//itt ki kell rendesen jelentkeztetni a usert
				.logoutRequestMatcher(new AntPathRequestMatcher("/logout"));
				//.logoutSuccessUrl("/logout")
				//.invalidateHttpSession(true)
				//.deleteCookies("JSESSIONID")
				//.permitAll();
		
	}

}

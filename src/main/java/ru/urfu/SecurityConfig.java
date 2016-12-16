package ru.urfu;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.inject.Inject;
import javax.inject.Named;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	@Inject @Named("userDetailsService")
	private UserDetailsService userDetailsService;

	@Inject
	private AuthenticationProvider authenticationProvider;

	//private myAccessDeniedHandler

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
            .authorizeRequests()
            .antMatchers("/css/**", "/js/**", "/login", "/signup", "/rest/messages/**", "/rest/messages/**")
                .permitAll() //todo authorization for REST
            .antMatchers("/**").hasRole("USER")
            .and()
            .formLogin()
            .loginPage("/login");
    }
    
    @Inject
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider);
    }


}

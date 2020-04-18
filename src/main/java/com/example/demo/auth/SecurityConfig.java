package com.example.demo.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    public static final String ENTRY_URL = "/";
    public static final String LOGIN_URL = "/login";
    public static final String LOGOUT_URL = "/logout";
    /*
    public static final String CSS_URL = "/css/**";
    public static final String JS_URL = "/js/**";
    */
    public static final String APP_URL = "/app/**";


    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
            .passwordEncoder(NoOpPasswordEncoder.getInstance());

    }

    @Autowired
    private AuthenticationSuccessHandler successHandler;

    @Autowired
    private AuthenticationFailureHandler failureHandler;

    protected CustomAuthFilter customAuthFilter() throws Exception {
        CustomAuthFilter customAuthFilter = new CustomAuthFilter();
        customAuthFilter.setAuthenticationSuccessHandler(successHandler);
        customAuthFilter.setAuthenticationFailureHandler(failureHandler);
        customAuthFilter.setAuthenticationManager(super.authenticationManagerBean());
        return customAuthFilter;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
            /*
            .csrfTokenRepository(new HttpSessionCsrfTokenRepository())
            .ignoringAntMatchers(ENTRY_URL)
            .ignoringAntMatchers(CSS_URL)
            .ignoringAntMatchers(JS_URL);
            */

        http.authorizeRequests()
            .antMatchers(APP_URL).authenticated()
            .anyRequest().permitAll();
            /*
            .antMatchers(CSS_URL).permitAll()
            .antMatchers(JS_URL).permitAll()
            .anyRequest().authenticated();
            */

        http.addFilterAt(customAuthFilter(), UsernamePasswordAuthenticationFilter.class)
            .formLogin()
            //.loginPage(ENTRY_URL).permitAll()
            .loginProcessingUrl(LOGIN_URL);

        http.logout()
            .logoutUrl(LOGIN_URL)
            .deleteCookies("JSESSIONID")
            .deleteCookies("isAuthenticated")
            .deleteCookies("displayname")
            .deleteCookies("authories")
            .invalidateHttpSession(true);
    }
}
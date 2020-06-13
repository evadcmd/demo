package com.example.demo.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    public static final String ENTRY_URL = "/";
    public static final String LOGIN_PROCESSING_URL = "/login";
    public static final String LOGOUT_PROCESSING_URL = "/logout";
    public static final String API_URL = "/api/**";

    @Autowired
    private UserDetailsService userDetailsService;

    // thread-safe
    @Autowired
    private BCryptPasswordEncoder bcryptPasswordEncoder;

    @Autowired
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
            .passwordEncoder(bcryptPasswordEncoder);
    }

    @Autowired
    private AuthenticationSuccessHandler successHandler;

    @Autowired
    private AuthenticationFailureHandler failureHandler;

    protected AuthFilter authFilter() throws Exception {
        AuthFilter authFilter = new AuthFilter();
        authFilter.setAuthenticationSuccessHandler(successHandler);
        authFilter.setAuthenticationFailureHandler(failureHandler);
        authFilter.setAuthenticationManager(super.authenticationManagerBean());
        return authFilter;
    }

    @Autowired
    private AntPathRequestMatcher antPathRequestMatcher;

    @Autowired
    private LogoutSuccessHandler logoutSuccessHandler;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // enable csrf
        http.csrf()
            .csrfTokenRepository(new HttpSessionCsrfTokenRepository())
            .requireCsrfProtectionMatcher(
                (request) -> antPathRequestMatcher.matches(request)
            );

        http.authorizeRequests()
            .antMatchers(API_URL).authenticated()
            .anyRequest().permitAll();

        http.addFilterAt(authFilter(), UsernamePasswordAuthenticationFilter.class)
            .formLogin()
            .loginPage(ENTRY_URL).permitAll()
            .loginProcessingUrl(LOGIN_PROCESSING_URL);

        http.logout()
            .logoutUrl(LOGOUT_PROCESSING_URL) // must be POST method if csrf is enabled
            .logoutSuccessHandler(logoutSuccessHandler);
    }
}
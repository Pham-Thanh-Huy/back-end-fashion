package com.example.fashion.security;

import com.example.fashion.security.JWT.JWTFilter;
import com.example.fashion.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    @Autowired
    private AccountService accountService;

    @Autowired
    private JWTFilter jwtFilter;

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws  Exception{
        return  configuration.getAuthenticationManager();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(){
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(accountService);
        daoAuthenticationProvider.setPasswordEncoder(bCryptPasswordEncoder());
        return daoAuthenticationProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
         http.authorizeHttpRequests(
                authorize -> authorize
                        .requestMatchers(HttpMethod.GET, SecurityEndpoints.PUBLIC_GET_ENDPOINTS).permitAll()
                        .requestMatchers(HttpMethod.POST, SecurityEndpoints.PUBLIC_POST_ENDPOINTS).permitAll()
                        .requestMatchers(HttpMethod.PUT, SecurityEndpoints.PUBLIC_PUT_ENDPOINTS).permitAll()
                        .requestMatchers(HttpMethod.GET, SecurityEndpoints.ADMIN_STAFF_GET_ENDPOINTS).hasAnyAuthority("ADMIN", "STAFF")
                        .requestMatchers(HttpMethod.POST, SecurityEndpoints.ADMIN_STAFF_POST_ENDPOINTS).hasAnyAuthority("ADMIN", "STAFF")
                        .requestMatchers(HttpMethod.PUT, SecurityEndpoints.ADMIN_STAFF_PUT_ENDPOINTS).hasAnyAuthority("ADMIN", "STAFF")
                        .requestMatchers(HttpMethod.DELETE, SecurityEndpoints.ADMIN_STAFF_DELETE_ENPOINTS).hasAnyAuthority("ADMIN", "STAFF")
                        .requestMatchers(HttpMethod.POST, SecurityEndpoints.ADMIN_POST_ENDPOINTS).hasAnyAuthority("ADMIN")
                        .requestMatchers(HttpMethod.PUT, SecurityEndpoints.ADMIN_PUT_ENDPOINTS).hasAnyAuthority("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, SecurityEndpoints.ADMIN_DELETE_ENDPOINTS).hasAnyAuthority("ADMIN")
                        .requestMatchers(SecurityEndpoints.AUTH_WHITELIST).permitAll()


         );
         http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
         http.sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
         http.csrf(csrf->csrf.disable());
         http.httpBasic(Customizer.withDefaults());
         return http.build();
    }
}

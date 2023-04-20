package com.ayderbek.springbootexample.config;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;


@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    private final LogoutHandler logoutHandler;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http
                .cors()
                .and()
                .csrf()
                .disable()
                .authorizeHttpRequests(
                        authorize -> authorize.requestMatchers(HttpMethod.POST, "/{property:\\d+}/image/upload").permitAll()
                                .requestMatchers("/api/v1/properties/**").hasAuthority("ROLE_USER")
                                .requestMatchers("/api/v1/users").permitAll()
                                .requestMatchers("/api/v1/reservations/**").hasAuthority("ROLE_USER")
                                .requestMatchers("/api/v1/reviews/**").hasAuthority("ROLE_USER")
                                .requestMatchers("/api/v1/hosts/**").hasAuthority("ROLE_USER")
                                .requestMatchers("/api/v1/wishlist/**").hasAuthority("ROLE_USER")
                                .requestMatchers("/api/v1/recommendations/**").hasAuthority("ROLE_USER")
                                .requestMatchers("/ws/**").hasAuthority("ROLE_USER")
                                .requestMatchers(HttpMethod.POST, "/api/v1/properties/create").permitAll()
                                .requestMatchers("/api/v1/auth/**").permitAll()
                                .anyRequest()
                                .authenticated()
                )
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .logout()
                .logoutUrl("/api/v1/auth/logout")
                .addLogoutHandler(logoutHandler)
                .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext());
        return http.build();
    }
}

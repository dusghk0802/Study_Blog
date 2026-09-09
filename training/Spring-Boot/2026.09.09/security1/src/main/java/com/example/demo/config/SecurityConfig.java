package com.example.demo.config;

import com.example.demo.repository.MemberRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;

@Component
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(MemberRepository memberRepository){
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                var member = memberRepository.findByEmail(username).orElseThrow(new Supplier<UsernameNotFoundException>(){
                    @Override
                    public UsernameNotFoundException get(){
                        return new UsernameNotFoundException("User not found: " + username);
                    }
                });
                return User.builder()
                        .username(member.getPassword())
                        .password(member.getPassword())
                        .authorities(member.getAuthority()).build();
            }
        };
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http){
        http.authorizeHttpRequests(new Customizer<AuthorizeHttpRequestsConfigurer<org.springframework.security.config.annotation.web.builders.HttpSecurity>.AuthorizationManagerRequestMatcherRegistry>() {
            @Override
            public void customize(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry authorize) {
                authorize.requestMatchers("/", "/home").permitAll()
                        .requestMatchers("/member/**").hasAnyAuthority("ROLE_ADMIN")
                        .anyRequest().authenticated();
            }
        }).formLogin(Customizer.withDefaults())
                .logout(Customizer.withDefaults());
        return http.build();
    }
}

package com.example.demo.config;

import com.example.demo.model.Member;
import com.example.demo.model.MemberUserDetails;
import com.example.demo.repository.AuthorityRepository;
import com.example.demo.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final MemberRepository memberRepository;
    private final AuthorityRepository authorityRepository;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
        throws Exception{
        http.csrf(Customizer.withDefaults())
                .authorizeHttpRequests(authorize-> authorize
                        .requestMatchers("/", "/article/list", "/article/content")
                        .permitAll()
                        .requestMatchers("/member/**")
                        .hasAnyAuthority("ROLE_ADMIN")
                        .requestMatchers("/signup")
                        .permitAll()
                        .anyRequest().authenticated()
                )
                .httpBasic(Customizer.withDefaults())
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/")
                        .permitAll());
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(){
        return username -> {
            Member member = memberRepository.findByEmail(username)
                    .orElseThrow();
            return new MemberUserDetails(member, authorityRepository.findByMember(member));
        };
    }
}

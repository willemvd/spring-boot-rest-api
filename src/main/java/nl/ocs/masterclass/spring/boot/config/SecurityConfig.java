package nl.ocs.masterclass.spring.boot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .httpBasic()
                .and()
                .authorizeRequests()
                    .antMatchers("/user/**").fullyAuthenticated()

                // OR
                    //.antMatchers(HttpMethod.GET, "/user/**").fullyAuthenticated()
                    //.antMatchers(HttpMethod.PUT, "/user/**").authenticated()
                // OR
                    //.anyRequest().fullyAuthenticated()

                .and()
                .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // disable cookies
                .and()
                .csrf().disable();

        return http.build();
    }
}

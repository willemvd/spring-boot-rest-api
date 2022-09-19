package nl.ocs.masterclass.spring.boot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.sql.DataSource;

@Configuration
public class SecurityConfig {

    @Bean
    UserDetailsManager users(DataSource dataSource, AuthenticationManagerBuilder auth, PasswordEncoder passwordEncoder) throws Exception {
        UserDetails user = User.builder()
                .username("user")
                .password(passwordEncoder.encode("p@ssw0rd!"))
                .roles("USER")
                .build();
        UserDetails admin = User.builder()
                .username("admin")
                .password(passwordEncoder.encode("p@ssw0rd!"))
                .roles("USER", "ADMIN")
                .build();

        return auth.jdbcAuthentication()
                .dataSource(dataSource)
                .withDefaultSchema()
                .withUser(user)
                .withUser(admin)
                .getUserDetailsService();
    }

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SecurityFilterChain h2ConsoleSecurityFilterChain(HttpSecurity http) throws Exception {
        http.requestMatcher(new AntPathRequestMatcher("/h2-console/**"));
        http.csrf().disable();
        http.headers((headers) -> headers.frameOptions().sameOrigin());
        return http.build();
    }

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

    @Bean
    public PasswordEncoder getPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }
}

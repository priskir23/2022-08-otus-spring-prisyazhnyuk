package ru.otus.config;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import ru.otus.services.CustomUserDetailsService;

@EnableWebSecurity
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests(getGetUrlSecurityForAuth("/edit"))
                .authorizeRequests(getGetUrlSecurityForAll("/books"))
                .authorizeRequests(getGetUrlSecurityForAll("/authors"))
                .authorizeRequests(getGetUrlSecurityForAll("/genres"))
                .authorizeRequests(getInterceptUrlSecurity(HttpMethod.POST, "/book", "ROLE_ADMIN"))
                .authorizeRequests(getInterceptUrlSecurity(HttpMethod.PUT, "/book", "ROLE_ADMIN"))
                .authorizeRequests(getInterceptUrlSecurity(HttpMethod.GET, "/book/*", "ROLE_ADMIN"))
                .authorizeRequests(getInterceptUrlSecurity(HttpMethod.DELETE, "/book/*", "ROLE_ADMIN"))
                .formLogin()
                .defaultSuccessUrl("/edit");
        return http.build();
    }

    private static Customizer<ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry> getInterceptUrlSecurity(HttpMethod m, String matcher, String... roles) {
        return (authz) -> authz
                .antMatchers(m, matcher)
                .hasAnyAuthority(roles);
    }

    private static Customizer<ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry> getGetUrlSecurityForAll(String matcher) {
        return (authz) -> authz
                .antMatchers(HttpMethod.GET, matcher)
                .permitAll();
    }

    private static Customizer<ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry> getGetUrlSecurityForAuth(String matcher) {
        return (authz) -> authz
                .antMatchers(HttpMethod.GET, matcher)
                .authenticated();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().antMatchers( "/" );
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, PasswordEncoder noOpPasswordEncoder, CustomUserDetailsService userDetailService)
            throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(userDetailService)
                .passwordEncoder(noOpPasswordEncoder)
                .and()
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

}



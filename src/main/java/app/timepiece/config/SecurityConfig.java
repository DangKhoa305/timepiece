package app.timepiece.config;


import app.timepiece.security.JwtAuthenticationFilter;
import app.timepiece.service.serviceImpl.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(customUserDetailsService)
                .passwordEncoder(new BCryptPasswordEncoder());
    }


    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .cors(withDefaults())
                .authorizeRequests((authorize) -> authorize
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers("/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs/**", "/swagger-resources/**").permitAll()
//                        .requestMatchers(HttpMethod.GET, "/api/watches/searchWatchByKeyword").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/watches/searchWatchByKeywordAndFilter").permitAll()
//                        .requestMatchers(HttpMethod.GET, "/api/watches/searchWatch").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/watches/top12/Approved").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/feedbacks/watch/{watchId}").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/watches/getAll").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/watches/{id}/getWatchById").permitAll()
                        .requestMatchers("/api/brands/**").permitAll()
                        .requestMatchers("/api/watch-types/**").permitAll()
                        .anyRequest().authenticated() // Tất cả các yêu cầu khác cần phải xác thực
                )
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}

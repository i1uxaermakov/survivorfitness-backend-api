package com.changeplusplus.survivorfitness.backendapi.config;

import com.changeplusplus.survivorfitness.backendapi.security.JwtRequestFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Collections;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().
                cors().configurationSource(new CorsConfigurationSource() {
            @Override
            public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                CorsConfiguration config = new CorsConfiguration();
                config.setAllowedOrigins(Collections.singletonList("*")); //todo change the allowed origins
                config.setAllowedMethods(Collections.singletonList("*"));
                config.setAllowCredentials(true);
                config.setAllowedHeaders(Collections.singletonList("*"));
                config.setExposedHeaders(Arrays.asList("Authorization"));
                config.setMaxAge(3600L);
                return config;
            }
        }).and().csrf().disable()
                .authorizeRequests()
                    .antMatchers("/api/v1/authenticate").permitAll()
                    .antMatchers("/api/v1/users/reset_password").permitAll()
                    .antMatchers("/v2/api-docs",
                                        "/configuration/ui",
                                        "/swagger-resources/**",
                                        "/configuration/security",
                                        "/swagger-ui.html",
                                        "/webjars/**",
                                        "/swagger-ui/*").permitAll()
                    .anyRequest().authenticated();
//            .authorizeRequests().anyRequest().permitAll();
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
    }

    /*
     * @Override protected void configure(AuthenticationManagerBuilder auth) throws
     * Exception {
     * auth.inMemoryAuthentication().withUser("admin").password("12345").authorities
     * ("admin").and(). withUser("user").password("12345").authorities("read").and()
     * .passwordEncoder(NoOpPasswordEncoder.getInstance()); }
     */

    /*
     * @Override protected void configure(AuthenticationManagerBuilder auth) throws
     * Exception { InMemoryUserDetailsManager userDetailsService = new
     * InMemoryUserDetailsManager(); UserDetails user =
     * User.withUsername("admin").password("12345").authorities("admin").build();
     * UserDetails user1 =
     * User.withUsername("user").password("12345").authorities("read").build();
     * userDetailsService.createUser(user); userDetailsService.createUser(user1);
     * auth.userDetailsService(userDetailsService); }
     */

    /*
     * @Bean public UserDetailsService userDetailsService(DataSource dataSource) {
     * return new JdbcUserDetailsManager(dataSource); }
     */

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

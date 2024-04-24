package com.bookStore.config;

import com.bookStore.services.MySimpleUrlAuthenticationSuccessHandler;
import com.bookStore.services.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder()
    {
        return NoOpPasswordEncoder.getInstance();
    }

    /*Все, что в url имеет приставку /user доступно только загеристрированным пользователям
    * c ролью USER, /admin соответственно ADMIN*/

    //Страницы /, /all_books, /registration, /login доступны всем пользователям
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/", "/all_books", "/registration").permitAll()
                        .requestMatchers("/user/**").hasRole("USER")
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .formLogin((form) -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        /*.defaultSuccessUrl("/user/main", true)*/
                        .successHandler(myAuthenticationSuccessHandler())
                        .permitAll()
                )
                .logout((logout) -> logout.permitAll());

        return http.build();
    }

    /*Вот это нужно для перенаправления на нужные URL в соответствии с ролями,
    * этот класс лежит в папочке services*/
    @Bean
    public AuthenticationSuccessHandler myAuthenticationSuccessHandler(){
        return new MySimpleUrlAuthenticationSuccessHandler();
    }

}
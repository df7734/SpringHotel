package com.example.springhotel.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;


@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    DataSource dataSource;

    // Create users in memory example
    /*
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("user")
                .password("password")
                .roles("ADMIN")
                .and()
                .withUser("user2")
                .password("password")
                .roles("USER");
    }
    */

    // Get users with authorities from DB (authorities, not roles?)
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication()
                .dataSource(dataSource)
                .passwordEncoder(NoOpPasswordEncoder.getInstance())
                .usersByUsernameQuery("SELECT username, password, 'true' as enabled FROM user WHERE username=?")
                .authoritiesByUsernameQuery("SELECT u.username, ur.roles FROM user u INNER JOIN user_role ur on u.id = ur.user_id where u.username=?");
    }

    // Set filters for pages (authorities, not roles?)
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/", "/main", "/registration").permitAll()
                .antMatchers("/rooms", "/users", "/bookings", "/bookings/add").hasAnyAuthority("USER", "ADMIN")
                .antMatchers("/**").hasAuthority("ADMIN")
                .and().formLogin().loginPage("/login")
                .defaultSuccessUrl("/main",true)
                .permitAll()
                .and().logout()
                .and().csrf().disable();
                //^ to Post via thymeleaf instead 403 ^//
    }


    @Bean
    public PasswordEncoder encoder(){
        return NoOpPasswordEncoder.getInstance();
    }
}

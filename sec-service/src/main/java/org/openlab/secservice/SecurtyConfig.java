package org.openlab.secservice;

import org.openlab.secservice.entities.AppUser;
import org.openlab.secservice.services.AccountService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

@Configuration
@EnableWebSecurity
public class SecurtyConfig extends WebSecurityConfigurerAdapter {
    private AccountService accountService;

    public SecurtyConfig(AccountService accountService) {
        this.accountService = accountService;
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                AppUser appUser = accountService.loadUserByUserName(username);
                Collection<GrantedAuthority> roles = appUser.getAppRoles()
                        .stream()
                        .map(r->new SimpleGrantedAuthority(r.getRoleName()))
                        .collect(Collectors.toList());

                /*Collection<GrantedAuthority> roles = new ArrayList<>();
                appUser.getAppRoles().forEach(r->roles.add(new SimpleGrantedAuthority(r.getRoleName())));*/
                return new User(appUser.getUsername(), appUser.getPassword(), roles);
            }
        });
    }
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        /*
            *STATEFUL AUTH IS USED BY DEFAULT
            * to use stateless auth one need to first disable csrf token
            * second we need to specify the type of auth using sessionManagement
        */
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.headers().frameOptions().disable();
        //http.formLogin();
        http.authorizeRequests().antMatchers("/h2-console/**").permitAll();
        http.authorizeRequests().anyRequest().authenticated();
    }

}

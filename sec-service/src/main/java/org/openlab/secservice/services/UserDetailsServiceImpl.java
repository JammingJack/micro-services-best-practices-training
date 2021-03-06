package org.openlab.secservice.services;

import org.openlab.secservice.entities.AppUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private AccountService accountService;

    public UserDetailsServiceImpl(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser appUser = accountService.loadUserByUserName(username);
        Collection<GrantedAuthority> roles = appUser.getAppRoles()
                .stream()
                .map(r->new SimpleGrantedAuthority(r.getRoleName()))
                .collect(Collectors.toList());
        return new User(appUser.getUsername(), appUser.getPassword(), roles);
    }
}

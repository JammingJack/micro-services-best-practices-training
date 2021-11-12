package org.openlab.secservice.services;

import org.openlab.secservice.entities.AppRole;
import org.openlab.secservice.entities.AppUser;

import java.util.List;

public interface AccountService {
    AppUser addNewUser(AppUser appUser);
    AppRole addNewRole(AppRole appRole);
    void addRoleToUser(String username, String roleName);
    AppUser loadUserByUserName(String username);
    List<AppUser> listUsers();
}

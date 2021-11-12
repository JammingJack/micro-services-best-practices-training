package org.openlab.secservice.repositories;

import org.openlab.secservice.entities.AppRole;
import org.openlab.secservice.entities.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppRoleRepository extends JpaRepository<AppRole, Long> {
    AppUser findByRolename(String roleName);
}

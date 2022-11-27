package com.cloudcomputing.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserService userService;
    private final RoleService roleService;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        com.cloudcomputing.entity.User user = userService.getUserByUsername(userName);
        if (Optional.ofNullable(user).isEmpty()) {
            System.out.println("User not found! " + userName);
            throw new UsernameNotFoundException("User " + userName + " was not found in the database.");
        }

        System.out.println("Found User: " + user);

        // [ROLE_USER, ROLE_ADMIN,..]
        List<String> roleNames = this.roleService.getRoleNames(user.getId());

        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        if (!CollectionUtils.isEmpty(roleNames)) {
            grantedAuthorities.addAll(roleNames.stream()
                    .map(SimpleGrantedAuthority::new)
                    .toList());
        }

        return new User(user.getUsername(), user.getPassword(), grantedAuthorities);
    }

}
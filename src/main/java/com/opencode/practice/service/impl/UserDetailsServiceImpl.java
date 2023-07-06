package com.opencode.practice.service.impl;

import com.opencode.practice.models.SecurityUser;
import com.opencode.practice.models.User;
import com.opencode.practice.repos.ReposUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service("userDetailsServiceImpl")
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private ReposUser reposUser;

    @Autowired
    public UserDetailsServiceImpl(ReposUser reposUser) {
        this.reposUser = reposUser;
    }


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = reposUser.findByEmail(email).orElseThrow(() ->
                new UsernameNotFoundException("User doesn't exists"));
        return SecurityUser.fromUser(user);
    }
}

package com.project.p_project.security;

import com.project.p_project.dao.abstracts.UserDao;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

import java.util.NoSuchElementException;

@Repository
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserDao userDao;

    public UserDetailsServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        try {
            return userDao.getByEmail(s).orElseThrow();
        } catch (UsernameNotFoundException | NoSuchElementException e) {
            throw new BadCredentialsException("Invalid username");
        }
    }
}

package com.example.backendfruitable.service;

import com.example.backendfruitable.repository.UserRepository;
import com.example.backendfruitable.entity.Authorize;
import org.springframework.beans.factory.annotation.Autowired;
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
public class AccountService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        com.example.backendfruitable.entity.User user = userRepository.getUserByUsername(username);
        if(user == null){
            throw new UsernameNotFoundException("Tài khoản không tồn tại");
        }
     User userDetail = new User(user.getUsername(), user.getPassword(),mapToAuthorize(user.getAuthorizeList()));
        return userDetail;
    }

    private Collection<? extends GrantedAuthority> mapToAuthorize(Collection<Authorize> authorizeList){
        return authorizeList.stream().map(authorize -> new SimpleGrantedAuthority(authorize.getAuthorizeName())).collect(Collectors.toList());
    }

}

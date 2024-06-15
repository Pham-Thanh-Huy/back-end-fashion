package com.example.fashion.service;

import com.example.fashion.repository.UserRepository;
import com.example.fashion.entity.Authorize;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class AccountService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        com.example.fashion.entity.User user = userRepository.getUserByUsername(username);
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

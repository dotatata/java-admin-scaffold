package com.jiayun.erp.wms.entity;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Getter
public class AuthUser extends org.springframework.security.core.userdetails.User {

    private User user;

    public AuthUser(User user, String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.user = user;
    }
}

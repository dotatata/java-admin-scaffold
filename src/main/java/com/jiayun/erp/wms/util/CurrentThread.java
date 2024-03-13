package com.jiayun.erp.wms.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class CurrentThread {

    public static User currentUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null && authentication.isAuthenticated()){
            Object principal = authentication.getPrincipal();
            if (principal instanceof UserDetails) {
                //System.out.println("((UserDetails) principal: " + principal);
                return (User) principal;
            }else {
                String username = principal.toString();
            }
        }else {
            return null;
        }
        return null;
    }
}

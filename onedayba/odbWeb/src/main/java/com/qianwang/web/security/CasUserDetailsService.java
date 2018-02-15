package com.qianwang.web.security;

import com.qianwang.service.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author by zhangchanghong on 15/12/6.
 */
public class CasUserDetailsService implements UserDetailsService, Serializable {
    private static final Logger log = LoggerFactory.getLogger(CasUserDetailsService.class);

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, DataAccessException {
        CasUser out = null;

        Map<String, Object> user = userService.getUserFromHyip(username);
        if (user != null && user.size() > 0) {

            List<String> roles = userService.getUserRoles(Long.parseLong(user.get("userId").toString()));

            final List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
            for (String ur : roles) {
                authorities.add(new SimpleGrantedAuthority(ur));
            }

            Object obj = user.get("userId");
            String nickName = "";
            String lastLoginTime = user.get(UserService.USER_LASTLOGIN_KEY).toString().trim();
            String casId = user.get("userId").toString().trim();
            if (obj != null) {
                out = new CasUser(Long.parseLong(obj.toString()), username, "", true, "", authorities);
                out.setNickName(nickName);
                out.setLastLoginTime(lastLoginTime);
                out.setCasId(casId);
            }
        }
        if (out == null) {
            throw new UsernameNotFoundException("error username");
        }
        return out;
    }

}

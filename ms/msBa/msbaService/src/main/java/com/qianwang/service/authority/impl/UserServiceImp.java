package com.qianwang.service.authority.impl;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by huguo on 2016/10/12.
 */
public class UserServiceImp extends JdbcDaoSupport implements UserDetailsService {

    public static final String usersByUsernameQuery =
            "select username,password,enabled " +
                    "from admin_ass_users " +
                    "where username = ?";

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
    {
        List<UserDetails> users = getJdbcTemplate().query(usersByUsernameQuery, new String[] {username}, new RowMapper<UserDetails>() {
            public UserDetails mapRow(ResultSet rs, int rowNum) throws SQLException {
                String username = rs.getString(1);
                String password = rs.getString(2);
                boolean enabled = rs.getBoolean(3);
                return new User(username, password, enabled, true, true, true, AuthorityUtils.NO_AUTHORITIES);
            }

        });

        UserDetails user = users.get(0);
        return user;
    }
}

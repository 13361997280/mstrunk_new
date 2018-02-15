package com.qianwang.web.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by DN0806 on 2016/5/10.
 */
public class LoginFilter extends OncePerRequestFilter {

    private final static Logger LOG = LoggerFactory.getLogger(LoginFilter.class);
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//        LOG.info("*********LoginFilter.doFilterInternal begin**********");
//        LOG.info("*********getRequestURI**********" + request.getRequestURI());
        if ("/api/account4Client/login".equals(request.getRequestURI())) {
            request.getRequestDispatcher("/api/v34/account4Client/login.do").forward(request, response);
        } else {
            filterChain.doFilter(request, response);
        }
    }
}

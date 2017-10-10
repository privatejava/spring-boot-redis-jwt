/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package np.com.ngopal.spring.boot.redis.jwt;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

/**
 *
 * @author NGM
 */
public class JWTAuthenticationFilter extends GenericFilterBean {

    private TokenAuthenticationService service;

    public JWTAuthenticationFilter(TokenAuthenticationService service) {
        this.service = service;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        Authentication authentication = service.getAuthentication((HttpServletRequest) request);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }
}

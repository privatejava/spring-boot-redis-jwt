/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package np.com.ngopal.spring.boot.redis.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import np.com.ngopal.spring.boot.redis.domain.AuthenticationTokenImpl;
import np.com.ngopal.spring.boot.redis.domain.SessionUser;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 *
 * @author NGM
 */
public class JWTLoginFilter extends AbstractAuthenticationProcessingFilter {

    private TokenAuthenticationService tokenAuthenticationService;

    public JWTLoginFilter(String url, AuthenticationManager authenticationManager, TokenAuthenticationService service) {
        super(new AntPathRequestMatcher(url));
        setAuthenticationManager(authenticationManager);
        tokenAuthenticationService = service;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest httpServletRequest, HttpServletResponse hsr1) throws AuthenticationException, IOException, ServletException {
        SessionUser credentials = new ObjectMapper().readValue(httpServletRequest.getInputStream(), SessionUser.class);
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(credentials.getUsername(), credentials.getPassword());
        return getAuthenticationManager().authenticate(token);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication)
            throws IOException, ServletException {
        
        AuthenticationTokenImpl auth = (AuthenticationTokenImpl) authentication;
        tokenAuthenticationService.addAuthentication(response, auth); 
    }
}

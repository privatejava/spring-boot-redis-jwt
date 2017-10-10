/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package np.com.ngopal.spring.boot.redis.domain;

import java.util.Collection;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.DigestUtils;

/**
 *
 * @author Narayan G. Maharjan &lt;me@ngopal.com.np&gt;
 */
@Slf4j
@ToString(callSuper = true)
public class AuthenticationTokenImpl extends AbstractAuthenticationToken {

    @Setter
    private String username;

    public AuthenticationTokenImpl(String principal, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.username = principal;
    }

    public void authenticate() {
        if (getDetails() != null && getDetails() instanceof SessionUser && !((SessionUser) getDetails()).hasExpired()) {
            setAuthenticated(true);
        } else {
            setAuthenticated(false);
        }
    }

    @Override
    public Object getCredentials() {
        return "";
    }

    @Override
    public Object getPrincipal() {
        return username != null ? username.toString() : "";
    }

    public String getHash() {
        return DigestUtils.md5DigestAsHex(String.format("%s_%d", username, ((SessionUser) getDetails()).getCreated().getTime()).getBytes());
    }

}

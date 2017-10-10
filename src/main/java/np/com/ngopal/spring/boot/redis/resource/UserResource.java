/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package np.com.ngopal.spring.boot.redis.resource;

import javax.servlet.http.HttpServletResponse;
import np.com.ngopal.spring.boot.redis.domain.AuthenticationTokenImpl;
import np.com.ngopal.spring.boot.redis.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author NGM
 */
@RestController
@RequestMapping("/user")
public class UserResource {

    @Autowired
    private RedisService service;

    @RequestMapping(method = RequestMethod.GET)
    public String getName(AuthenticationTokenImpl auth, HttpServletResponse response) {
        return auth.getPrincipal().toString();
    }

    @RequestMapping(value = "/processor", method = RequestMethod.GET)
    public Integer getProcessor(AuthenticationTokenImpl auth, HttpServletResponse response) {
        return Runtime.getRuntime().availableProcessors();
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout(AuthenticationTokenImpl auth, HttpServletResponse response) {
        service.setValue(auth.getPrincipal().toString().toLowerCase(), "");
        return "Logout Successfully";
    }

}

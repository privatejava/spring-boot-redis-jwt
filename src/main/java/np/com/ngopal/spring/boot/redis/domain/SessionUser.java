
package np.com.ngopal.spring.boot.redis.domain;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import lombok.*;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author NGM
 */
@Data
public class SessionUser {

    private String username;

    private String password;

    private Date created;

    public boolean hasExpired() {
        if(created == null){
            return true;
        }
        LocalDateTime localDateTime = created.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        localDateTime = localDateTime.plusHours(1);
        return  Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant()).before(new Date());
    }

}

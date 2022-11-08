package top.iseason.cmsystem.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import top.iseason.cmsystem.utils.Role;

import java.util.Collections;

@Data
@Accessors(chain = true)
@TableName("base_user")
public class BaseUser {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String mail;
    private String password;
    private Role role;

    public User toUser() {
        return new User(mail, password, Collections.singletonList(new SimpleGrantedAuthority(role.name())));
    }
}

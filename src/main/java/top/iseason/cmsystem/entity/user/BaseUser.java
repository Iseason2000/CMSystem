package top.iseason.cmsystem.entity.user;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import top.iseason.cmsystem.utils.Role;

import java.io.Serializable;
import java.util.Collections;

@Data
@Accessors(chain = true)
@TableName("base_user")
public class BaseUser implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer id;
    @ApiModelProperty("邮箱")
    private String mail;
    @ApiModelProperty("手机")
    private String phone;
    @ApiModelProperty("密码")
    private String password;
    @ApiModelProperty("权限")
    private Role role;

    public User toUser() {
        return new User(mail, password, Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role.name())));
    }
}

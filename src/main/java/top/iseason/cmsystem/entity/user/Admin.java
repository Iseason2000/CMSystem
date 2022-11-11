package top.iseason.cmsystem.entity.user;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
@TableName(value = "user_admin")
public class Admin implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer userId;
    @ApiModelProperty("管理员名字")
    private String name;
}

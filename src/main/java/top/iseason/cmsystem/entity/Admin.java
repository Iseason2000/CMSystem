package top.iseason.cmsystem.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@TableName(value = "user_admin")
public class Admin {
    @TableId
    private Integer id;
    private Integer userId;
    private String name;
}

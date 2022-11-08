package top.iseason.cmsystem.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@TableName(value = "user_team")
public class Team {
    @TableId
    private Integer id;
    private Integer userId;
    private String name;
    private String leader;
    private String member;
    private String instructor;
    private String profile;
}

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
@TableName(value = "user_team")
public class Team implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer userId;
    @ApiModelProperty("团队名称")
    private String name;
    @ApiModelProperty("队长")
    private String leader;
    @ApiModelProperty("成员")
    private String member;
    @ApiModelProperty("指导老师")
    private String instructor;
    @ApiModelProperty("团队简介")
    private String profile;
}

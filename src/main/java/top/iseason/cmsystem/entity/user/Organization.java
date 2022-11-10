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
@TableName(value = "user_organization")
public class Organization implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer userId;
    @ApiModelProperty("组织名称")
    private String name;
    @ApiModelProperty("所有者")
    private String owner;
    @ApiModelProperty("所有者身份证")
    private String idCard;
    @ApiModelProperty("组织简介")
    private String profile;
}

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
@TableName(value = "user_judge")
public class Judge implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer userId;
    @ApiModelProperty("名字")
    private String name;
    @ApiModelProperty("身份证")
    private String idCard;
    @ApiModelProperty("单位/公司")
    private String company;
    @ApiModelProperty("职业")
    private String career;
    @ApiModelProperty("简介")
    private String profile;
}

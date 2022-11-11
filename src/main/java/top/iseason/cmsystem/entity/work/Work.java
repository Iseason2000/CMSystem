package top.iseason.cmsystem.entity.work;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
@TableName(value = "work")
public class Work implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer teamId;
    private Integer realId;
    @ApiModelProperty("作品名称")
    private String name;
    @ApiModelProperty("作品介绍")
    private String profile;
    @ApiModelProperty("作品内容")
    private String content;
    @ApiModelProperty("作品得分")
    private Double score;

}
package top.iseason.cmsystem.entity.work;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@Data
@Accessors(chain = true)
@TableName(value = "real_work")
public class RealWork implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer teamId;
    @ApiModelProperty("作品名称")
    private String name;
    @ApiModelProperty("作品内容")
    private String content;
    @ApiModelProperty("邮寄状态")
    private String post;

    @ApiModelProperty("入库时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date inStoreTime;

    @ApiModelProperty("入库位置")
    private String position;

    @ApiModelProperty("出库时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date outStoreTime;

    @ApiModelProperty("出库状态")
    private String outState;

}
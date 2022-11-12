package top.iseason.cmsystem.entity.relationship;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
@TableName("relation_work_channel")
public class WorkChannel implements Serializable {
    @TableId
    private Integer workId;
    private Integer channelId;
    private Boolean accept;
}

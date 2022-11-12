package top.iseason.cmsystem.entity.relationship;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@TableName("relation_judge_channel")
public class JudgeChannel {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer judgeId;
    private Integer channelId;

}

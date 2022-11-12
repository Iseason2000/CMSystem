package top.iseason.cmsystem.entity.relationship;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@TableName("relation_work_score")
public class WorkScore {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer workId;
    private Integer judgeId;
    private Double score = 0.0;
}

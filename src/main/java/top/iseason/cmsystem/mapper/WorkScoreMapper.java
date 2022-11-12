package top.iseason.cmsystem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import top.iseason.cmsystem.entity.relationship.WorkScore;

@Mapper
@CacheNamespace
public interface WorkScoreMapper extends BaseMapper<WorkScore> {

    @Select("select avg(`score`) from `relation_work_score` where `work_id` = #{workId}")
    Double getScore(String workId);

}

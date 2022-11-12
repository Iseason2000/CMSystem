package top.iseason.cmsystem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import top.iseason.cmsystem.entity.relationship.JudgeChannel;
import top.iseason.cmsystem.entity.user.Judge;

import java.util.List;

@Mapper
@CacheNamespace
public interface JudgeChannelMapper extends BaseMapper<JudgeChannel> {

    @Select("select * from `user_judge`,`relation_judge_channel` where `relation_judge_channel`.`channel_id` = #{channel_id} and `user_judge`.`id` = `relation_judge_channel`.`judge_id`;")
    List<Judge> getJudges(String channelId);
}

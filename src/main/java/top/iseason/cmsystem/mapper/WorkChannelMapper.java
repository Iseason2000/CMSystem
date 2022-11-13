package top.iseason.cmsystem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import top.iseason.cmsystem.config.MybatisCache;
import top.iseason.cmsystem.entity.relationship.WorkChannel;
import top.iseason.cmsystem.entity.work.Work;

import java.util.List;

@Mapper
@CacheNamespace(implementation = MybatisCache.class, eviction = MybatisCache.class)
public interface WorkChannelMapper extends BaseMapper<WorkChannel> {

    @Select("select `work`.`id`,`work`.`team_id`,`work`.`real_id`,`work`.`name`,`work`.`profile` from `work`,`relation_work_channel` where `relation_work_channel`.`channel_id` = #{channelId} and `relation_work_channel`.`work_id` = `work`.`id`;")
    List<Work> getChannelWorks(String channelId);
}

package top.iseason.cmsystem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Mapper;
import top.iseason.cmsystem.entity.relationship.WorkToChannel;

@Mapper
@CacheNamespace
public interface WorkToChannelMapper extends BaseMapper<WorkToChannel> {
}

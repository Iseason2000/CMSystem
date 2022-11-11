package top.iseason.cmsystem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Mapper;
import top.iseason.cmsystem.entity.channel.Channel;

@Mapper
@CacheNamespace
public interface ChannelMapper extends BaseMapper<Channel> {
}

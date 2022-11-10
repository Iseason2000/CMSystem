package top.iseason.cmsystem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Mapper;
import top.iseason.cmsystem.entity.user.Judge;

@Mapper
@CacheNamespace
public interface JudgeMapper extends BaseMapper<Judge> {
}

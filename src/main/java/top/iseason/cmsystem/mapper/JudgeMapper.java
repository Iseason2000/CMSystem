package top.iseason.cmsystem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Mapper;
import top.iseason.cmsystem.config.MybatisCache;
import top.iseason.cmsystem.entity.user.Judge;

@Mapper
@CacheNamespace(implementation = MybatisCache.class, eviction = MybatisCache.class)
public interface JudgeMapper extends BaseMapper<Judge> {
}

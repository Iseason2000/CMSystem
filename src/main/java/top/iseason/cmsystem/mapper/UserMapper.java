package top.iseason.cmsystem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import top.iseason.cmsystem.entity.BaseUser;

@Mapper
public interface UserMapper extends BaseMapper<BaseUser> {
}

package top.iseason.cmsystem.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;
import top.iseason.cmsystem.entity.BaseUser;
import top.iseason.cmsystem.mapper.UserMapper;
import top.iseason.cmsystem.service.UserService;

@Service
@CacheConfig(cacheNames = "caffeineCacheManager")
public class UserServiceImpl extends ServiceImpl<UserMapper, BaseUser> implements UserService {
}

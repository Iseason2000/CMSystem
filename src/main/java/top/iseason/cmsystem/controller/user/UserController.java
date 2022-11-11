package top.iseason.cmsystem.controller.user;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.iseason.cmsystem.entity.user.*;
import top.iseason.cmsystem.mapper.*;
import top.iseason.cmsystem.utils.Result;
import top.iseason.cmsystem.utils.ResultCode;
import top.iseason.cmsystem.utils.Role;

import javax.annotation.Resource;

@Api(tags = "用户信息API, 需要登录")
@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    UserMapper userMapper;

    @Resource
    JudgeMapper judgeMapper;

    @Resource
    TeamMapper teamMapper;

    @Resource
    OrganizationMapper organizationMapper;

    @Resource
    AdminMapper adminMapper;

    @Transactional(readOnly = true)
    @ApiOperation(value = "获取用户信息", notes = "用户信息分为2部分，一个是用户通用信息，一个是角色信息")
    @GetMapping("/{id}")
    public Result getUserInfo(@PathVariable String id) {
        BaseUser baseUser = userMapper.selectById(id);
        if (baseUser == null) return Result.of(ResultCode.USER_NOT_EXIST);
        baseUser.setPassword(null);
        Role role = baseUser.getRole();
        Object user = null;
        switch (role) {
            case ADMIN: {
                user = adminMapper.selectOne(new QueryWrapper<Admin>().eq("user_id", id));
                break;
            }
            case JUDGE: {
                user = judgeMapper.selectOne(new QueryWrapper<Judge>().eq("user_id", id));
                break;
            }
            case ORGANIZATION: {
                user = organizationMapper.selectOne(new QueryWrapper<Organization>().eq("user_id", id));
                break;
            }
            case TEAM: {
                user = teamMapper.selectOne(new QueryWrapper<Team>().eq("user_id", id));
                break;
            }
        }
        if (user == null) return Result.of(ResultCode.USER_NOT_EXIST);
        return Result.success(baseUser, user);
    }


}

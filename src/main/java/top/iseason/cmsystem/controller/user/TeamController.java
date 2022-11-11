package top.iseason.cmsystem.controller.user;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.iseason.cmsystem.entity.user.BaseUser;
import top.iseason.cmsystem.entity.user.Team;
import top.iseason.cmsystem.mapper.TeamMapper;
import top.iseason.cmsystem.mapper.UserMapper;
import top.iseason.cmsystem.utils.Result;
import top.iseason.cmsystem.utils.ResultCode;
import top.iseason.cmsystem.utils.UserUtil;

import javax.annotation.Resource;

@Api(tags = "团队API, 需要登录团队")
@RestController
@RequestMapping("/team")
public class TeamController {

    @Resource
    TeamMapper teamMapper;

    @Resource
    UserMapper userMapper;

    @Transactional
    @ApiOperation(value = "修改团队信息", notes = "修改登录者自己的信息，仅修改传入的信息")
    @PutMapping("")
    public Result modifyTeam(
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String leader,
            @RequestParam(required = false) String member,
            @RequestParam(required = false) String instructor,
            @RequestParam(required = false) String profile) {
        Team team = UserUtil.getTeam();
        if (team == null) return Result.of(ResultCode.USER_NOT_EXIST);
        if (phone != null) {
            BaseUser user = UserUtil.getUser();
            user.setPhone(phone);
            userMapper.updateById(user);
        }
        if (name != null) team.setName(name);
        if (leader != null) team.setLeader(leader);
        if (member != null) team.setMember(member);
        if (instructor != null) team.setInstructor(instructor);
        if (profile != null) team.setProfile(profile);
        teamMapper.updateById(team);
        return Result.success(team);
    }

}

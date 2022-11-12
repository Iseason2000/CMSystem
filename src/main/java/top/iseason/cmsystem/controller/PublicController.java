package top.iseason.cmsystem.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.annotations.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.*;
import top.iseason.cmsystem.entity.channel.Channel;
import top.iseason.cmsystem.entity.user.BaseUser;
import top.iseason.cmsystem.entity.user.Judge;
import top.iseason.cmsystem.entity.user.Organization;
import top.iseason.cmsystem.entity.user.Team;
import top.iseason.cmsystem.mapper.*;
import top.iseason.cmsystem.utils.Result;
import top.iseason.cmsystem.utils.ResultCode;
import top.iseason.cmsystem.utils.Role;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Api(tags = "公开API，无权限校验")
@RestController
@RequestMapping("/public")
public class PublicController {

    @Resource
    CsrfTokenRepository tokenRepository;

    @Resource
    UserMapper userMapper;

    @Resource
    JudgeMapper judgeMapper;

    @Resource
    TeamMapper teamMapper;

    @Resource
    OrganizationMapper organizationMapper;

    @Resource
    ChannelMapper channelMapper;
    @Resource
    PasswordEncoder passwordEncoder;

    @ApiOperation("登录接口.")
    @PostMapping("/login")
    public Result fakeLogin(@RequestParam String username, @RequestParam String password, @RequestParam(value = "remember", required = false) String remember) {
        throw new IllegalStateException("This method shouldn't be called. It's implemented by Spring Security filters.");
    }

    @ApiOperation("退出接口.")
    @PostMapping("/logout")
    public Result fakeLogout() {
        throw new IllegalStateException("This method shouldn't be called. It's implemented by Spring Security filters.");
    }


    @ApiResponses({
            @ApiResponse(code = 200, message = "请求成功"),
    })
    @ApiOperation(value = "获取csrf验证token", notes = "安全接口,在所有请求的请求头中加入 'X-CSRF-TOKEN': token")
    @GetMapping("/csrf")
    public Result csrf(HttpServletRequest request, HttpServletResponse response) {
        CsrfToken csrfToken = tokenRepository.loadToken(request);
        if (csrfToken == null) {
            csrfToken = tokenRepository.generateToken(request);
            tokenRepository.saveToken(csrfToken, request, response);
        }
        return Result.success(csrfToken);
    }

    @Transactional
    @ApiResponses({
            @ApiResponse(code = 200, message = "请求成功"),
    })
    @ApiOperation(value = "注册裁判", notes = "安全接口,在所有请求的请求头中加入 'X-CSRF-TOKEN': token")
    @PostMapping("/register/judge")
    public Result registerJudge(
            @RequestParam String mail,
            @RequestParam String password,
            @RequestParam(required = false) String phone,
            @RequestParam String name,
            @RequestParam String idCard,
            @RequestParam String company,
            @RequestParam String career,
            @RequestParam(required = false) String profile) {
        BaseUser baseUser = new BaseUser()
                .setMail(mail)
                .setPassword(passwordEncoder.encode(password))
                .setRole(Role.JUDGE);
        try {
            userMapper.insert(baseUser);
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return Result.of(ResultCode.USER_ID_EXIST);
        }
        Judge judge = new Judge()
                .setUserId(baseUser.getId())
                .setName(name)
                .setIdCard(idCard)
                .setCompany(company)
                .setCareer(career)
                .setProfile(profile);
        judgeMapper.insert(judge);

        return Result.success(judge);
    }

    @Transactional
    @ApiResponses({
            @ApiResponse(code = 200, message = "请求成功"),
    })
    @ApiOperation(value = "注册团队", notes = "安全接口,在所有请求的请求头中加入 'X-CSRF-TOKEN': token")
    @PostMapping("/register/team")
    public Result registerTeam(
            @RequestParam String mail,
            @RequestParam String password,
            @RequestParam(required = false) String phone,
            @RequestParam String name,
            @RequestParam String leader,
            @RequestParam String member,
            @RequestParam String instructor,
            @RequestParam(required = false) String profile) {
        BaseUser baseUser = new BaseUser()
                .setMail(mail)
                .setPassword(passwordEncoder.encode(password))
                .setPhone(phone)
                .setRole(Role.TEAM);
        try {
            userMapper.insert(baseUser);
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return Result.of(ResultCode.USER_ID_EXIST);
        }
        Team team = new Team()
                .setUserId(baseUser.getId())
                .setName(name)
                .setLeader(leader)
                .setMember(member)
                .setInstructor(instructor)
                .setProfile(profile);
        teamMapper.insert(team);
        return Result.success(team);
    }

    @Transactional
    @ApiResponses({
            @ApiResponse(code = 200, message = "请求成功"),
    })
    @ApiOperation(value = "注册组织", notes = "安全接口,在所有请求的请求头中加入 'X-CSRF-TOKEN': token")
    @PostMapping("/register/organization")
    public Result registerOrganization(
            @RequestParam String mail,
            @RequestParam String password,
            @RequestParam(required = false) String phone,
            @RequestParam String name,
            @RequestParam String owner,
            @RequestParam String idCard,
            @RequestParam(required = false) String profile) {
        BaseUser baseUser = new BaseUser()
                .setMail(mail)
                .setPassword(passwordEncoder.encode(password))
                .setPhone(phone)
                .setRole(Role.ORGANIZATION);

        try {
            userMapper.insert(baseUser);
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return Result.of(ResultCode.USER_ID_EXIST);
        }

        Organization organization = new Organization()
                .setUserId(baseUser.getId())
                .setName(name)
                .setOwner(owner)
                .setIdCard(idCard)
                .setProfile(profile);

        organizationMapper.insert(organization);
        return Result.success(organization);
    }

    @ApiOperation(value = "获取所有比赛(没有内容)")
    @GetMapping("channel/{page}")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "page", value = "页码", dataType = "java.lang.Integer"),
            @ApiImplicitParam(name = "size", value = "每页容量，默认10", dataType = "java.lang.Integer"),
            @ApiImplicitParam(name = "isAsc", value = "是否顺序", dataType = "java.lang.Boolean")
    })
    public Result getWork(@PathVariable Integer page, @RequestParam(required = false) Integer size, @RequestParam(required = false) Boolean isAsc) {
        if (page == null || page <= 0) page = 1;
        if (size == null || size <= 0) size = 10;
        List<Channel> records = channelMapper.selectList(
                new LambdaQueryWrapper<Channel>()
                        .select(Channel.class, tableFieldInfo -> !tableFieldInfo.getColumn().equals("content"))
                        .orderBy(isAsc == null || isAsc, true, Channel::getId)
                        .last("limit " + (page - 1) * size + "," + size));
        return Result.success(records);
    }
}



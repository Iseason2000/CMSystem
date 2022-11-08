package top.iseason.cmsystem.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.context.annotation.Description;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import top.iseason.cmsystem.entity.BaseUser;
import top.iseason.cmsystem.entity.Judge;
import top.iseason.cmsystem.entity.Organization;
import top.iseason.cmsystem.entity.Team;
import top.iseason.cmsystem.mapper.JudgeMapper;
import top.iseason.cmsystem.mapper.OrganizationMapper;
import top.iseason.cmsystem.mapper.TeamMapper;
import top.iseason.cmsystem.mapper.UserMapper;
import top.iseason.cmsystem.utils.Result;
import top.iseason.cmsystem.utils.ResultCode;
import top.iseason.cmsystem.utils.Role;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
    PasswordEncoder passwordEncoder;

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
            @RequestParam Role role,
            @RequestParam String name,
            @RequestParam String idCard,
            @RequestParam String company,
            @RequestParam String career,
            @RequestParam(required = false) String profile) {
        BaseUser baseUser = new BaseUser()
                .setMail(mail)
                .setPassword(passwordEncoder.encode(password))
                .setRole(role);

        Judge judge = new Judge()
                .setName(name)
                .setIdCard(idCard)
                .setCompany(company)
                .setCareer(career)
                .setProfile(profile);

        userMapper.insert(baseUser);
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
            @RequestParam Role role,
            @RequestParam String name,
            @RequestParam String leader,
            @RequestParam String member,
            @RequestParam String instructor,
            @RequestParam(required = false) String profile) {
        BaseUser baseUser = new BaseUser()
                .setMail(mail)
                .setPassword(passwordEncoder.encode(password))
                .setRole(role);
        Team team = new Team()
                .setName(name)
                .setLeader(leader)
                .setMember(member)
                .setInstructor(instructor)
                .setProfile(profile);

        userMapper.insert(baseUser);
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
            @RequestParam Role role,
            @RequestParam String name,
            @RequestParam String owner,
            @RequestParam String idCard,
            @RequestParam(required = false) String profile) {
        BaseUser baseUser = new BaseUser()
                .setMail(mail)
                .setPassword(passwordEncoder.encode(password))
                .setRole(role);
        Organization organization = new Organization()
                .setName(name)
                .setOwner(owner)
                .setIdCard(idCard)
                .setProfile(profile);
        userMapper.insert(baseUser);
        organizationMapper.insert(organization);
        return Result.success(organization);
    }
}



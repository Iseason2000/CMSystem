package top.iseason.cmsystem.controller.user;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.iseason.cmsystem.entity.user.BaseUser;
import top.iseason.cmsystem.entity.user.Judge;
import top.iseason.cmsystem.mapper.JudgeMapper;
import top.iseason.cmsystem.mapper.UserMapper;
import top.iseason.cmsystem.utils.Result;
import top.iseason.cmsystem.utils.ResultCode;
import top.iseason.cmsystem.utils.UserUtil;

import javax.annotation.Resource;

@Api(tags = "裁判API, 需要登录裁判")
@RestController
@RequestMapping("/judge")
public class JudgeController {

    @Resource
    JudgeMapper judgeMapper;

    @Resource
    UserMapper userMapper;

    @Transactional
    @ApiOperation(value = "修改裁判信息", notes = "修改登录者自己的信息，仅修改传入的信息")
    @PutMapping("")
    public Result modifyJudge(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String idCard,
            @RequestParam(required = false) String company,
            @RequestParam(required = false) String career,
            @RequestParam(required = false) String profile) {

        Judge judge = UserUtil.getJudge();
        if (judge == null) return Result.of(ResultCode.USER_NOT_EXIST);
        if (phone != null) {
            BaseUser user = UserUtil.getUser();
            user.setPhone(phone);
            userMapper.updateById(user);
        }
        if (name != null) judge.setName(name);
        if (idCard != null) judge.setIdCard(idCard);
        if (company != null) judge.setCompany(company);
        if (career != null) judge.setCareer(career);
        if (profile != null) judge.setProfile(profile);
        judgeMapper.updateById(judge);
        return Result.success(judge);
    }
}

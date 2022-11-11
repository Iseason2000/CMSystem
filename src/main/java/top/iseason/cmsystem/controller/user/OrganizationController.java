package top.iseason.cmsystem.controller.user;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.iseason.cmsystem.entity.user.BaseUser;
import top.iseason.cmsystem.entity.user.Organization;
import top.iseason.cmsystem.mapper.OrganizationMapper;
import top.iseason.cmsystem.mapper.UserMapper;
import top.iseason.cmsystem.utils.Result;
import top.iseason.cmsystem.utils.ResultCode;
import top.iseason.cmsystem.utils.UserUtil;

import javax.annotation.Resource;

@Api(tags = "组织API, 需要登录组织")
@RestController
@RequestMapping("/organization")
public class OrganizationController {

    @Resource
    OrganizationMapper organizationMapper;

    @Resource
    UserMapper userMapper;

    @Transactional
    @ApiOperation(value = "修改组织信息", notes = "修改登录者自己的信息，仅修改传入的信息")
    @PutMapping("")
    public Result modifyTeam(
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String owner,
            @RequestParam(required = false) String idCard,
            @RequestParam(required = false) String profile) {

        Organization organization = UserUtil.getOrganization();
        if (organization == null) return Result.of(ResultCode.USER_NOT_EXIST);
        if (phone != null) {
            BaseUser user = UserUtil.getUser();
            user.setPhone(phone);
            userMapper.updateById(user);
        }
        if (name != null) organization.setName(name);
        if (owner != null) organization.setOwner(owner);
        if (idCard != null) organization.setIdCard(idCard);
        if (profile != null) organization.setProfile(profile);
        organizationMapper.updateById(organization);
        return Result.success(organization);
    }
}

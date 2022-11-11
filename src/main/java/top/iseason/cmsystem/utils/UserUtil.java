package top.iseason.cmsystem.utils;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.jetbrains.annotations.Nullable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import top.iseason.cmsystem.entity.user.*;
import top.iseason.cmsystem.mapper.*;

import javax.annotation.PostConstruct;

@Component
public class UserUtil {

    private static UserMapper userMapper;


    private static JudgeMapper judgeMapper;


    private static TeamMapper teamMapper;


    private static OrganizationMapper organizationMapper;


    private static AdminMapper adminMapper;

    public static BaseUser getUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!(principal instanceof User)) return null;
        User user = (User) principal;
        return userMapper.selectOne(new LambdaQueryWrapper<BaseUser>().eq(BaseUser::getMail, user.getUsername()));
    }

    @Nullable
    public static Team getTeam() {
        BaseUser user = getUser();
        if (user == null) return null;
        return teamMapper.selectOne(new LambdaQueryWrapper<Team>().eq(Team::getUserId, user.getId()));
    }

    @Nullable
    public static Judge getJudge() {
        BaseUser user = getUser();
        if (user == null) return null;
        return judgeMapper.selectOne(new LambdaQueryWrapper<Judge>().eq(Judge::getUserId, user.getId()));
    }

    @Nullable
    public static Organization getOrganization() {
        BaseUser user = getUser();
        if (user == null) return null;
        return organizationMapper.selectOne(new LambdaQueryWrapper<Organization>().eq(Organization::getUserId, user.getId()));
    }

    @Nullable
    public static Admin getAdmin() {
        BaseUser user = getUser();
        if (user == null) return null;
        return adminMapper.selectOne(new LambdaQueryWrapper<Admin>().eq(Admin::getUserId, user.getId()));
    }

    @PostConstruct
    public void init() {
        userMapper = MyApplicationContext.getBean(UserMapper.class);
        judgeMapper = MyApplicationContext.getBean(JudgeMapper.class);
        teamMapper = MyApplicationContext.getBean(TeamMapper.class);
        organizationMapper = MyApplicationContext.getBean(OrganizationMapper.class);
        adminMapper = MyApplicationContext.getBean(AdminMapper.class);
    }

}

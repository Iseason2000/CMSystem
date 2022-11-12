package top.iseason.cmsystem.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.*;
import top.iseason.cmsystem.entity.channel.Channel;
import top.iseason.cmsystem.entity.relationship.JudgeChannel;
import top.iseason.cmsystem.entity.relationship.WorkChannel;
import top.iseason.cmsystem.entity.relationship.WorkScore;
import top.iseason.cmsystem.entity.user.Judge;
import top.iseason.cmsystem.entity.user.Organization;
import top.iseason.cmsystem.entity.user.Team;
import top.iseason.cmsystem.entity.work.Work;
import top.iseason.cmsystem.mapper.*;
import top.iseason.cmsystem.utils.Result;
import top.iseason.cmsystem.utils.ResultCode;
import top.iseason.cmsystem.utils.UserUtil;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Api(tags = "作品API")
@RestController
@RequestMapping("/work")
public class WorkController {

    @Resource
    WorkMapper workMapper;
    @Resource
    ChannelMapper channelMapper;

    @Resource
    WorkChannelMapper workChannelMapper;

    @Resource
    JudgeChannelMapper judgeChannelMapper;

    @Resource
    WorkScoreMapper workScoreMapper;

    @ApiOperation(value = "获取该团队所有作品", notes = "需要团队权限，每页最多显示10个作品, (不显示内容)")
    @PreAuthorize("hasAnyRole('TEAM')")
    @GetMapping("page/{page}")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "page", value = "页码", dataType = "java.lang.Integer"),
            @ApiImplicitParam(name = "size", value = "每页容量，默认10", dataType = "java.lang.Integer"),
            @ApiImplicitParam(name = "isAsc", value = "是否顺序", dataType = "java.lang.Boolean")
    })
    public Result getWork(@PathVariable Integer page, @RequestParam(required = false) Integer size, @RequestParam(required = false) Boolean isAsc) {
        Team team = UserUtil.getTeam();
        if (team == null) return Result.failure();
        if (page == null || page <= 0) page = 1;
        if (size == null || size <= 0) size = 10;
        List<Work> records = workMapper.selectList(
                new LambdaQueryWrapper<Work>()
                        .select(Work.class, tableFieldInfo -> !tableFieldInfo.getColumn().equals("content"))
                        .eq(Work::getTeamId, team.getId())
                        .orderBy(isAsc == null || isAsc, true, Work::getId)
                        .last("limit " + (page - 1) * size + "," + size));
        return Result.success(records);
    }

    @ApiOperation(value = "根据某个作品ID获取内容", notes = "需要团队权限")
    @PreAuthorize("hasAnyRole('TEAM')")
    @GetMapping("/{id}")
    public Result getWork(@PathVariable Integer id) {
        Team team = UserUtil.getTeam();
        if (team == null) return Result.failure();
        Work work = workMapper.selectOne(new LambdaQueryWrapper<Work>().eq(Work::getId, id).eq(Work::getTeamId, team.getId()));
        if (work == null) return Result.of(ResultCode.WORK_NOT_EXIST);
        return Result.success(work);
    }

    @ApiOperation(value = "创建作品", notes = "需要团队权限")
    @PreAuthorize("hasAnyRole('TEAM')")
    @PostMapping("")
    public Result createWork(
            @RequestParam String name,
            @RequestParam String profile,
            @RequestParam String content
    ) {
        Team team = UserUtil.getTeam();
        if (team == null) return Result.failure();
        Work work = new Work()
                .setTeamId(team.getId())
                .setName(name)
                .setContent(content)
                .setProfile(profile);
        workMapper.insert(work);
        return Result.success(work);
    }

    @ApiOperation(value = "修改该团队作品", notes = "需要团队权限")
    @PreAuthorize("hasAnyRole('TEAM')")
    @PutMapping("/{id}")
    public Result modifyWork(
            @PathVariable String id,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String profile,
            @RequestParam(required = false) String content
    ) {
        Team team = UserUtil.getTeam();
        if (team == null) return Result.failure();
        Work work = workMapper.selectById(id);
        if (work == null) return Result.of(ResultCode.WORK_NOT_EXIST);
        if (name != null && !name.isEmpty()) work.setName(name);
        if (profile != null && !profile.isEmpty()) work.setProfile(profile);
        if (content != null && !content.isEmpty()) work.setContent(content);
        workMapper.updateById(work);
        return Result.success(work);
    }

    @ApiOperation(value = "删除该团队作品", notes = "需要团队权限")
    @PreAuthorize("hasAnyRole('TEAM')")
    @DeleteMapping("/{id}")
    public Result deleteWork(@PathVariable String id) {
        Team team = UserUtil.getTeam();
        if (team == null) return Result.failure();
        int i = workMapper.delete(new LambdaQueryWrapper<Work>().eq(Work::getId, id).eq(Work::getTeamId, team.getId()).last("limit 1"));
        if (i == 1) return Result.success();
        else return Result.of(ResultCode.WORK_NOT_EXIST);
    }

    @Transactional
    @ApiOperation(value = "将作品投递至赛道", notes = "需要团队权限")
    @PreAuthorize("hasAnyRole('TEAM')")
    @PostMapping("/channel/{channelId}/{workId}")
    public Result postWorkToChannel(
            @PathVariable String channelId,
            @PathVariable String workId
    ) {
        Team team = UserUtil.getTeam();
        if (team == null) return Result.failure();
        Channel channel = channelMapper.selectOne(new LambdaQueryWrapper<Channel>().select(Channel::getId).eq(Channel::getId, channelId));
        if (channel == null) return Result.of(ResultCode.CHANNEL_NOT_EXIST);
        Work work = workMapper.selectOne(new LambdaQueryWrapper<Work>().select(Work::getId).eq(Work::getId, workId).eq(Work::getTeamId, team.getId()));
        if (work == null) return Result.of(ResultCode.WORK_NOT_EXIST);
        WorkChannel workToChannel = new WorkChannel().setWorkId(work.getId()).setChannelId(channel.getId());
        try {
            workChannelMapper.insert(workToChannel);
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return Result.of(ResultCode.WORK_ALREADY_EXIST_CHANNEL);
        }
        return Result.success(workToChannel);
    }

    @ApiOperation(value = "查看某个赛道所有投递的作品", notes = "需要组织或管理员权限,(不显示内容)")
    @PreAuthorize("hasAnyRole('ADMIN','ORGANIZATION')")
    @GetMapping("channels/{channelId}")
    public Result getWorksFromChannel(
            @PathVariable String channelId
    ) {
        List<Work> channelWorks = workChannelMapper.getChannelWorks(channelId);
        return Result.success(channelWorks);
    }

    @Transactional
    @ApiOperation(value = "审核某个作品", notes = "需要组织权限, true 为通过, false 不通过")
    @PreAuthorize("hasAnyRole('ADMIN','ORGANIZATION')")
    @PutMapping("/accept/{workId}")
    public Result acceptWork(
            @PathVariable String workId,
            @RequestParam Boolean accept
    ) {
        Organization organization = UserUtil.getOrganization();
        if (organization == null) return Result.failure();
        WorkChannel workToChannel = workChannelMapper.selectById(workId);
        if (workToChannel == null) return Result.of(ResultCode.WORK_NOT_EXIST_CHANNEL);
        boolean exists = channelMapper.exists(new LambdaQueryWrapper<Channel>().eq(Channel::getId, workToChannel.getChannelId()).eq(Channel::getOrganizationId, organization.getId()));
        if (!exists) return Result.of(ResultCode.NO_PERMISSION);
        workToChannel.setAccept(accept);
        workChannelMapper.updateById(workToChannel);
        return Result.success(workToChannel);
    }

    @ApiOperation(value = "将作品从赛道删除", notes = "需要团队权限")
    @PreAuthorize("hasAnyRole('TEAM')")
    @DeleteMapping("/channel/{channelId}/{workId}")
    public Result delWorkFromChannel(
            @PathVariable String channelId,
            @PathVariable String workId
    ) {
        Team team = UserUtil.getTeam();
        if (team == null) return Result.failure();
        Channel channel = channelMapper.selectOne(new LambdaQueryWrapper<Channel>().select(Channel::getId).eq(Channel::getId, channelId));
        if (channel == null) return Result.of(ResultCode.CHANNEL_NOT_EXIST);
        Work work = workMapper.selectOne(new LambdaQueryWrapper<Work>().select(Work::getId).eq(Work::getId, workId).eq(Work::getTeamId, team.getId()));
        if (work == null) return Result.of(ResultCode.WORK_NOT_EXIST);
        int delete = workChannelMapper.delete(new LambdaQueryWrapper<WorkChannel>().eq(WorkChannel::getWorkId, workId).eq(WorkChannel::getChannelId, channelId));
        if (delete != 1) return Result.of(ResultCode.WORK_NOT_EXIST_CHANNEL);
        return Result.success();
    }

    @Transactional
    @ApiOperation(value = "给作品评分", notes = "需要裁判权限")
    @PreAuthorize("hasAnyRole('JUDGE')")
    @PostMapping("/score/{workId}")
    public Result workScore(
            @PathVariable String workId,
            @RequestParam Double score
    ) {
        Judge judge = UserUtil.getJudge();
        if (judge == null) return Result.of(ResultCode.NO_PERMISSION);
        WorkChannel workChannel = workChannelMapper.selectById(workId);
        if (workChannel == null) return Result.of(ResultCode.WORK_NOT_EXIST_CHANNEL);
        JudgeChannel judgeChannel = judgeChannelMapper.selectOne(new LambdaQueryWrapper<JudgeChannel>().eq(JudgeChannel::getJudgeId, judge.getId()).eq(JudgeChannel::getChannelId, workChannel.getChannelId()));
        if (judgeChannel == null) return Result.of(ResultCode.NO_PERMISSION);
        WorkScore workScore = new WorkScore()
                .setJudgeId(judge.getId())
                .setWorkId(workChannel.getWorkId())
                .setScore(score);
        try {
            workScoreMapper.insert(workScore);
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            Result.of(ResultCode.WORK_ALREADY_EXIST_SCORE);
        }
        return Result.success(workScore);
    }

    @ApiOperation(value = "获取某个作品的平均分", notes = "需要组织/团队权限")
    @PreAuthorize("hasAnyRole('TEAM','ORGANIZATION')")
    @GetMapping("/score/{workId}")
    public Result getScore(
            @PathVariable String workId
    ) {
        return Result.success(workScoreMapper.getScore(workId));
    }

    @ApiOperation(value = "获取该团队所有参加该赛道的作品", notes = "需要团队权限")
    @PreAuthorize("hasAnyRole('TEAM')")
    @GetMapping("/channel/{channelId}")
    public Result delWorkFromChannel(@PathVariable String channelId) {
        Team team = UserUtil.getTeam();
        if (team == null) return Result.failure();
        List<WorkChannel> works = workChannelMapper.selectList(new LambdaQueryWrapper<WorkChannel>().eq(WorkChannel::getChannelId, channelId));
        List<Integer> collect = works.stream().map(WorkChannel::getWorkId).collect(Collectors.toList());
        List<Work> result = workMapper.selectBatchIds(collect);
        return Result.success(result);
    }

}

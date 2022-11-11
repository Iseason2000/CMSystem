package top.iseason.cmsystem.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import top.iseason.cmsystem.entity.channel.Channel;
import top.iseason.cmsystem.entity.relationship.WorkToChannel;
import top.iseason.cmsystem.entity.user.Team;
import top.iseason.cmsystem.entity.work.Work;
import top.iseason.cmsystem.mapper.ChannelMapper;
import top.iseason.cmsystem.mapper.WorkMapper;
import top.iseason.cmsystem.mapper.WorkToChannelMapper;
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
    WorkToChannelMapper workToChannelMapper;

    @ApiOperation(value = "获取该团队所有作品(没有内容)", notes = "需要团队权限，每页最多显示10个作品")
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
            @RequestParam String profile) {
        Team team = UserUtil.getTeam();
        if (team == null) return Result.failure();
        Work work = new Work()
                .setTeamId(team.getId())
                .setName(name)
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

    @ApiOperation(value = "将作品投递至赛道", notes = "需要团队权限")
    @PreAuthorize("hasAnyRole('TEAM')")
    @PostMapping("/relationship/{channelId}/{workId}")
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
        WorkToChannel workToChannel = new WorkToChannel().setWorkId(work.getId()).setChannelId(channel.getId());
        try {
            workToChannelMapper.insert(workToChannel);
        } catch (Exception e) {
            return Result.of(ResultCode.WORK_ALREADY_EXIST_CHANNEL);
        }
        return Result.success(workToChannel);
    }

    @ApiOperation(value = "将作品从赛道删除", notes = "需要团队权限")
    @PreAuthorize("hasAnyRole('TEAM')")
    @DeleteMapping("/relationship/{channelId}/{workId}")
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
        int delete = workToChannelMapper.delete(new LambdaQueryWrapper<WorkToChannel>().eq(WorkToChannel::getWorkId, workId).eq(WorkToChannel::getChannelId, channelId));
        if (delete != 1) return Result.of(ResultCode.WORK_NOT_EXIST_CHANNEL);
        return Result.success();
    }

    @ApiOperation(value = "获取该团队所有参加该赛道的作品", notes = "需要团队权限")
    @PreAuthorize("hasAnyRole('TEAM')")
    @GetMapping("/relationship/{channelId}")
    public Result delWorkFromChannel(@PathVariable String channelId) {
        Team team = UserUtil.getTeam();
        if (team == null) return Result.failure();
        List<WorkToChannel> works = workToChannelMapper.selectList(new LambdaQueryWrapper<WorkToChannel>().eq(WorkToChannel::getChannelId, channelId));
        List<Integer> collect = works.stream().map(WorkToChannel::getWorkId).collect(Collectors.toList());
        List<Work> result = workMapper.selectBatchIds(collect);
        return Result.success(result);
    }
}

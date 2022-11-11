package top.iseason.cmsystem.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import top.iseason.cmsystem.entity.channel.Channel;
import top.iseason.cmsystem.entity.user.Organization;
import top.iseason.cmsystem.mapper.ChannelMapper;
import top.iseason.cmsystem.mapper.OrganizationMapper;
import top.iseason.cmsystem.mapper.UserMapper;
import top.iseason.cmsystem.utils.Result;
import top.iseason.cmsystem.utils.ResultCode;
import top.iseason.cmsystem.utils.UserUtil;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Api(tags = "赛道API")
@RestController
@RequestMapping("/channel")
public class ChannelController {
    @Resource
    UserMapper userMapper;

    @Resource
    ChannelMapper channelMapper;

    @Resource
    OrganizationMapper organizationMapper;

    @ApiOperation(value = "获取该组织所有比赛(没有内容)", notes = "需要组织权限")
    @PreAuthorize("hasAnyRole('ORGANIZATION')")
    @GetMapping("page/{page}")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "page", value = "页码", dataType = "java.lang.Integer"),
            @ApiImplicitParam(name = "size", value = "每页容量，默认10", dataType = "java.lang.Integer"),
            @ApiImplicitParam(name = "isAsc", value = "是否顺序", dataType = "java.lang.Boolean")
    })
    public Result getWork(@PathVariable Integer page, @RequestParam(required = false) Integer size, @RequestParam(required = false) Boolean isAsc) {
        Organization organization = UserUtil.getOrganization();
        if (organization == null) return Result.failure();
        if (page == null || page <= 0) page = 1;
        if (size == null || size <= 0) size = 10;
        List<Channel> records = channelMapper.selectList(
                new LambdaQueryWrapper<Channel>()
                        .select(Channel.class, tableFieldInfo -> !tableFieldInfo.getColumn().equals("content"))
                        .eq(Channel::getOrganizationId, organization.getId())
                        .orderBy(isAsc == null || isAsc, true, Channel::getId)
                        .last("limit " + (page - 1) * size + "," + size));
        return Result.success(records);
    }

    @ApiOperation(value = "创建赛道", notes = "需要组织权限")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "title", value = "比赛标题", dataType = "java.lang.String", required = true),
            @ApiImplicitParam(name = "content", value = "比赛内容", dataType = "java.lang.String", required = true),
            @ApiImplicitParam(name = "endTime", value = "截止时间", dataType = "java.util.Date", required = true),
    })
    @PreAuthorize("hasAnyRole('ORGANIZATION')")
    @PostMapping("")
    public Result createChannel(
            @RequestParam String title,
            @RequestParam String content,
            @RequestParam Date endTime) {
        Organization organization = UserUtil.getOrganization();
        if (organization == null) return Result.of(ResultCode.USER_NOT_EXIST);
        Channel channel = new Channel()
                .setOrganizationId(organization.getId())
                .setTitle(title)
                .setContent(content)
                .setEndTime(endTime);
        channelMapper.insert(channel);
        return Result.success(channel);
    }

    @ApiOperation(value = "修改该组织赛道", notes = "需要组织权限")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "title", value = "比赛标题", dataType = "java.lang.String"),
            @ApiImplicitParam(name = "content", value = "比赛内容", dataType = "java.lang.String"),
            @ApiImplicitParam(name = "endTime", value = "截止时间", dataType = "java.util.Date"),
    })
    @PreAuthorize("hasAnyRole('ORGANIZATION')")
    @PutMapping("/{id}")
    public Result modifyChannel(
            @PathVariable String id,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String content,
            @RequestParam(required = false) Date endTime) {

        Organization organization = UserUtil.getOrganization();
        if (organization == null) return Result.of(ResultCode.USER_NOT_EXIST);
        Channel channel = channelMapper.selectOne(new LambdaQueryWrapper<Channel>().eq(Channel::getId, id).eq(Channel::getOrganizationId, organization.getId()));
        if (channel == null) return Result.of(ResultCode.CHANNEL_NOT_EXIST);
        if (title != null && !title.isEmpty()) channel.setTitle(title);
        if (content != null && !content.isEmpty()) channel.setContent(content);
        if (endTime != null) channel.setEndTime(endTime);
        channelMapper.updateById(channel);
        return Result.success(channel);
    }

    @ApiOperation(value = "删除该组织的赛道", notes = "需要组织权限")
    @PreAuthorize("hasAnyRole('ADMIN','ORGANIZATION')")
    @DeleteMapping("/{id}")
    public Result deleteChannel(@PathVariable String id) {
        Organization organization = UserUtil.getOrganization();
        if (organization == null) return Result.of(ResultCode.USER_NOT_EXIST);
        int i = channelMapper.delete(new LambdaQueryWrapper<Channel>().eq(Channel::getId, id).eq(Channel::getOrganizationId, organization.getId()));
        if (i == 1) return Result.success();
        else return Result.of(ResultCode.CHANNEL_NOT_EXIST);
    }
}

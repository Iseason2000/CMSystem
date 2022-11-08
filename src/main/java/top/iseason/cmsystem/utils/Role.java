package top.iseason.cmsystem.utils;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * 用户角色
 */
public enum Role {

    ADMIN(0, "系统管理员"),
    JUDGE(1, "裁判"),
    ORGANIZATION(2, "大赛组织者"),
    TEAM(3, "参赛团队");

    @EnumValue
    private final int ordinal;
    /**
     * 角色说明
     */
    @JsonValue
    private final String name;

    Role(int ordinal, String name) {
        this.ordinal = ordinal;
        this.name = name;
    }

}

package top.iseason.cmsystem.utils;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ResultCode {
    /* 成功 */
    SUCCESS(200, "请求成功"),

    /* 默认失败 */
    COMMON_FAIL(999, "请求失败"),

    /* 参数错误：1000～1999 */
    PARAM_NOT_VALID(1001, "参数无效"),
    PARAM_IS_BLANK(1002, "参数为空"),
    PARAM_TYPE_ERROR(1003, "参数类型错误"),
    PARAM_NOT_COMPLETE(1004, "参数缺失"),

    /* 用户错误 */
    USER_LOGIN_SUCCESS(2100, "登录成功"),
    USER_LOGOUT_SUCCESS(2101, "退出成功"),
    USER_NOT_LOGIN(2001, "用户未登录"),
    USER_ACCOUNT_EXPIRED(2002, "账号已过期"),
    USER_CREDENTIALS_ERROR(2003, "用户名或密码错误"),
    USER_CREDENTIALS_EXPIRED(2004, "密码过期"),
    USER_ACCOUNT_DISABLE(2005, "账号不可用"),
    USER_ACCOUNT_LOCKED(2006, "账号被锁定"),
    USER_ACCOUNT_NOT_EXIST(2007, "账号不存在"),
    USER_ACCOUNT_ALREADY_EXIST(2008, "账号已存在"),
    USER_ACCOUNT_USE_BY_OTHERS(2009, "账号下线"),
    USER_NOT_EXIST(2201, "用户ID不存在"),
    USER_ID_EXIST(2202, "邮箱已注册"),
    CHANNEL_NOT_EXIST(3001, "赛道不存在"),
    WORK_NOT_EXIST(4001, "作品不存在"),
    WORK_ALREADY_EXIST_CHANNEL(4002, "作品已投入该赛道"),
    WORK_ALREADY_EXIST_SCORE(4003, "作品已评分"),
    WORK_NOT_EXIST_CHANNEL(4004, "该作品没有参与该赛道"),
    JUDGE_ALREADY_EXIST_CHANNEL(4011, "该裁判已经在此赛道"),
    JUDGE_NOT_EXIST_CHANNEL(4012, "该裁判不在在此赛道"),

    /* 业务错误 */
    NO_PERMISSION(9001, "没有权限");

    /**
     * 状态码
     */
    @Getter
    @Setter
    private Integer code;

    /**
     * 状态描述
     */
    @Getter
    @Setter
    private String message;

    ResultCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * 根据code获取message
     *
     * @param code
     * @return
     */
    public static String getMessageByCode(Integer code) {
        for (ResultCode ele : values()) {
            if (ele.getCode().equals(code)) {
                return ele.getMessage();
            }
        }
        return null;
    }

}
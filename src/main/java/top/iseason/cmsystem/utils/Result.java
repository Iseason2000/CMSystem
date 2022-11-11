package top.iseason.cmsystem.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * API结果集
 */
@ApiModel(description = "响应封装类")
@Data
@Accessors(chain = true)
public class Result {
    /**
     * 状态码
     */
    @ApiModelProperty("状态码")
    private ResultCode result;
    /**
     * 数据 (如果有的话)
     */
    @ApiModelProperty("返回数据(可能不存在)")
    private Object data;
    private final static ObjectMapper objectMapper;

    static {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper = mapper;
    }

    public static Result success(Object... data) {
        return new Result()
                .setResult(ResultCode.SUCCESS)
                .setData(data);
    }

    public static Result success(Object data) {
        return new Result()
                .setResult(ResultCode.SUCCESS)
                .setData(data);
    }

    public static Result success() {
        return new Result()
                .setResult(ResultCode.SUCCESS);
    }

    public static Result failure(Object... data) {
        return new Result()
                .setResult(ResultCode.COMMON_FAIL)
                .setData(data);
    }

    public static Result failure(Object data) {
        return new Result()
                .setResult(ResultCode.COMMON_FAIL)
                .setData(data);
    }

    public static Result failure() {
        return new Result()
                .setResult(ResultCode.COMMON_FAIL);
    }

    public static Result of(ResultCode code, Object... data) {
        return new Result()
                .setResult(code)
                .setData(data);
    }

    public static Result of(ResultCode code) {
        return new Result().setResult(code);
    }

    @Override
    public String toString() {
        try {
            return objectMapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            System.out.println(e);
            return null;
        }
    }

}

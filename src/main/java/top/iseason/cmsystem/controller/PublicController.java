package top.iseason.cmsystem.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.context.annotation.Description;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.iseason.cmsystem.utils.Result;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Api(tags = "公开API，无权限校验")
@RestController
@RequestMapping("/public")
public class PublicController {

    @Resource
    CsrfTokenRepository tokenRepository;

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


}

package top.iseason.cmsystem.controller.user;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "管理员API, 需要登录管理员")
@RestController
@RequestMapping("/admin")
public class AdminController {
}

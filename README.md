# 大赛管理系统

> 数据库大作业，基于 SSM 框架的 RESTFUL 后端

## 运行说明

双击 startup.bat 启动服务端，在开启的窗口按 ctrl+c 关闭服务端

浏览器输入 http://localhost/doc.html 打开API文档

默认的管理员用户是

账号: admin@mail.com

密码: admin123654

## 调试说明

在 接口文档中的 csrf接口添加 AfterScript 以支持每次调用接口自动设置 header

~~~ javascript
var code=ke.response.data.result.code;
if(code==200){
    //判断,如果服务端响应code是8200才执行操作
    //获取token
    var token=ke.response.data.data[0].token;
    var heaer=ke.response.data.data[0].headerName;
    //1、如何参数是Header，则设置当前逻辑分组下的全局Header
    ke.global.setHeader(heaer,token);
}
~~~
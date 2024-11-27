## 帖子后端启动说明
``` bash
打开IDEA构建Maven项目后
启动src/main/java/di0retsa/userlogin/UserLoginApplication.class即可运行项目
```
**确保端口5000未被占用**

## 功能完成情况
- [x] 包装JWT Token工具类
- [x] 包装加密解密工具类
- [x] 实现用户注册基本功能
- [x] 实现用户登录基本功能
- [ ] 重载接口留出登陆后修改密码的接口
- [ ] 开放token解析接口以便其他服务调用

## 配置文件
``` bash
配置文件位于：src/main/resources/application.yml
```
### 主要配置信息
- SpringBoot
  - MySQL数据库
  - Hikari连接池
  - servlet
- 服务器端口
- MyBatis
- MyBatisPlus
- JWT工具类
- 加解密工具类

## 依赖管理
项目依赖通过 Maven 管理，定义在 `pom.xml` 文件中。
主要依赖：
- **Spring Boot**
- **Lombok**
- **JJWT**
- **Fastjson2**
- **Hutool**
- **MyBatis**
- **MySQL-Connector**

## 接口
- POST —— /api/auth/register —— 注册新用户
- POST —— /api/auth/login —— 用户登录

## 项目结构
项目主要分为以下几个板块：

- **config**
  - HikariLoaderConfiguration: 在项目启动时自动启动连接池以加快第一次请求的响应速度
  - WebConfiguration: 解决跨域问题
- **controller**
  - UserLoginController: 接收用户注册登录等请求
- **entity**
  - dto
    - UserLoginDTO: 匹配前端发来的请求数据
  - exception
    - BaseException: 自定义异常抽象类
    - ErrorPasswordException: 密码错误异常
    - ErrorTokenException: Token解析异常错误
    - IllegalPasswordException: 密码非法异常
    - IllegalStuIdException: 学号非法异常
    - TextIsBlankException: 请求数据为空异常
    - UserExistException: 用户已存在异常（注册时）
    - UserNonException: 用户不存在异常（登陆时）
  - vo
    - UserLoginVO: 匹配前端接受的请求数据
  - EncryptProperties: 加密工具属性类
  - JWTProperties: JWTToken工具属性类
  - Result: 封装响应结果
  - StatusCode: Http状态码枚举类
  - User: 用户实体类
- **mapper**
  - UserMapper: 处理用户相关数据库操作
- **service**
  - impl
    - UserLoginServiceImpl: 用户注册登录相关服务具体实现类
  UserLoginService: 用户注册登录相关服务接口
- **util**
  - EncryptUtil: 加解密工具类
  - JWTUtil: JWT Token生成/解析工具类

## 接口说明
**Token解析接口(POST)**

示例请求
``` 示例请求
http://SERVER_IP:PORT/api/auth/token?token=YOUR_TOKEN
```

响应结果
``` 响应结果
{
    "username": String,
    "userId": String,
    "role": Integer
}
```

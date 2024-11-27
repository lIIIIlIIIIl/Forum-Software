package di0retsa.userlogin.controller;

import di0retsa.userlogin.entity.Result;
import di0retsa.userlogin.entity.User;
import di0retsa.userlogin.entity.dto.UserLoginDTO;
import di0retsa.userlogin.entity.exception.*;
import di0retsa.userlogin.entity.vo.UserAuthorizationVO;
import di0retsa.userlogin.entity.vo.UserLoginVO;
import di0retsa.userlogin.service.UserLoginService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


/**
 * 用户登录系统Controller
 */
@RestController
@Slf4j
@RequestMapping("api/auth/")
@RequiredArgsConstructor
public class UserLoginController {

    private final UserLoginService userLoginService;

    /**
     * 新用户注册
     * @param userLoginDTO 用户注册信息
     * @return
     * @throws Throwable
     */
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserLoginDTO userLoginDTO) throws Throwable {
        try{
            User user = userLoginService.userRegister(userLoginDTO);
            return Result.success();
        } catch (BaseException e) {
            return Result.error(e);
        }
    }

    /**
     * 登录
     * @param userLoginDTO 用户注册信息
     * @return
     * @throws Throwable
     */
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserLoginDTO userLoginDTO) throws Throwable {
        try{
            UserLoginVO user = userLoginService.userLogin(userLoginDTO);
            return Result.success(user);
        } catch (BaseException e) {
            return Result.error(e);
        }
    }

    /**
     * 校验JWTToken有效性
     * @param token JWT令牌
     * @return 令牌有效时：userId 令牌无效时
     */
    @PostMapping("/token")
    public ResponseEntity<String> parseJWTToken(@RequestParam String token) throws Throwable{
        try{
            UserAuthorizationVO user = userLoginService.userAuthorize(token);
            return Result.success(user);
        } catch (BaseException e) {
            return Result.error(e);
        }
    }
}

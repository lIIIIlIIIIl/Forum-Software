package di0retsa.userlogin.service.impl;

import cn.hutool.core.util.StrUtil;
import di0retsa.userlogin.entity.User;
import di0retsa.userlogin.entity.dto.UserLoginDTO;
import di0retsa.userlogin.entity.exception.*;
import di0retsa.userlogin.entity.vo.UserAuthorizationVO;
import di0retsa.userlogin.entity.vo.UserLoginVO;
import di0retsa.userlogin.mapper.UserMapper;
import di0retsa.userlogin.service.UserLoginService;
import di0retsa.userlogin.util.EncryptUtil;
import di0retsa.userlogin.util.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 用户登录服务实现类
 */
@Service
@RequiredArgsConstructor
public class UserLoginServiceImpl implements UserLoginService {
    /**
     * 加密工具类，用于加密密码
     */
    private final EncryptUtil encryptUtil;

    /**
     * JWT Token工具类，用户生成Token
     */
    private final JWTUtil jwtUtil;

    /**
     * 处理用户相关SQL
     */
    private final UserMapper userMapper;

    /**
     * 参数合法性检验
     * @param userLoginDTO
     * @return
     * @throws Throwable
     */
    private static Boolean LegalTest(UserLoginDTO userLoginDTO) throws Throwable {
        // 非空检验
        if (userLoginDTO == null || StrUtil.isBlank(userLoginDTO.getStuId()) || StrUtil.isBlank(userLoginDTO.getPassword())) {
            throw new TextIsBlankException();
        }
        // 密码合法性检验
        if (!userLoginDTO.isLegalPassword()) {
            throw new IllegalPasswordException();
        }
        // 学号合法性检验
        if (!userLoginDTO.isLegalStuId()){
            throw new IllegalStuIdException();
        }
        return Boolean.TRUE;
    }

    /**
     * 用户登录
     * @param userLoginDTO
     * @return
     * @throws Throwable
     */
    @Override
    public UserLoginVO userLogin(UserLoginDTO userLoginDTO) throws Throwable {
        LegalTest(userLoginDTO);
        // 查询用户是否存在
        User user = userMapper.getByStuId(userLoginDTO.getStuId());
        if (!Objects.nonNull(user)) {
            throw new UserNonException();
        }
        // 检验密码
        String storePassword = encryptUtil.encrypt(userLoginDTO.getPassword().getBytes(), Cipher.ENCRYPT_MODE);
        if (!storePassword.equals(user.getPassword())) {
            throw new ErrorPasswordException();
        }
        // 生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getUserId());
        claims.put("username", user.getUsername());
        claims.put("role", user.getRole());

        String jwtToken = jwtUtil.createJWT(user.getRole(), claims);
        return UserLoginVO.builder()
                .userId(user.getUserId())
                .token(jwtToken)
                .message("登陆成功!")
                .username(user.getUsername())
                .build();
    }

    /**
     * 用户注册
     * @param userLoginDTO
     * @return
     * @throws Throwable
     */
    @Override
    public User userRegister(UserLoginDTO userLoginDTO) throws Throwable {
        LegalTest(userLoginDTO);
        // 查询用户是否存在
        User user = userMapper.getByStuId(userLoginDTO.getStuId());
        if (Objects.nonNull(user)) {
            throw new UserExistException();
        }
        User newUser = User.builder()
                .stuId(userLoginDTO.getStuId())
                .password(encryptUtil.encrypt(userLoginDTO.getPassword().getBytes(),Cipher.ENCRYPT_MODE))
                .username(userLoginDTO.getStuId()) // 默认用户名与学号一致
                .role(0)
                .createTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .build();
        userMapper.insert(newUser);
        return newUser;
    }

    /**
     * JWTToken校验
     * @param jwtToken JWT令牌
     * @return 校验不通过:null 校验通过:userAuthorizationVO
     */
    @Override
    public UserAuthorizationVO userAuthorize(String jwtToken) throws Throwable {
        Map<String, Object> map = jwtUtil.paresJWT(jwtToken);
        return map.isEmpty()
                ? null
                : UserAuthorizationVO.builder()
                    .username((String) map.get("username"))
                    .userId((String) map.get("userId"))
                    .role((Integer) map.get("role"))
                    .build();
    }

    // TODO:修改密码重载
}

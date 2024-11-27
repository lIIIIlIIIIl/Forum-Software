package di0retsa.userlogin.service;

import di0retsa.userlogin.entity.User;
import di0retsa.userlogin.entity.dto.UserLoginDTO;
import di0retsa.userlogin.entity.vo.UserAuthorizationVO;
import di0retsa.userlogin.entity.vo.UserLoginVO;

public interface UserLoginService {
    UserLoginVO userLogin(UserLoginDTO userLoginDTO) throws Throwable;

    User userRegister(UserLoginDTO userLoginDTO) throws Throwable;

    UserAuthorizationVO userAuthorize(String jwtToken) throws Throwable;
}

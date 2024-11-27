package di0retsa.userlogin.entity.vo;

import lombok.Builder;
import lombok.Data;

/**
 * 用户权限校验返回实体类
 */
@Builder
@Data
public class UserAuthorizationVO {
    /**
     * 用户名
     */
    private String username;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 角色
     */
    private Integer role;
}

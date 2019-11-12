package boss.portal.controller;

import boss.portal.constant.ConstantKey;
import boss.portal.entity.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 描述：
 * <p>
 *
 * @author: 赵新国
 * @date: 2018/6/22 13:49
 */
@RestController
@Api(value = "登录管理", description = "登录管理")
public class LoginController extends BaseController {

   @ApiOperation(value = "自定义登录")
   @RequestMapping(value = "/login")
    public void login(String username, String password, HttpServletResponse response) {
       System.out.println(username);
        User userVo = userRepository.findByUsername(username);
        if (userVo != null) {

            // * 自定义生成Token，因为使用了自定义登录，就不会执行JWTLoginFilter了，所以需要字段调用生成token并返给前端

            // 这里可以根据用户信息查询对应的角色信息，这里为了简单，我直接设置个空list
            List roleList = new ArrayList<>();
            String subject = userVo.getUsername() + "-" + roleList;
                    String token = Jwts.builder()
                    .setSubject(subject)
                    .setExpiration(new Date(System.currentTimeMillis() + 365 * 24 * 60 * 60 * 1000)) // 设置过期时间 365 * 24 * 60 * 60秒(这里为了方便测试，所以设置了1年的过期时间，实际项目需要根据自己的情况修改)
                    .signWith(SignatureAlgorithm.HS512, ConstantKey.SIGNING_KEY) //采用什么算法是可以自己选择的，不一定非要采用HS512
                    .compact();
            // 登录成功后，返回token到header里面
            response.addHeader("Authorization", "Bearer " + token);
        }
    }
}

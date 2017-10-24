package com.beini.controller;

import com.beini.bean.UserBean;
import com.beini.http.BaseResponseJson;
import com.beini.service.UserService;
import com.beini.util.RedisCacheUtil;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by beini on 2017/10/20.
 */
@Controller
public class UserController {
    /**
     * 交互流程
     * 客户端通过登录请求提交用户名和密码，服务端验证通过后生成一个Token与该用户进行关联，并将Token返回给客户端。
     * 客户端在接下来的请求中都会携带Token，服务端通过解析Token检查登录状态。
     * 当用户退出登录、其他终端登录同一账号（被顶号）、长时间未进行操作时Token会失效，这时用户需要重新登录。ip变化是否重新登录;
     * <p>
     * 登录请求一定要使用HTTPS，否则无论Token做的安全性多好密码泄露了也是白搭
     * Token的生成方式有很多种，例如比较热门的有JWT（JSON Web Tokens）、OAuth等。
     */
    @Autowired
    UserService userService;


    @Autowired
    private RedisCacheUtil redisCacheUtil;

    /**
     * login
     *
     * @param userBean
     * @return
     */
    @RequestMapping(value = "login", method = RequestMethod.POST)
    public @ResponseBody
    String login(@RequestBody UserBean userBean) {
        String userName = userBean.getUsername();
        BaseResponseJson baseResponseJson = new BaseResponseJson();

        if (userService.findUserByName(userName) != null) {//  is register
            String passwrod = userBean.getPassword();
            //success return boken
            List<UserBean> userBeans = userService.queryUserByUserNameAndPasswrod(userName, passwrod);
            if (userBeans.size() > 0) {
                redisCacheUtil.createToken(userBeans.get(0).getId());

            } else {

            }
        } else {  //faile


        }
        return new Gson().toJson(baseResponseJson);
    }

    /**
     * logout
     *
     * @param currentUser
     * @return
     */
    @RequestMapping(value = "logout", method = RequestMethod.POST)
    public @ResponseBody
    String logout(UserBean currentUser) {
        redisCacheUtil.deleteToken(currentUser.getId());
        return new Gson().toJson(new BaseResponseJson().setReturnCode(0));
    }

    /**
     * register
     *
     * @param currentUser
     * @return
     */
    @RequestMapping(value = "register", method = RequestMethod.POST)
    public @ResponseBody
    String register(UserBean currentUser) {

        return "";
    }

    /**
     * getBackPasswrod
     *
     * @param currentUser
     * @return
     */
    @RequestMapping(value = "getBackPasswrod", method = RequestMethod.POST)
    public @ResponseBody
    String getBackPasswrod(UserBean currentUser) {

        return "";
    }
}
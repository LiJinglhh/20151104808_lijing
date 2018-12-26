package com.controller;

import com.dao.UserMapper;
import com.model.User;
import com.utils.MD5Util;
import com.utils.RegexUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;
import java.security.NoSuchAlgorithmException;

@Controller
public class LoginController {

    @Autowired
    private UserMapper userMapper;

    /**
     * 跳转到登录页面方法
     *
     * @param session
     * @return
     */
    @RequestMapping("/login")
    public String toLoginView(HttpSession session) {

        return "/views/login";
    }

    /**
     * 登录方法
     *
     * @param user
     * @param model
     * @param session
     * @return
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(User user, Model model, HttpSession session) throws NoSuchAlgorithmException {
        //判断输入信息
        boolean flag = true;
        if (user.getUserName() == null || user.getUserName().length() == 0) {
            model.addAttribute("errUserNameMsg", "用户名不能为空");
            flag = false;
        } else if (!RegexUtil.regUserName(user.getUserName())) {
            model.addAttribute("errUserNameMsg", "用户名不符合规则");
            flag = false;
        }
        if (user.getPassword() == null || user.getPassword().length() == 0) {
            model.addAttribute("errPwdMsg", "密码不能为空");
            flag = false;
        } else if (!RegexUtil.regPassword(user.getPassword())) {
            model.addAttribute("errPwdMsg", "密码不符合规则");
            flag = false;
        }
        if (flag == false) {
            return "/views/login";
        }

        //查询数据库中的该username账号信息
        User daoUser = userMapper.selectByPrimaryKey(user.getUserName());

        //判断是否查找到结果，没查找到说明没有账号
        if (daoUser == null) {
            model.addAttribute("errMsg", "账号不存在");
            flag = false;
        }else if (!user.getPassword().equals(daoUser.getPassword())) {
            model.addAttribute("errMsg", "密码错误，请重新输入");
            flag = false;
        }else if(daoUser.getIsFreeze()==true){
            //被冻结用户
            model.addAttribute("errMsg", "登录失败，该用户已被冻结");
            flag = false;
        }
        if (flag == true) {
            //登录成功将用户信息放入session中
            session.setAttribute("user", daoUser);
            return "redirect:/index";
        } else {
            return "/views/login";
        }
    }

    @RequestMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("user");
        return "redirect:/index";
    }


} 
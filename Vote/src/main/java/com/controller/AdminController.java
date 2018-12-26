package com.controller;

import com.dao.*;
import com.model.*;
import com.sun.org.apache.xpath.internal.operations.Mod;
import com.utils.FileUploadUtil;
import com.utils.MD5Util;
import com.utils.RegexUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private AdminMapper adminMapper;
    @Autowired
    private TeacherMapper teacherMapper;

    /**
     * 跳转到登录页面
     *
     * @return
     */
    @RequestMapping("/login")
    public String toLoginView() {
        return "/views/admin/login";
    }

    /**
     * 登录方法
     *
     * @return
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(Admin admin, Model model, HttpSession session) throws NoSuchAlgorithmException {

        Admin daoAdmin = adminMapper.selectByPrimaryKey(admin.getUserName());
        if (daoAdmin == null) {
            model.addAttribute("errMsg", "用户名不存在！");
            return "/views/admin/login";
        }
        if (!daoAdmin.getPassword().equals(admin.getPassword())) {
            model.addAttribute("errMsg", "登录失败，密码错误！");
            return "/views/admin/login";
        }
        session.setAttribute("admin", daoAdmin);
        return "redirect:/admin/index";
    }

    @RequestMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("admin");
        return "/views/admin/login";
    }

    @RequestMapping("/index")
    public String index() {
        return "/views/admin/index";
    }

    /**
     * 跳转到添加管理员信息页面
     *
     * @param model
     * @return
     */
    @RequestMapping("/admins/add")
    public String admins_add(Model model) {
        return "/views/admin/admins_add";
    }


    /**
     * 添加管理员信息
     *
     * @param admin
     * @param repassword
     * @param model
     * @return
     */
    @RequestMapping(value = "/admins/add", method = RequestMethod.POST)
    public String register(Admin admin, String repassword, Model model) throws NoSuchAlgorithmException {
        //判断输入信息
        boolean flag = true;
        if (admin.getUserName() == null || admin.getUserName().length() == 0) {
            model.addAttribute("errUserNameMsg", "用户名不能为空");
            flag = false;
        } else if (!RegexUtil.regUserName(admin.getUserName())) {
            model.addAttribute("errUserNameMsg", "用户名不符合规则");
            flag = false;
        }

        if (admin.getPassword() == null || admin.getPassword().length() == 0) {
            model.addAttribute("errPwdMsg", "密码不能为空");
            flag = false;
        } else if (repassword == null || repassword.length() == 0) {
            model.addAttribute("errRePwdMsg", "确认密码不能为空");
            flag = false;
        } else if (!RegexUtil.regPassword(admin.getPassword())) {
            model.addAttribute("errPwdMsg", "密码不符合规则");
            flag = false;
        } else if (!admin.getPassword().equals(repassword)) {
            model.addAttribute("errMsg", "两次密码输入内容不同");
            flag = false;
        }
        if (repassword == null || repassword.length() == 0) {
            model.addAttribute("errRePwdMsg", "确认密码不能为空");
            flag = false;
        }
        if (flag == false) {
            return "/views/admin/admins_add";
        }
        //判断数据库中是否有该账号
        Admin daoAdmin = adminMapper.selectByPrimaryKey(admin.getUserName());
        if (daoAdmin != null) {
            model.addAttribute("errMsg", "添加管理员失败，该用户名已存在！");
            flag = false;
        } else {
            //使用MD5对密码加密
            admin.setPassword(admin.getPassword());
            //插入数据库
            int insert = adminMapper.insert(admin);
            if (insert < 1) {
                model.addAttribute("errMsg", "添加管理员失败，未知原因！");
                flag = false;
            }
        }
        if (flag == true) {
            return "/views/admin/admins_add_success";
        } else {
            return "/views/admin/admins_add";
        }
    }


    /**
     * 跳转到查看管理员信息页面
     *
     * @param model
     * @return
     */
    @RequestMapping("/admins")
    public String admins(Model model) {
        AdminExample example = new AdminExample();
        List<Admin> admins = adminMapper.selectByExample(example);
        model.addAttribute("list", admins);
        return "/views/admin/admins";
    }

    /**
     * 删除管理员信息
     *
     * @param
     * @return
     */
    @RequestMapping("/admins/delete")
    public String delete_admin(String userName) {
        //空值处理
        if (userName == null || userName.length() == 0) {
            return "redirect:/admin/admins";
        }
        Admin admin = adminMapper.selectByPrimaryKey(userName);
        if (admin == null) {
            return "redirect:/admin/admins";
        }
        adminMapper.deleteByPrimaryKey(admin.getUserName());
        return "redirect:/admin/admins";
    }

    /**
     * 跳转到查看业务管理人员信息页面
     *
     * @param model
     * @return
     */
    @RequestMapping("/teachers")
    public String teachers(Model model) {
        TeacherExample example = new TeacherExample();
        List<Teacher> teachers = teacherMapper.selectByExample(example);
        model.addAttribute("list", teachers);
        return "/views/admin/teachers";
    }

    /**
     * 跳转到添加管理员信息页面
     *
     * @param model
     * @return
     */
    @RequestMapping("/teachers/add")
    public String teachers_add(Model model) {
        return "/views/admin/teachers_add";
    }

    @RequestMapping(value = "/teachers/add", method = RequestMethod.POST)
    public String register(Teacher teacher, String repassword, Model model) throws NoSuchAlgorithmException {
        //判断输入信息
        boolean flag = true;
        if (teacher.getUserName() == null || teacher.getUserName().length() == 0) {
            model.addAttribute("errUserNameMsg", "用户名不能为空");
            flag = false;
        } else if (!RegexUtil.regUserName(teacher.getUserName())) {
            model.addAttribute("errUserNameMsg", "用户名不符合规则");
            flag = false;
        }
        if (teacher.getRealName() == null || teacher.getRealName().length() == 0) {
            model.addAttribute("errRealNameMsg", "真实姓名不能为空");
            flag = false;
        } else if (!RegexUtil.regChinese(teacher.getRealName())) {
            model.addAttribute("errRealNameMsg", "真实姓名不符合规则");
            flag = false;
        }
        if (teacher.getPassword() == null || teacher.getPassword().length() == 0) {
            model.addAttribute("errPwdMsg", "密码不能为空");
            flag = false;
        } else if (repassword == null || repassword.length() == 0) {
            model.addAttribute("errRePwdMsg", "确认密码不能为空");
            flag = false;
        } else if (!RegexUtil.regPassword(teacher.getPassword())) {
            model.addAttribute("errPwdMsg", "密码不符合规则");
            flag = false;
        } else if (!teacher.getPassword().equals(repassword)) {
            model.addAttribute("errMsg", "两次密码输入内容不同");
            flag = false;
        }
        if (repassword == null || repassword.length() == 0) {
            model.addAttribute("errRePwdMsg", "确认密码不能为空");
            flag = false;
        }
        if (teacher.getMobilePhone() == null || teacher.getMobilePhone().length() == 0) {
            model.addAttribute("errPhoneMsg", "手机号不能为空");
            flag = false;
        } else if (!RegexUtil.regPhone(teacher.getMobilePhone())) {
            model.addAttribute("errPhoneMsg", "手机号不符合规则");
            flag = false;
        }
        if (teacher.getEmail() == null || teacher.getEmail().length() == 0) {
            model.addAttribute("errEmailMsg", "邮箱信息不能为空");
            flag = false;
        } else if (!RegexUtil.regEmail(teacher.getEmail())) {
            model.addAttribute("errEmailMsg", "邮箱信息不符合规则");
            flag = false;
        }
        if (flag == false) {
            return "/views/admin/teachers_add";
        }
        //判断数据库中是否有该账号
        Teacher daoTeacher = teacherMapper.selectByPrimaryKey(teacher.getUserName());
        if (daoTeacher != null) {
            model.addAttribute("errMsg", "添加失败，该用户名已存在！");
            flag = false;
        } else {
            //使用MD5对密码加密
            teacher.setPassword(teacher.getPassword());
            //插入数据库
            int insert = teacherMapper.insert(teacher);
            if (insert < 1) {
                model.addAttribute("errMsg", "添加失败，未知原因！");
                flag = false;
            }
        }
        if (flag == true) {
            return "/views/admin/teachers_add_success";
        } else {
            return "/views/admin/teachers_add";
        }
    }


    /**
     * 删除管理员信息
     *
     * @param
     * @return
     */
    @RequestMapping("/teachers/delete")
    public String delete_teachers(String userName) {
        //空值处理
        if (userName == null || userName.length() == 0) {
            return "redirect:/admin/teachers";
        }
        Teacher teacher = teacherMapper.selectByPrimaryKey(userName);
        if (teacher == null) {
            return "redirect:/admin/teachers";
        }
        teacherMapper.deleteByPrimaryKey(teacher.getUserName());
        return "redirect:/admin/teachers";
    }
}
package com.controller;

import com.dao.*;
import com.model.*;
import com.utils.MD5Util;
import com.utils.RegexUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Controller
public class UserController {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private VoteBatchMapper voteBatchMapper;
    @Autowired
    private VoteQuestionMapper voteQuestionMapper;
    @Autowired
    private VoteMessageMapper voteMessageMapper;

    /**
     * 跳转到注册页面
     *
     * @return
     */
    @RequestMapping("/register")
    public String toRegisterView() {
        return "/views/register";
    }

    /**
     * 注册方法
     *
     * @param user
     * @param repassword
     * @param model
     * @return
     */
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String register(User user, String repassword, Model model) throws NoSuchAlgorithmException {
        //判断输入信息
        boolean flag = true;
        if (user.getUserName() == null || user.getUserName().length() == 0) {
            model.addAttribute("errUserNameMsg", "用户名不能为空");
            flag = false;
        } else if (!RegexUtil.regUserName(user.getUserName())) {
            model.addAttribute("errUserNameMsg", "用户名不符合规则");
            flag = false;
        }
        if (user.getRealName() == null || user.getRealName().length() == 0) {
            model.addAttribute("errRealNameMsg", "真实姓名不能为空");
            flag = false;
        } else if (!RegexUtil.regChinese(user.getRealName())) {
            model.addAttribute("errRealNameMsg", "真实姓名不符合规则");
            flag = false;
        }
        if (user.getPassword() == null || user.getPassword().length() == 0) {
            model.addAttribute("errPwdMsg", "密码不能为空");
            flag = false;
        } else if (repassword == null || repassword.length() == 0) {
            model.addAttribute("errRePwdMsg", "确认密码不能为空");
            flag = false;
        } else if (!RegexUtil.regPassword(user.getPassword())) {
            model.addAttribute("errPwdMsg", "密码不符合规则");
            flag = false;
        } else if (!user.getPassword().equals(repassword)) {
            model.addAttribute("errMsg", "两次密码输入内容不同");
            flag = false;
        }
        if (repassword == null || repassword.length() == 0) {
            model.addAttribute("errRePwdMsg", "确认密码不能为空");
            flag = false;
        }
        if (user.getIdentityCard() == null || user.getIdentityCard().length() == 0) {
            model.addAttribute("errIdCardMsg", "身份证号不能为空");
            flag = false;
        } else if (!RegexUtil.regIdCard(user.getIdentityCard())) {
            model.addAttribute("errIdCardMsg", "身份证号不符合规则");
            flag = false;
        }
        if (user.getMobilePhone() == null || user.getMobilePhone().length() == 0) {
            model.addAttribute("errPhoneMsg", "手机号不能为空");
            flag = false;
        } else if (!RegexUtil.regPhone(user.getMobilePhone())) {
            model.addAttribute("errPhoneMsg", "手机号不符合规则");
            flag = false;
        }
        if (user.getEmail() == null || user.getEmail().length() == 0) {
            model.addAttribute("errEmailMsg", "邮箱信息不能为空");
            flag = false;
        } else if (!RegexUtil.regEmail(user.getEmail())) {
            model.addAttribute("errEmailMsg", "邮箱信息不符合规则");
            flag = false;
        }
        if (user.getkindGroup() == null || user.getkindGroup().length() == 0){
            model.addAttribute("errGroupMsg", "分类信息不能为空");
            flag = false;
        }
        if (flag == false) {
            return "/views/register";
        }
        //判断数据库中是否有该账号
        User daoUser = userMapper.selectByPrimaryKey(user.getUserName());
        if (daoUser != null) {
            model.addAttribute("errMsg", "注册失败，该用户名已存在！");
            flag = false;
        } else {
            //使用MD5对密码加密
            user.setPassword(user.getPassword());
            user.setIsFreeze(false);
            //插入数据库
            int insert = userMapper.insert(user);
            if (insert < 1) {
                model.addAttribute("errMsg", "注册失败，未知原因！");
                flag = false;
            }
        }
        if (flag == true) {
            return "/views/register_success";
        } else {
            return "/views/register";
        }
    }

    /**
     * 跳转到个人信息页面
     *
     * @return
     */
    @RequestMapping("/personal")
    public String personal(Model model,HttpSession session) {
        if (session.getAttribute("user") == null) {
            return "/views/login";
        }
        User sessionUser = (User)session.getAttribute("user");
        User user = userMapper.selectByPrimaryKey(sessionUser.getUserName());
        String group = user.getkindGroup();

        model.addAttribute("group",group);
        return "/views/personal";
    }

    /**
     * 跳转到修改个人信息页面
     *
     * @return
     */
    @RequestMapping("/personal/update")
    public String to_update_personal(Model model,HttpSession session) {
        User sessionUser = (User)session.getAttribute("user");
        User user = userMapper.selectByPrimaryKey(sessionUser.getUserName());
        String group = user.getkindGroup();
        model.addAttribute("group",group);
        if (session.getAttribute("user") == null) {
            return "/views/login";
        }
        return "/views/personal_update";
    }

    /**
     * 修改个人信息
     *
     * @return
     */
    @RequestMapping(value = "/personal/update", method = RequestMethod.POST)
    public String do_update_personal(User user, Model model, HttpSession session) {
        if (session.getAttribute("user") == null) {
            return "/views/login";
        }
        //判断输入信息
        boolean flag = true;
        if (user.getRealName() == null || user.getRealName().length() == 0) {
            model.addAttribute("errRealNameMsg", "真实姓名不能为空");
            flag = false;
        } else if (!RegexUtil.regChinese(user.getRealName())) {
            model.addAttribute("errRealNameMsg", "真实姓名不符合规则");
            flag = false;
        }
        if (user.getIdentityCard() == null || user.getIdentityCard().length() == 0) {
            model.addAttribute("errIdCardMsg", "身份证号不能为空");
            flag = false;
        } else if (!RegexUtil.regIdCard(user.getIdentityCard())) {
            model.addAttribute("errIdCardMsg", "身份证号不符合规则");
            flag = false;
        }
        if (user.getMobilePhone() == null || user.getMobilePhone().length() == 0) {
            model.addAttribute("errPhoneMsg", "手机号不能为空");
            flag = false;
        } else if (!RegexUtil.regPhone(user.getMobilePhone())) {
            model.addAttribute("errPhoneMsg", "手机号不符合规则");
            flag = false;
        }
        if (user.getEmail() == null || user.getEmail().length() == 0) {
            model.addAttribute("errEmailMsg", "邮箱信息不能为空");
            flag = false;
        } else if (!RegexUtil.regEmail(user.getEmail())) {
            model.addAttribute("errEmailMsg", "邮箱信息不符合规则");
            flag = false;
        }
        if (flag == false) {
            String group = user.getkindGroup();
            model.addAttribute("group",group);
            return "/views/personal_update";
        }
        //获取当前登录的用户信息
        User sessionUser = (User) session.getAttribute("user");
        //更新信息但未更新到session和数据库中
        sessionUser.setRealName(user.getRealName());
        sessionUser.setIdentityCard(user.getIdentityCard());
        sessionUser.setMobilePhone(user.getMobilePhone());
        sessionUser.setEmail(user.getEmail());

        int count = userMapper.updateByPrimaryKeySelective(sessionUser);

        if (count > 0) {
            //更新成功，将信息更新到session中
            session.setAttribute("user", sessionUser);
        } else {
            model.addAttribute("errMsg", "更新失败，未知原因，请联系系统管理员！");
            flag = false;
        }
        if (flag == true) {
            return "/views/personal_update_success";
        } else {
            String group = user.getkindGroup();
            model.addAttribute("group",group);
            return "/views/personal_update";
        }
    }

    /**
     * 跳转到修改密码页面
     *
     * @return
     */
    @RequestMapping("/password/update")
    public String to_update_password(HttpSession session) {
        if (session.getAttribute("user") == null) {
            return "/views/login";
        }
        return "/views/password_update";
    }

    /**
     * 修改密码
     *
     * @return
     */
    @RequestMapping(value = "/password/update", method = RequestMethod.POST)
    public String do_update_password(String oldpassword, String newpassword, String repassword, Model model, HttpSession session) throws NoSuchAlgorithmException {
        if (session.getAttribute("user") == null) {
            return "/views/login";
        }
        //判断输入信息
        boolean flag = true;

        if (oldpassword == null || oldpassword.length() == 0) {
            model.addAttribute("errOldPwdMsg", "旧密码不能为空");
            flag = false;
        } else if (newpassword == null || newpassword.length() == 0) {
            model.addAttribute("errNewPwdMsg", "新密码不能为空");
            flag = false;
        } else if (repassword == null || repassword.length() == 0) {
            model.addAttribute("errRePwdMsg", "确认密码不能为空");
            flag = false;
        } else if (!newpassword.equals(repassword)) {
            model.addAttribute("errMsg", "两次密码输入内容不同");
            flag = false;
        } else if (!RegexUtil.regPassword(newpassword)) {
            model.addAttribute("errNewPwdMsg", "新密码不符合规则");
            flag = false;
        }

        if (flag == false) {
            return "/views/password_update";
        }
        //获取当前登录的用户信息
        User sessionUser = (User) session.getAttribute("user");

        User user = userMapper.selectByPrimaryKey(sessionUser.getUserName());

        if (!oldpassword.equals(user.getPassword())) {
            model.addAttribute("errMsg", "旧密码不正确");
            return "/views/password_update";
        }
        //更新session信息
        sessionUser.setPassword(newpassword);

        int count = userMapper.updateByPrimaryKeySelective(sessionUser);

        if (count > 0) {
            //更新成功，将信息更新到session中
            session.setAttribute("user", sessionUser);
        } else {
            model.addAttribute("errMsg", "更新失败，未知原因，请联系系统管理员！");
            flag = false;
        }
        if (flag == true) {
            return "/views/password_update_success";
        } else {
            return "/views/password_update";
        }
    }

    /**
     * 签到
     *
     * @return
     */
    @RequestMapping(value = "/sign")
    public String sign(Model model, HttpSession session) {
        if (session.getAttribute("user") == null) {
            return "/views/login";
        }
        //判断输入信息
        boolean flag = true;
        //获取当前登录的用户信息
        User sessionUser = (User) session.getAttribute("user");

        User user = userMapper.selectByPrimaryKey(sessionUser.getUserName());

        if (user.getSignTime() != null) {

            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date());
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            Date today = cal.getTime();
            if (user.getSignTime().after(today)) {
                return "/views/sign_error";
            }
        }

        user.setSignTime(new Date());
        if (user.getSignNumber() == null){
            user.setSignNumber(0);
        }
        user.setSignNumber(user.getSignNumber() + 1);


        int count = userMapper.updateByPrimaryKeySelective(user);

        if (count > 0) {
            //更新成功，将信息更新到session中
            session.setAttribute("user", user);
        } else {
            model.addAttribute("errMsg", "更新失败，未知原因，请联系系统管理员！");
            flag = false;
        }
        if (flag == true) {
            return "/views/sign_success";
        } else {
            return "/views/sign_error";
        }
    }

    /**
     * 展示投票信息
     *
     * @param model
     * @return
     */
    @RequestMapping("/votedatas")
    public String votedatas(Model model, HttpSession session) {
        if (session.getAttribute("user") == null) {
            return "/views/login";
        }
        String errorData = "";
        VoteBatchExample example = new VoteBatchExample();
        List<VoteBatch> voteBatch = voteBatchMapper.selectByExample(example);
        User sessionUser = (User) session.getAttribute("user");
        model.addAttribute("list", voteBatch);
        model.addAttribute("errorData", errorData);
        return "/views/votedatas";
    }

    /**
     * 搜索投票信息
     *
     * @param model
     * @return
     */
    @RequestMapping("/votedatas/search")
    public String votedatas(String batchName, Model model, HttpSession session) {
        if (session.getAttribute("user") == null) {
            return "/views/login";
        }
        List<VoteBatch> voteBatchList = voteBatchMapper.selectLikeBatchName(batchName);
        String errorData = "";
        model.addAttribute("list", voteBatchList);
        model.addAttribute("errorData", errorData);
        return "/views/votedatas";
    }

    /**
     * 跳转至投票页面
     *
     * @param
     * @return
     */
    @RequestMapping("/votedatas/joinvote")
    public String joinvote(Integer batchId,HttpSession session,Model model){
        if (session.getAttribute("user") == null) {
            return "/views/login";
        }
        User sessionUser = (User) session.getAttribute("user");
        boolean flag = true;
        String errorData = "";
        VoteBatch voteBatch = voteBatchMapper.selectByPrimaryKey(batchId);
        VoteBatchExample example = new VoteBatchExample();
        List<VoteBatch> voteBatchList = voteBatchMapper.selectByExample(example);
        List<VoteQuestion> voteQuestion = voteQuestionMapper.selectByBatchId(batchId);
        //查看该用户是否已经参与过投票
        VoteMessage question = voteMessageMapper.select(sessionUser.getUserName(), batchId);
        if(question != null){
            errorData = "您已参与过该项目的投票。";
            flag = false;
        }
        if (!voteBatch.getBatchGroup().contains(sessionUser.getkindGroup())){
            errorData = "您所在的用户分类暂无权限参与该项目的投票。";
            flag = false;
        }
        if (voteBatch.getEndTime().before(new Date())){
            errorData = "该项目的投票已经截止。";
            flag = false;
        }
        model.addAttribute("voteBatch", voteBatch);
        model.addAttribute("voteQuestion", voteQuestion);
        model.addAttribute("errorData", errorData);
        model.addAttribute("list", voteBatchList);
        if (!flag){
            return "/views/votedatas";
        }
        return "/views/joinvote";
    }

    /**
     * 跳转至投票统计页面
     *
     * @param
     * @return
     */
    @RequestMapping("/votedatas/lookvote")
    public String lookvote(Integer batchId,HttpSession session,Model model){
        if (session.getAttribute("user") == null) {
            return "/views/login";
        }
        VoteBatch voteBatch = voteBatchMapper.selectByPrimaryKey(batchId);
        List<VoteQuestion> voteQuestion = voteQuestionMapper.selectByBatchId(batchId);

        model.addAttribute("voteBatch", voteBatch);
        model.addAttribute("voteQuestion", voteQuestion);
        return "/views/lookvote";
    }

    /**
     * 提交投票信息
     *
     * @param
     * @return
     */
    @RequestMapping("/votedatas/updatevote")
    public String updatevote(String batchId,VoteQuestions voteQuestions,HttpSession session){
        if (session.getAttribute("user") == null) {
            return "/views/login";
        }
        User sessionUser = (User) session.getAttribute("user");
        //更新问题表的选项参与人数
        updateCheckNumber(voteQuestions.getQuestion1());
        updateCheckNumber(voteQuestions.getQuestion2());
        updateCheckNumber(voteQuestions.getQuestion3());
        updateCheckNumber(voteQuestions.getQuestion4());
        updateCheckNumber(voteQuestions.getQuestion5());
        //更新项目表的参与人数
        VoteBatch voteBatch = voteBatchMapper.selectByPrimaryKey(Integer.parseInt(batchId));
        voteBatch.setUserNum(voteBatch.getUserNum() + 1);
        voteBatchMapper.updateByPrimaryKey(voteBatch);
        //向投票信息表里插入用户投票记录
        VoteMessage voteMessage = new VoteMessage();
        voteMessage.setBatchId(Integer.parseInt(batchId));
        voteMessage.setUserName(sessionUser.getUserName());
        voteMessageMapper.insert(voteMessage);
        return "/views/joinvote_success";
    }

    public void updateCheckNumber(String question){
        if (StringUtils.isNotEmpty(question)){
            String questionId = question.substring(0,question.length() - 1);
            String check = question.substring(question.length() - 1);
            VoteQuestion voteQuestion = voteQuestionMapper.selectByQuestionId(Integer.parseInt(questionId));
            if ("A".equals(check)){
                voteQuestion.setNumberA(voteQuestion.getNumberA()+1);
            }
            if ("B".equals(check)){
                voteQuestion.setNumberB(voteQuestion.getNumberB()+1);
            }
            if ("C".equals(check)){
                voteQuestion.setNumberC(voteQuestion.getNumberC()+1);
            }
            if ("D".equals(check)){
                voteQuestion.setNumberD(voteQuestion.getNumberD()+1);
            }
            voteQuestionMapper.updateCheckNumber(voteQuestion);
        }else {
            return;
        }
    }
} 
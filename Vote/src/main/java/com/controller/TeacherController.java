package com.controller;

import com.dao.*;
import com.model.*;
import com.utils.MD5Util;
import com.utils.MailUtil;
import com.utils.RegexUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/teacher")
public class TeacherController {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private TeacherMapper teacherMapper;
    @Autowired
    private NoticeMapper noticeMapper;
    @Autowired
    private VoteBatchMapper voteBatchMapper;
    @Autowired
    private VoteQuestionMapper voteQuestionMapper;

    /**
     * 跳转到登录页面
     *
     * @return
     */
    @RequestMapping("/login")
    public String toLoginView() {
        return "/views/teacher/login";
    }

    /**
     * 登录方法
     *
     * @return
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(Teacher teacher, Model model, HttpSession session) throws NoSuchAlgorithmException {

        Teacher daoTeacher = teacherMapper.selectByPrimaryKey(teacher.getUserName());
        if (daoTeacher == null) {
            model.addAttribute("errMsg", "业务管理人员用户名不存在！");
            return "/views/teacher/login";
        }
        if (!daoTeacher.getPassword().equals(teacher.getPassword())) {
            model.addAttribute("errMsg", "登录失败，密码错误！");
            return "/views/teacher/login";
        }
        session.setAttribute("teacher", daoTeacher);
        return "redirect:/teacher/index";
    }

    @RequestMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("teacher");
        return "/views/teacher/login";
    }

    @RequestMapping("/index")
    public String index() {
        return "/views/teacher/index";
    }

    /**
     * 展示用户信息
     *
     * @param model
     * @return
     */
    @RequestMapping("/users")
    public String users(Model model) {
        UserExample example = new UserExample();
        List<User> users = userMapper.selectByExample(example);
        for (int i = 0 ; i < users.size() ; i++){
            if (users.get(i).getSignNumber()==null){
                users.get(i).setSignNumber(new Integer(0));
            }
        }
        model.addAttribute("list", users);
        return "/views/teacher/users";
    }

    /**
     * 冻结
     *
     * @param userName
     * @return
     */
    @RequestMapping("/users/freeze")
    public String freeze(String userName) {
        //空值处理
        if (userName == null || userName.length() == 0) {
            return "redirect:/teacher/users";
        }
        User user = userMapper.selectByPrimaryKey(userName);
        if (user == null) {
            return "redirect:/teacher/users";
        }
        user.setIsFreeze(true);

        userMapper.updateByPrimaryKey(user);
        return "redirect:/teacher/users";
    }

    /**
     * 解冻
     *
     * @param userName
     * @return
     */
    @RequestMapping("/users/unfreeze")
    public String unfreeze(String userName) {
        //空值处理
        if (userName == null || userName.length() == 0) {
            return "redirect:/teacher/users";
        }
        User user = userMapper.selectByPrimaryKey(userName);
        if (user == null) {
            return "redirect:/teacher/users";
        }
        user.setIsFreeze(false);

        userMapper.updateByPrimaryKey(user);
        return "redirect:/teacher/users";
    }

    /**
     * 删除用户信息
     *
     * @param
     * @return
     */
    @RequestMapping("/users/delete")
    public String delete_users(String userName) {
        //空值处理
        if (userName == null || userName.length() == 0) {
            return "redirect:/teacher/users";
        }
        User user = userMapper.selectByPrimaryKey(userName);
        if (user == null) {
            return "redirect:/teacher/users";
        }
        userMapper.deleteByPrimaryKey(user.getUserName());
        return "redirect:/teacher/users";
    }

    /**
     * 跳转到个人信息页面
     *
     * @return
     */
    @RequestMapping("/personal")
    public String personal(HttpSession session) {
        return "/views/teacher/personal";
    }

    /**
     * 跳转到修改个人信息页面
     *
     * @return
     */
    @RequestMapping("/personal/update")
    public String to_update_personal() {
        return "/views/teacher/personal_update";
    }

    /**
     * 修改个人信息
     *
     * @return
     */
    @RequestMapping(value = "/personal/update", method = RequestMethod.POST)
    public String do_update_personal(Teacher teacher, Model model, HttpSession session) {
        //判断输入信息
        boolean flag = true;
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
            return "/views/teacher/personal_update";
        }
        //获取当前登录的用户信息
        Teacher sessionTeacher = (Teacher) session.getAttribute("teacher");
        //更新信息但未更新到session和数据库中
        sessionTeacher.setMobilePhone(teacher.getMobilePhone());
        sessionTeacher.setEmail(teacher.getEmail());

        int count = teacherMapper.updateByPrimaryKeySelective(sessionTeacher);

        if (count > 0) {
            //更新成功，将信息更新到session中
            session.setAttribute("teacher", sessionTeacher);
        } else {
            model.addAttribute("errMsg", "更新失败，未知原因，请联系系统管理员！");
            flag = false;
        }
        if (flag == true) {
            return "/views/teacher/personal_update_success";
        } else {
            return "/views/teacher/personal_update";
        }
    }

    /**
     * 跳转到修改密码页面
     *
     * @return
     */
    @RequestMapping("/password/update")
    public String to_update_password(HttpSession session) {
        return "/views/teacher/password_update";
    }

    /**
     * 修改密码
     *
     * @return
     */
    @RequestMapping(value = "/password/update", method = RequestMethod.POST)
    public String do_update_password(String oldpassword, String newpassword, String repassword, Model model, HttpSession session) throws NoSuchAlgorithmException {
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
            return "/views/teacher/password_update";
        }
        //获取当前登录的用户信息
        Teacher sessionTeacher = (Teacher) session.getAttribute("teacher");

        Teacher teacher = teacherMapper.selectByPrimaryKey(sessionTeacher.getUserName());

        if (!oldpassword.equals(teacher.getPassword())) {
            model.addAttribute("errMsg", "旧密码不正确");
            return "/views/teacher/password_update";
        }
        //更新session信息
        sessionTeacher.setPassword(newpassword);

        int count = teacherMapper.updateByPrimaryKeySelective(sessionTeacher);

        if (count > 0) {
            //更新成功，将信息更新到session中
            session.setAttribute("teacher", sessionTeacher);
        } else {
            model.addAttribute("errMsg", "更新失败，未知原因，请联系系统管理员！");
            flag = false;
        }
        if (flag == true) {
            return "/views/teacher/password_update_success";
        } else {
            return "/views/teacher/password_update";
        }
    }

    /**
     * 跳转到添加公告信息页面
     *
     * @param model
     * @return
     */
    @RequestMapping("/notices/add")
    public String notices_add(Model model) {
        return "/views/teacher/notices_add";
    }


    /**
     * 添加公告信息
     *
     * @param notice
     * @param model
     * @return
     */
    @RequestMapping(value = "/notices/add", method = RequestMethod.POST)
    public String register(Notice notice, Model model) throws NoSuchAlgorithmException {
        //判断输入信息
        boolean flag = true;
        if (notice.getTitle() == null || notice.getTitle().length() == 0) {
            model.addAttribute("errTitleMsg", "公告标题不能为空");
            flag = false;
        }
        if (flag == false) {
            return "/views/teacher/notices_add";
        }
        //插入数据库
        int insert = noticeMapper.insert(notice);
        if (insert < 1) {
            model.addAttribute("errMsg", "添加公告失败，未知原因！");
            flag = false;
        }
        if (flag == true) {
            return "/views/teacher/notices_add_success";
        } else {
            return "/views/teacher/notices_add";
        }
    }

    /**
     * 展示公告信息
     *
     * @param model
     * @return
     */
    @RequestMapping("/notices")
    public String notices(Model model) {
        NoticeExample example = new NoticeExample();
        List<Notice> notices = noticeMapper.selectByExample(example);
        model.addAttribute("list", notices);
        return "/views/teacher/notices";
    }

    /**
     * 删除公告信息
     *
     * @param
     * @return
     */
    @RequestMapping("/notices/delete")
    public String delete_notices(Integer id) {
        noticeMapper.deleteByPrimaryKey(id);
        return "redirect:/teacher/notices";
    }

    /**
     * 跳转至修改公告页面
     * * @return
     */
    @RequestMapping("/notices/update")
    public String toUpdateNotice(Integer id, Model model) {

        Notice notice = noticeMapper.selectByPrimaryKey(id);

        model.addAttribute("notice", notice);

        return "/views/teacher/notices_update";
    }

    /**
     * 修改公告信息
     *
     * @param notice
     * @param model
     * @return
     */
    @RequestMapping(value = "/notices/update", method = RequestMethod.POST)
    public String notices_update(Notice notice, Model model) throws NoSuchAlgorithmException {
        //判断输入信息
        boolean flag = true;
        if (notice.getTitle() == null || notice.getTitle().length() == 0) {
            model.addAttribute("errTitleMsg", "公告标题不能为空");
            flag = false;
        }
        if (flag == false) {
            return "/views/teacher/notices_update";
        }
        Notice daoNotice = noticeMapper.selectByPrimaryKey(notice.getId());
        daoNotice.setTitle(notice.getTitle());
        daoNotice.setContent(notice.getContent());
        int count = noticeMapper.updateByPrimaryKeySelective(daoNotice);

        if (count > 0) {
            return "/views/teacher/notices_update_success";
        } else {
            return "/views/teacher/notices_update";
        }
    }
    /**
     * 搜索投票信息
     *
     * @param model
     * @return
     */
    @RequestMapping("/votedatas/search")
    public String votedatas(String batchName, Model model, HttpSession session) {
        List<VoteBatch> voteBatchList = voteBatchMapper.selectLikeBatchName(batchName);
        model.addAttribute("list", voteBatchList);
        return "/views/teacher/votedatas";
    }

    /**
     * 跳转到发布投票信息页面
     *
     * @param model
     * @return
     */
    @RequestMapping("/votedatas/add")
    public String votedatas_add(Model model,String send) {
        int questionNum = 1;
        if (send != null){
            questionNum = Integer.valueOf(send);
        }
        model.addAttribute("questionNum",questionNum);
        return "/views/teacher/votedatas_add";
    }


    /**
     * 发布投票信息
     *
     * @param endTimeString
     * @param voteBatch
     * @param voteQuestions
     * @param model
     * @return
     */
    @RequestMapping(value = "/votedatas/add", method = RequestMethod.POST)
    public String productsAdd(String endTimeString,VoteBatch voteBatch,VoteQuestions voteQuestions,Model model, HttpServletRequest request) {
        //判断输入信息
        boolean flag = true;
        String errTitleMsg = "";
        String errendTimeMsg = "";
        String errGroupMsg = "";
        String errQuestionNameMsg = "";
        if (voteBatch.getBatchName() == null || voteBatch.getBatchName().length() == 0) {
            errTitleMsg = "标题不能为空";
            flag = false;
        }
        VoteBatch voteBatchByName = null;
        voteBatchByName = voteBatchMapper.selectByBatchName(voteBatch.getBatchName());
        if (voteBatchByName!=null){
            errTitleMsg = "该项目名称已发起过投票";
            flag = false;
        }
        if (endTimeString == null || endTimeString.length() == 0) {
            errendTimeMsg = "请选择投票截止日期";
            flag = false;
        }else {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date endTime = new Date();
            try {
                endTime = sdf.parse(endTimeString);
                voteBatch.setEndTime(endTime);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        if (voteBatch.getBatchGroup() == null || voteBatch.getBatchGroup().length() == 0) {
            errGroupMsg = "请至少选择一种用户类型";
            flag = false;
        }
        if (stringEmpty(voteQuestions.getQuestionNames()) ||
            stringEmpty(voteQuestions.getQuestionAs()) ||
            stringEmpty(voteQuestions.getQuestionBs()) ||
            stringEmpty(voteQuestions.getQuestionCs())||
            stringEmpty(voteQuestions.getQuestionDs())) {
            errQuestionNameMsg = "问题名称或选项不能为空";
            flag = false;
        }
        if (flag == false) {
            int questionNum = 1;
            if (StringUtils.isNotEmpty(String.valueOf(voteBatch.getQuestionNum()))){
                questionNum = voteBatch.getQuestionNum();
            }
            model.addAttribute("questionNum",questionNum);
            model.addAttribute("errTitleMsg", errTitleMsg);
            model.addAttribute("errendTimeMsg", errendTimeMsg);
            model.addAttribute("errGroupMsg", errGroupMsg);
            model.addAttribute("errQuestionNameMsg", errQuestionNameMsg);
            return "/views/teacher/votedatas_add";
        }
        int insert = voteBatchMapper.insert(voteBatch);
        voteBatchByName = voteBatchMapper.selectByBatchName(voteBatch.getBatchName());
        for (int i = 0 ; i < voteBatch.getQuestionNum() ; i++){
            VoteQuestion voteQuestion = new VoteQuestion();
            voteQuestion.setQuestionBatchId(voteBatchByName.getBatchId());
            voteQuestion.setQuestionName(voteQuestions.getQuestionNames()[i]);
            voteQuestion.setQuestionA(voteQuestions.getQuestionAs()[i]);
            voteQuestion.setQuestionB(voteQuestions.getQuestionBs()[i]);
            voteQuestion.setQuestionC(voteQuestions.getQuestionCs()[i]);
            voteQuestion.setQuestionD(voteQuestions.getQuestionDs()[i]);
            voteQuestionMapper.insert(voteQuestion);
        }

        if (insert > 0) {
            return "/views/teacher/votedatas_add_success";
        } else {
            int questionNum = 1;
            if (StringUtils.isNotEmpty(String.valueOf(voteBatch.getQuestionNum()))){
                questionNum = voteBatch.getQuestionNum();
            }
            model.addAttribute("questionNum",questionNum);
            return "/views/teacher/votedatas_add";
        }
    }


    /**
     * 展示投票信息
     *
     * @param model
     * @return
     */
    @RequestMapping("/votedatas")
    public String votedatas(Model model) {
        VoteBatchExample example = new VoteBatchExample();
        List<VoteBatch> voteBatchList = voteBatchMapper.selectByExample(example);
        model.addAttribute("list", voteBatchList);
        return "/views/teacher/votedatas";
    }

    /**
     * 删除投票项目
     * @param batchId
     * @return
     */
    @RequestMapping(value = "/votedatas/delete")
    public String voteBatch_delete(Integer batchId) {
        voteBatchMapper.deleteByPrimaryKey(batchId);
        voteQuestionMapper.deleteByBatchId(batchId);
        return "redirect:/teacher/votedatas";
    }


    /**
     * 跳转至修改投票信息页面
     * * @return
     */
    @RequestMapping("/votedatas/update")
    public String toUpdatevotedatas(Integer id, Model model) {

        VoteBatch voteBatch = voteBatchMapper.selectByPrimaryKey(id);
        List<VoteQuestion> voteQuestion = voteQuestionMapper.selectByBatchId(id);

        model.addAttribute("voteBatch", voteBatch);
        model.addAttribute("voteQuestion", voteQuestion);

        return "/views/teacher/votedatas_update";
    }

    /**
     * 修改投票截止时间
     * @param endTimeString
     * @param voteBatch
     * @param model
     * @return
     */
    @RequestMapping(value = "/votedatas/update", method = RequestMethod.POST)
    public String votedatas_update(String endTimeString,VoteBatch voteBatch, Model model) throws NoSuchAlgorithmException {
        //判断输入信息
        boolean flag = true;
        String errTitleMsg = "";
        if (endTimeString == null || endTimeString.length()==0) {
            errTitleMsg = "修改时间不能为空";
            flag = false;
        }else {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date endTime = new Date();
            try {
                endTime = sdf.parse(endTimeString);
                voteBatch.setEndTime(endTime);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        if (flag == false) {
            model.addAttribute("errTitleMsg", errTitleMsg);
            return "/views/teacher/votedatas_update";
        }

        int count = voteBatchMapper.updateEndTimeByPrimaryKeySelective(voteBatch);

        if (count > 0) {
            return "/views/teacher/votedatas_update_success";
        } else {
            return "/views/teacher/votedatas_update";
        }
    }

    /**
     * 扣押押金
     * * @return
     */
    @RequestMapping(value = "/users/confiscate")
    public String confiscate(String userName) throws MessagingException {
        //空值处理
        if (userName == null || userName.length() == 0) {
            return "redirect:/teacher/users";
        }
        User user = userMapper.selectByPrimaryKey(userName);
        if (user == null) {
            return "redirect:/teacher/users";
        }
        userMapper.updateDeposit(user);
        return "redirect:/teacher/users";
    }

    /**
     * 跳转至发送邮件页面
     * * @return
     */
    @RequestMapping("/users/sendemail")
    public String toSendEmail(String userName, Model model) throws MessagingException {
        User user = userMapper.selectByPrimaryKey(userName);
        model.addAttribute("user", user);
        return "/views/teacher/sendemail";
    }

    /**
     * 发送邮件
     * * @return
     */
    @RequestMapping(value = "/users/sendemail", method = RequestMethod.POST)
    public String sendEmail(String email, String title, String content, Model model) throws MessagingException {
        MailUtil.sendMail(email, title, content);
        return "/views/teacher/sendemail_success";
    }

    public boolean stringEmpty(String[] chars){
        if (chars!=null && chars.length!=0){
            for (int i = 0; i < chars.length; i++){
                if ("".equals(chars[i])){
                    return true;
                }
            }
            return false;
        }
        return true;
    }

}
package com.controller;

import com.dao.NoticeMapper;
import com.model.Notice;
import com.model.NoticeExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class IndexController {
    @Autowired
    NoticeMapper noticeMapper;

    @RequestMapping("/")
    public String index(Model model, HttpSession session) {
        if (session.getAttribute("user") == null) {
            return "/views/login";
        }
        NoticeExample example = new NoticeExample();
        List<Notice> notices = noticeMapper.selectByExample(example);
        model.addAttribute("list", notices);
        return "/views/index";
    }

    @RequestMapping("/index")
    public String toIndex(Model model, HttpSession session) {
        if (session.getAttribute("user") == null) {
            return "/views/login";
        }
        NoticeExample example = new NoticeExample();
        List<Notice> notices = noticeMapper.selectByExample(example);
        model.addAttribute("list", notices);
        return "/views/index";
    }
}
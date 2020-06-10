package com.vibe.controller.information;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import com.vibe.pojo.user.User;
import com.vibe.pojo.infomation.Post;
import com.vibe.pojo.infomation.ShowTask;
import com.vibe.pojo.infomation.ShowTaskVo;
import com.vibe.service.information.PostService;
import com.vibe.service.information.ShowTaskService;
import com.vibe.utils.FormJson;
import com.vibe.utils.FormJsonBulider;
import com.vibe.utils.Page;

@Controller
public class ShowTaskController {
    @Autowired
    private ShowTaskService sts;

    @Autowired
    private PostService postService;

    @RequestMapping("/showtask/addPostAndTask")
    @ResponseBody
    public FormJson addPostAndTask(Vo vo) throws UnsupportedEncodingException {
        User u = (User) RequestContextHolder.getRequestAttributes().getAttribute("loginUser", RequestAttributes.SCOPE_SESSION);
        Post post = postService.savePost(vo.getPost().getTitle(), vo.getPost().getContent(), u.getId(), 0);
        if (post.getPid() == null) {
            return FormJsonBulider.fail(null);
        }
        String url = "/information/detail?pid=" + post.getPid() + "&title=" + URLEncoder.encode(post.getTitle(), "utf-8");
        vo.getTask().setUrl(url);
        sts.addTask(vo.getTask());
        return FormJsonBulider.success();
    }


    @RequestMapping("/showtask/add")
    @ResponseBody
    public String add(ShowTask a) {
        sts.addTask(a);
        return "SUCCESS";
    }

    @RequestMapping("/showtask/cancel")
    @ResponseBody
    public String cancel(Integer[] id) {
        sts.cancelTask(id);
        return "SUCCESS";
    }

    @RequestMapping("/showtask/update")
    @ResponseBody
    public String update(ShowTask a) {
        sts.updateTask(a);
        return "SUCCESS";
    }

    @RequestMapping("/showtask/list")
    @ResponseBody
    public Page<ShowTask> list(ShowTaskVo stv) {
        return sts.getTask(stv, stv.getPageNum(), stv.getPageSize());
    }

    @RequestMapping("/showtask/getCurrent")
    @ResponseBody
    public List<ShowTask> getCurrentTask(Integer equipment, HttpServletRequest request) {
        List<ShowTask> currentTask = sts.getCurrentTask(equipment);

        String basePath = "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
        currentTask.forEach(it -> {
            if (it.getUrl() != null && !it.getUrl().startsWith("http://")) {
                it.setUrl(basePath + it.getUrl());
            }
        });
        return currentTask;
    }

    @RequestMapping("/showtask/get")
    @ResponseBody
    public ShowTask get(Integer id) {
        return sts.get(id);
    }

    @InitBinder
    public void initBinder(ServletRequestDataBinder bin) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        CustomDateEditor cust = new CustomDateEditor(sdf, true);
        bin.registerCustomEditor(Date.class, cust);
    }

    public static class Vo {
        private ShowTask task;
        private Post post;

        public ShowTask getTask() {
            return task;
        }

        public void setTask(ShowTask task) {
            this.task = task;
        }

        public Post getPost() {
            return post;
        }

        public void setPost(Post post) {
            this.post = post;
        }
    }
}

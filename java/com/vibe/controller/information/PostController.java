package com.vibe.controller.information;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.vibe.pojo.user.User;
import com.vibe.pojo.infomation.Comment;
import com.vibe.pojo.infomation.Post;
import com.vibe.pojo.infomation.PostVo;
import com.vibe.service.information.CommentService;
import com.vibe.service.information.PostService;
import com.vibe.utils.FormJson;
import com.vibe.utils.FormJsonBulider;
import com.vibe.utils.Page;

@Controller
public class PostController {
	@Autowired
	private PostService postService;
	@Autowired
	private CommentService commentService;

	@RequestMapping("/information/delete")
	@ResponseBody
	public FormJson delete(int[] id) {
		postService.delete(id);
		return FormJsonBulider.success();
	}

	@RequestMapping("/information")
	public String index() {
		return "information/index";
	}
	@RequestMapping("/information/index_listHtml")
	public String index_listHtml(
			@RequestParam(value="pageNum", defaultValue="1") int pageNum,
			@RequestParam(value="pageSize", defaultValue="10") int pageSize,
			HttpSession session, Model model) {
		Page<Post> postPage = getPostList(pageNum, pageSize, session);
		model.addAttribute("postPage", postPage);
		return "information/index-list";
	}
	@RequestMapping("/information/search_listHtml")
	public String search_listHtml(PostVo vo, HttpSession session, Model model) {
		Page<Post> postPage = getSearchList(vo, session);
		model.addAttribute("postPage", postPage);
		return "information/index-list";
	}

	@RequestMapping("/information/postlist")
	@ResponseBody
	public Page<Post> getPostList(
			@RequestParam(value="pageNum", defaultValue="1") int pageNum,
			@RequestParam(value="pageSize", defaultValue="10") int pageSize,
			HttpSession session) {
		int level = 0;
		
		Page<Post> postPage = postService.getPosts(level, pageNum, pageSize);
		return postPage;
	}
	@RequestMapping("/information/search")
	@ResponseBody
	public Page<Post> getSearchList(PostVo vo, HttpSession session) {
		int pageNum = vo.getPageNum() == null ? 1 : vo.getPageNum();
		int pageSize = vo.getPageSize() == null ? 10 : vo.getPageSize();

		Page<Post> posts = postService.getSearchPosts(vo, pageNum, pageSize);
		return posts;
	}

	@RequestMapping("/information/detail")
	public String getPostDetail(
			@RequestParam(value="pid") int pid,
			HttpSession session, Model model) {

		
		Post post = postService.getPost(pid);
		List<Comment> comment = commentService.getCommentsByPost(pid);
		postService.incrementViews(pid);

		model.addAttribute("post", post);
		model.addAttribute("comments", comment);
		return "information/detail";
	}

	@RequestMapping("/information/add")
	public String addPost() {
		return "information/add";
	}

	@RequestMapping("/information/savePost")
	public ModelAndView savePost(
			@RequestParam("title") String title,
			@RequestParam("content") String content,
			@RequestParam(value="level", defaultValue="0") int level,
			HttpSession session, Model model) {
		if (title == null || title.length() == 0
				|| content == null || content.length() == 0) return new ModelAndView("redirect:/information");
		
		User user = (User) session.getAttribute("loginUser");
		//TODO 检查写权限
		
		postService.savePost(title, content, user.getId(), level);
		return new ModelAndView("redirect:/information");
	}

	@RequestMapping("/information/approve")
	@ResponseBody
	public void approve(
			@RequestParam("pid") int pid,
			HttpSession session/*, Model model*/) {
		User user = (User) session.getAttribute("loginUser");
		//TODO 检查审查权限
		
		postService.approve(pid);
	}

	@RequestMapping("/information/addComment")
	public ModelAndView addComment(
			@RequestParam("pid") int pid,
			@RequestParam(value="pcid", defaultValue="0") int pcid,
			@RequestParam(value="replyId", defaultValue="0") int replyId,
			@RequestParam("content") String content,
			HttpSession session, Model model) {
		
		User user = (User) session.getAttribute("loginUser");
		int uid = user.getId();
		
		commentService.saveComment(pid, pcid, replyId, uid, content);
		return new ModelAndView("redirect:/information/detail?pid=" + pid);
	}


	@RequestMapping("/information/addComment_html")
	public String addCommentHtml(
			@RequestParam("pid") int pid,
			@RequestParam(value="pcid", defaultValue="0") int pcid,
			@RequestParam(value="replyId", defaultValue="0") int replyId,
			@RequestParam("content") String content,
			HttpSession session, Model model) {
		
		User user = (User) session.getAttribute("loginUser");
		int uid = user.getId();
		//System.out.println("------addComment_html---------");
		commentService.saveComment(pid, pcid, replyId, uid, content);
		List<Comment> comment = commentService.getCommentsByPost(pid);
		model.addAttribute("comments", comment);
		//System.out.println(user.getLogin_id());
		return "information/detail-comment";
	}
}

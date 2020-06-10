package com.vibe.service.information;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.vibe.pojo.infomation.Post;
import com.vibe.pojo.infomation.PostVo;
import com.vibe.utils.Page;

@Repository
public interface PostService {

	Post savePost(String title, String content, int uid, int level);

	Page<Post> getPosts(int level, int pageNum, int pageSize);

	void approve(int pid);

	Post getPost(int pid);

	List<Post> getDraft(int uid, int pageNum, int pageSize);

	Page<Post> getSearchPosts(PostVo s, int pageNum, int pageSize);

	void incrementViews(int pid);

	void delete(int[] id);
}

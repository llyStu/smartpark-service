package com.vibe.service.information;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.vibe.mapper.information.PostDao;
import com.vibe.pojo.infomation.Post;
import com.vibe.pojo.infomation.PostVo;
import com.vibe.utils.Page;

@Repository
public class PostServiceImpl implements PostService {
    @Autowired
    private PostDao postDao;

    @Override
    public Post savePost(String title, String content, int uid, int level) {
        Post post = new Post();
        post.setTitle(title);
        post.setContent(content);
        post.setUid(uid);
        post.setLevel(level);
        post.setPublished(new Timestamp(System.currentTimeMillis()));
        post.setState(0);
        post.setViews(0);
        postDao.insertPost(post);
        return post;
    }

    @Override
    public Page<Post> getPosts(int level, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Post> list = postDao.queryPostList(level);
        PageInfo<Post> page = new PageInfo<>(list);

        Page<Post> result = new Page<>();
        result.setRows(list);
        result.setPage(page.getPageNum());
        result.setSize(page.getPageSize());
        result.setTotal((int) page.getTotal());
        return result;
    }

    @Override
    public Post getPost(int pid) {
        return postDao.queryPost(pid);
    }

    @Override
    public void approve(int pid) {
        postDao.updatePostState(pid, 1);
    }

    @Override
    public List<Post> getDraft(int uid, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        return postDao.queryDraft(uid);
    }

    @Override
    public Page<Post> getSearchPosts(PostVo s, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Post> list = postDao.getSearchPosts(s);
        PageInfo<Post> page = new PageInfo<>(list);

        Page<Post> result = new Page<>();
        result.setRows(list);
        result.setPage(page.getPageNum());
        result.setSize(page.getPageSize());
        result.setTotal((int) page.getTotal());
        return result;
    }

    @Override
    public void incrementViews(int pid) {
        postDao.updateViews(pid);
    }

    @Override
    public void delete(int[] ids) {
        postDao.delete(ids);
    }
}

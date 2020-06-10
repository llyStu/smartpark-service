package com.vibe.mapper.information;

import com.vibe.pojo.infomation.Comment;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentDao {

    int insert(Comment comment);

    List<Comment> queryByPost(int pid);

    List<Comment> queryByPage(
            @Param("pageCommnets") List<Comment> pageCommnets);

    String getLongUsername();
}

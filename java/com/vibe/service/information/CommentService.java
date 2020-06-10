package com.vibe.service.information;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.vibe.pojo.infomation.Comment;

@Repository
public interface CommentService {

    void saveComment(int pid, int pcid, int replyId, int uid, String content);

    List<Comment> getCommentsByPost(int pid);
}

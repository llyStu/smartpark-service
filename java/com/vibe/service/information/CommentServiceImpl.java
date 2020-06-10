package com.vibe.service.information;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.vibe.mapper.information.CommentDao;
import com.vibe.pojo.infomation.Comment;

@Repository
public class CommentServiceImpl implements CommentService {
    @Autowired
    private CommentDao commentDao;

    @Override
    public void saveComment(int pid, int pcid, int replyId, int uid, String content) {
        Comment comment = new Comment();
        comment.setPid(pid);
        comment.setPcid(pcid);
        comment.setReplyId(replyId);
        comment.setUid(uid);
        comment.setContent(content);
        comment.setCommented(new Timestamp(System.currentTimeMillis()));
        commentDao.insert(comment);
    }

    @Override
    public List<Comment> getCommentsByPost(int pid) {
        List<Comment> topComments = commentDao.queryByPost(pid);
        if (topComments == null || topComments.size() == 0) return Collections.emptyList();

        List<Comment> replies = commentDao.queryByPage(topComments);
        if (replies == null || replies.size() == 0) return topComments;


        int pcid = 0, j = -1;
        List<Comment> reply = null;
        for (Comment c : replies) {
            if (c.getPcid() != pcid) {
                pcid = c.getPcid();
                reply = new ArrayList<>();
                reply.add(c);

                while (topComments.get(++j).getCid() != pcid) ;
                topComments.get(j).setReplies(reply);
            } else {
                reply.add(c);
            }
        }
        return topComments;
    }
}

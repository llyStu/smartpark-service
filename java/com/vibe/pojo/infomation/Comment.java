package com.vibe.pojo.infomation;

import java.sql.Timestamp;
import java.util.List;


/*
create 
 */
public class Comment {
    private Integer cid;
    private Integer pid;
    private Integer uid;
    private Timestamp commented;
    private String content;
    private Integer replyId;
    private Integer pcid;
    private List<Comment> replies;
    //临时字段
    private String loginName;//当前用户名

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public Integer getCid() {
        return cid;
    }

    public void setCid(Integer cid) {
        this.cid = cid;
    }

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public Timestamp getCommented() {
        return commented;
    }

    public void setCommented(Timestamp commented) {

        this.commented = commented;
    }


    public String getContent() {
        return content;
    }

    /*public String getCommented() {
        return commented;
    }
    public void setCommented(String commented) {
        this.commented = commented.toString().substring(0, commented.toString().indexOf("."));
    }*/
    public void setContent(String content) {
        this.content = content;
    }

    public Integer getReplyId() {
        return replyId;
    }

    public void setReplyId(Integer replyId) {
        this.replyId = replyId;
    }

    public Integer getPcid() {
        return pcid;
    }

    public void setPcid(Integer pcid) {
        this.pcid = pcid;
    }

    public List<Comment> getReplies() {
        return replies;
    }

    public void setReplies(List<Comment> replies) {
        this.replies = replies;
    }

    @Override
    public String toString() {
        return "Comment [cid=" + cid + ", pid=" + pid + ", uid=" + uid + ", commented=" + commented + ", content="
                + content + ", replyId=" + replyId + ", pcid=" + pcid + ", replies=" + replies + "]";
    }


}

package com.vibe.mapper.information;

import com.vibe.pojo.infomation.Post;
import com.vibe.pojo.infomation.PostVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostDao {

    int insertPost(Post post);

    List<Post> queryPostList(int level);

    void updatePostState(
            @Param("pid") int pid,
            @Param("newLevel") int newLevel);

    Post queryPost(int pid);

    List<Post> queryDraft(@Param("uid") int uid);

    List<Post> getSearchPosts(PostVo s);

    void updateViews(int pid);

    void delete(@Param("ids") int[] ids);
}

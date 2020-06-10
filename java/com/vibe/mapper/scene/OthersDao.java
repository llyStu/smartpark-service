package com.vibe.mapper.scene;

import com.vibe.pojo.Others;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OthersDao {

    public void insertOthers(Others others);

    public void deleteOthers(int id);

    public Others queryOthers(int id);

    public List<Others> queryOthersList();

    public void updateOthers(Others others);

}

package com.vibe.service.scene;

import java.util.List;


import com.vibe.pojo.Archaeology;


public interface ArchaeologyService {

    public void insertArchaeology(Archaeology archaeology);

    public void deleteArchaeology(int parseInt);

    public Archaeology queryArchaeology(int id);

    public List<Archaeology> queryArchaeologyList();

    public void updateArchaeology(Archaeology archaeology);

}

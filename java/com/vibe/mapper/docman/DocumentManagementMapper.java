package com.vibe.mapper.docman;

import com.vibe.pojo.docman.DocumentBackup;
import com.vibe.pojo.docman.DocumentBackupVo;
import com.vibe.pojo.docman.DocumentDir;
import com.vibe.pojo.docman.DocumentDirVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentManagementMapper {

    int saveDocumentDir(DocumentDir dir);

    int updatedir(DocumentDir dir);

    List<DocumentDir> findAllDocumentDir();


    List<DocumentDir> findDocumentDir(DocumentDirVo vo);

    int backup(DocumentBackup bak);

    void addDocumentFile(@Param("did") int did, @Param("filenames") String filenames);

    int deleteDocumentDir(@Param("did") int did, @Param("visible") boolean visible);

    List<DocumentBackup> findbackup(DocumentBackupVo vo);

    int setBackupPath(@Param("path") String path);

    String getBackupPath();

    int delParent(@Param("pid") Integer pid);

    int restore(DocumentBackup backup);

    int saveBackupPath(String path);


}

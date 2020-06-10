package com.vibe.service.backup;

import java.io.File;
import java.util.List;

import com.vibe.utils.FormJson;

public interface BackupService {
    public FormJson backupData(List<String> dataBase, String path);

    public FormJson backupAll();

    public FormJson backupFiles(List<BackupBean> dirs, String backupPath);

    public FormJson recoverData(File[] srcFile);

    public FormJson recoverAll();
}

package com.ubagroup.dao;

import java.util.List;

import com.ubagroup.model.File;

public interface FileDao {
    void insertFile(File cus);
    void insertFiles(List<File> files);

    void insertFile(List<File> files);

    List<File> getAllFiles();
    File getFileById(String Id);
}
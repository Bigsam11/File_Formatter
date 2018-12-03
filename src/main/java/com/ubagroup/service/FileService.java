package com.ubagroup.service;

import java.util.List;

import com.ubagroup.model.File;

public interface FileService {
    void insertFile(File emp);
    void insertFiles(List<File> files);
    void getAllFiles();
    void getFileById(String fileId);
}
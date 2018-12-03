package com.ubagroup.service.impl;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ubagroup.dao.FileDao;
import com.ubagroup.model.File;
import com.ubagroup.service.FileService;

@Service
public class FileServiceImpl implements FileService {

    @Autowired
    FileDao fileDao;

    @Override
    public void insertFile(File file) {
        fileDao.insertFile(file);
    }

    @Override
    public void insertFiles(List<File> files) {
        fileDao.insertFiles(files);
    }

    @Override
    public void getAllFiles() {

    }

    public void getAllFile() {
        List<File> files = fileDao.getAllFiles();
        for (File file : files) {
            System.out.println(file.toString());
        }
    }

    @Override
    public void getFileById(String Id) {
        File file = fileDao.getFileById(Id);
        System.out.println(file);
    }

}
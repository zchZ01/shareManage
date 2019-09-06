package com.share.manage.service.impl;

import com.share.manage.dao.FileDao;
import com.share.manage.model.VedioFile;
import com.share.manage.service.IFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FileService implements IFileService {

    @Autowired
    FileDao fileDao;
    @Override
    public int addFile(VedioFile file) {
        return fileDao.addFile(file);
    }

    @Override
    public VedioFile showFile(int id) {
        return fileDao.showFile(id);
    }
}

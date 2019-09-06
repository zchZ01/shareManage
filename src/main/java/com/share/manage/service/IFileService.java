package com.share.manage.service;

import com.share.manage.model.VedioFile;


public interface IFileService {

    int addFile(VedioFile file);

    VedioFile showFile(int id);
}

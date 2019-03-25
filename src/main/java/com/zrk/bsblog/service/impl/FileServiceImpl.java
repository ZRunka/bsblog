package com.zrk.bsblog.service.impl;

import com.zrk.bsblog.domain.File;
import com.zrk.bsblog.repository.FileRepository;
import com.zrk.bsblog.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FileServiceImpl implements FileService {

    @Autowired
    public FileRepository fileRepository;

    @SuppressWarnings("finally")
    private File findOne(String Id) {
        File instance = null;
        try {
            List < File > fileList = fileRepository.findAll();
            for (File file: fileList) {
                if (file.getId().equals(Id)) {
                    instance = file;
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return instance;
        }
    }

    @Override
    public File saveFile(File file) {
        return fileRepository.save(file);
    }

    @Override
    public void removeFile(String id) {
        fileRepository.deleteById(id);
    }

    @Override
    public File getFileById(String id) {
        return this.findOne(id);
    }

    @Override
    public List<File> listFilesByPage(int pageIndex, int pageSize) {

        Page<File> page = null;
        List<File> list = null;

        Sort sort = new Sort(Sort.Direction.DESC,"uploadDate");
        Pageable pageable = PageRequest.of(pageIndex, pageSize, sort);

        page = fileRepository.findAll(pageable);
        list = page.getContent();
        return list;
    }
}

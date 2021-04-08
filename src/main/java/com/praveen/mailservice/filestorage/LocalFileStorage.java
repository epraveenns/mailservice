package com.praveen.mailservice.filestorage;

import com.praveen.mailservice.AppEnv;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Component
public class LocalFileStorage implements FileStorageInf {

    private final AppEnv env;

    @Autowired
    public LocalFileStorage(AppEnv env) {
        this.env = env;
    }

    @Override
    public String storeFile(File file) throws IOException {
        String filePath = env.getLocalFileStoragePath() + "/" + file.getName();
        FileOutputStream fos = new FileOutputStream(filePath);
        Files.copy(Paths.get(file.getPath()), fos);
        return filePath;
    }

    @Override
    public File getFile(String location) {
        return new File(location);
    }
}

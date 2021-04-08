package com.praveen.mailservice.filestorage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public interface FileStorageInf {
    public String storeFile(File file) throws IOException;

    public File getFile(String location);
}

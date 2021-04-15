package com.praveen.mailservice.util;

import com.praveen.mailservice.exception.FileDownloadException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

public class FileDownloadUtil {
    public static File downloadFile(String urlString) {
        HttpURLConnection con = null;
        try {
            URL url = new URL(urlString);
            con = (HttpURLConnection) url.openConnection();
            int responseCode = con.getResponseCode();
            if(responseCode != HttpURLConnection.HTTP_OK) {
                throw new FileDownloadException("Error during file download. Http Status is "+responseCode);
            }
            String fieldValue = con.getHeaderField("Content-Disposition");
            String fileName = "";
            if (fieldValue == null || ! fieldValue.contains("filename=\"")) {
                fileName = urlString.substring(urlString.lastIndexOf("/") + 1);
            } else {
                fileName = fieldValue.substring(fieldValue.indexOf("filename=\"") + 10, fieldValue.length() - 1);
            }
            File download = new File(System.getProperty("java.io.tmpdir"), fileName);

            ReadableByteChannel rbc = Channels.newChannel(con.getInputStream());
            try (FileOutputStream fos = new FileOutputStream(download)) {
                fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
            }
            return download;
        } catch (Exception e) {
            throw new FileDownloadException(e.getMessage(), e);
        }
        finally {
            if(con != null ) {
                con.disconnect();
            }
        }
    }
}

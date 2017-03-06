package com.qualsoft.volleylibrary;

import java.io.File;

/**
 * Created by suyati on 3/6/17.
 */

public class FileObject {
    public static String TYPE_IMAGE = "IMAGE";
    public static String TYPE_VIDEO = "VIDEO";
    private File file;
    private String type;

    public FileObject(File file, String type) {
        this.file = file;
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }
}

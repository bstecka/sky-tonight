package com.example.barbara.skytonight.entity;

import java.io.File;
import java.io.Serializable;

public class NoteFile implements Serializable {

    private String content;
    private File file;

    public NoteFile(String content, File file) {
        this.content = content;
        this.file = file;
    }

    public String getContent() {
        return content;
    }

    public File getFile() {
        return file;
    }

}

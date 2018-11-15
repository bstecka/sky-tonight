package com.example.barbara.skytonight.presentation.notes;

import java.io.File;

public class NoteFile {

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

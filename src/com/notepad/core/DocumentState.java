package com.notepad.core;

public class DocumentState {
    private String currentFilePath;
    private boolean modified;

    public DocumentState() {
        this.currentFilePath = null;
        this.modified = false;
    }

    public String getCurrentFilePath() {
        return currentFilePath;
    }

    public void setCurrentFilePath(String path) {
        this.currentFilePath = path;
    }

    public boolean isModified() {
        return modified;
    }

    public void markModified() {
        this.modified = true;
    }

    public void markSaved() {
        this.modified = false;
    }

    public void reset() {
        this.currentFilePath = null;
        this.modified = false;
    }
}

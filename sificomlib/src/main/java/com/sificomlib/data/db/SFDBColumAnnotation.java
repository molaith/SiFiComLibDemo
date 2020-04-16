package com.sificomlib.data.db;

public class SFDBColumAnnotation {
    private ColumType annotation;
    private boolean isPrimarykey = false;

    public SFDBColumAnnotation(ColumType annotation, boolean isPrimarykey) {
        this.annotation = annotation;
        this.isPrimarykey = isPrimarykey;
    }

    public ColumType getValueAnnotation() {
        return annotation;
    }

    public boolean isPrimaryKey() {
        return isPrimarykey;
    }
}

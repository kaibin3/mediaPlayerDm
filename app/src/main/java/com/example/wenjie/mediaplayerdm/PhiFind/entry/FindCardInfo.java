package com.example.wenjie.mediaplayerdm.PhiFind.entry;


public class FindCardInfo {

    private TYPE type;
    private String photoUrl;
    private String description;
    private String label;


    public enum TYPE {
        VIDEO,
        TEXT
    }


    public TYPE getType() {
        return type;
    }

    public void setType(TYPE type) {
        this.type = type;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }


    @Override
    public String toString() {
        return "FindCardInfo{" +
                "type=" + type +
                ", photoUrl='" + photoUrl + '\'' +
                ", description='" + description + '\'' +
                ", label='" + label + '\'' +
                '}';
    }
}

package com.example.wenjie.mediaplayerdm.data.model;


public class Media {
    private String name;
    private String path;
    private long durate;
    private long size;
    private long record;
    private int id;


    public long getRecord() {
        return record;
    }

    public void setRecord(long record) {
        this.record = record;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getDurate() {
        return durate;
    }

    public void setDurate(long durate) {
        this.durate = durate;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    /**
     * Media
     *
     * @param name
     * @param durate
     * @param size
     * @param record
     * @param path
     * @param id
     */
    public Media(String name, long durate, long size, long record, String path,
                 int id) {
        super();
        this.name = name;
        this.durate = durate;
        this.size = size;
        this.record = record;
        this.path = path;
        this.id = id;
    }


}

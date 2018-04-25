package com.woop.filetransferprototypeclient.mainactivity.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "host",
        "targetFileName",
        "submittedFileName",
        "directory"
})
public class FileListItemData {

    @JsonProperty("id")
    private int id;
    @JsonProperty("host")
    private int host;
    @JsonProperty("targetFileName")
    private String targetFileName;
    @JsonProperty("submittedFileName")
    private String submittedFileName;
    @JsonProperty("directory")
    private String directory;

    /**
     * No args constructor for use in serialization
     *
     */
    public FileListItemData() {
    }

    /**
     *
     * @param targetFileName
     * @param id
     * @param submittedFileName
     * @param host
     * @param directory
     */
    public FileListItemData(int id, int host, String targetFileName, String submittedFileName, String directory) {
        super();
        this.id = id;
        this.host = host;
        this.targetFileName = targetFileName;
        this.submittedFileName = submittedFileName;
        this.directory = directory;
    }

    @JsonProperty("id")
    public int getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(int id) {
        this.id = id;
    }

    @JsonProperty("host")
    public int getHost() {
        return host;
    }

    @JsonProperty("host")
    public void setHost(int host) {
        this.host = host;
    }

    @JsonProperty("targetFileName")
    public String getTargetFileName() {
        return targetFileName;
    }

    @JsonProperty("targetFileName")
    public void setTargetFileName(String targetFileName) {
        this.targetFileName = targetFileName;
    }

    @JsonProperty("submittedFileName")
    public String getSubmittedFileName() {
        return submittedFileName;
    }

    @JsonProperty("submittedFileName")
    public void setSubmittedFileName(String submittedFileName) {
        this.submittedFileName = submittedFileName;
    }

    @JsonProperty("directory")
    public String getDirectory() {
        return directory;
    }

    @JsonProperty("directory")
    public void setDirectory(String directory) {
        this.directory = directory;
    }

    @Override
    public String toString() {
        return "FileListItemData{" +
                "id='" + id + '\'' +
                ", host='" + host + '\'' +
                ", targetFileName='" + targetFileName + '\'' +
                ", submittedFileName='" + submittedFileName + '\'' +
                ", directory='" + directory + '\'' +
                '}';
    }
}

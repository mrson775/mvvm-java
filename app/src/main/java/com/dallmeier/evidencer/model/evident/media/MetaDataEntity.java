package com.dallmeier.evidencer.model.evident.media;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.dallmeier.evidencer.utils.Utils;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.text.DecimalFormat;
import java.util.List;

@Entity(tableName = "meta_data")
public class MetaDataEntity {
    @PrimaryKey
    @SerializedName("id")
    @Expose
    @NonNull
    private String id;
    @SerializedName("uploadedAt")
    @Expose
    private long uploadedAt;
    @SerializedName("originalFileName")
    @Expose
    private String originalFileName;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("contentType")
    @Expose
    private String contentType;
    @SerializedName("fileType")
    @Expose
    private String fileType;
    @SerializedName("originalFileSize")
    @Expose
    private long originalFileSize;
    @SerializedName("checkSum")
    @Ignore
    @Expose
    private Object checkSum;
    @SerializedName("displayMeta")
    @Ignore
    @Expose
    private Object displayMeta;
    @SerializedName("fileOrigin")
    @Expose
    private String fileOrigin;
    @SerializedName("expiretime")
    @Ignore
    @Expose
    private Object expiretime;
    @SerializedName("resourceLevel")
    @Ignore
    @Expose
    private Object resourceLevel;
    @Ignore
    @SerializedName("tags")
    @Expose
    private List<Object> tags = null;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getUploadedAt() {
        return uploadedAt;
    }

    public void setUploadedAt(long uploadedAt) {
        this.uploadedAt = uploadedAt;
    }

    public String getOriginalFileName() {
        return originalFileName;
    }

    public void setOriginalFileName(String originalFileName) {
        this.originalFileName = originalFileName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public long getOriginalFileSize() {
        return originalFileSize;
    }

    public void setOriginalFileSize(long originalFileSize) {
        this.originalFileSize = originalFileSize;
    }

    public Object getCheckSum() {
        return checkSum;
    }

    public void setCheckSum(Object checkSum) {
        this.checkSum = checkSum;
    }

    public Object getDisplayMeta() {
        return displayMeta;
    }

    public void setDisplayMeta(Object displayMeta) {
        this.displayMeta = displayMeta;
    }

    public String getFileOrigin() {
        return fileOrigin;
    }

    public void setFileOrigin(String fileOrigin) {
        this.fileOrigin = fileOrigin;
    }

    public Object getExpiretime() {
        return expiretime;
    }

    public void setExpiretime(Object expiretime) {
        this.expiretime = expiretime;
    }

    public Object getResourceLevel() {
        return resourceLevel;
    }

    public void setResourceLevel(Object resourceLevel) {
        this.resourceLevel = resourceLevel;
    }

    public List<Object> getTags() {
        return tags;
    }

    public void setTags(List<Object> tags) {
        this.tags = tags;
    }

    public String getCreateString() {
        return Utils.getFormattedDate(getUploadedAt());
    }

    public String getFileSize() {
        if (getOriginalFileSize() <= 0)
            return "0";
        final String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(getOriginalFileSize()) / Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(getOriginalFileSize() / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }
}
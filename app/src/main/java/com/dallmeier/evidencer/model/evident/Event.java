package com.dallmeier.evidencer.model.evident;

import android.graphics.drawable.Drawable;
import android.util.Log;

import com.dallmeier.evidencer.R;
import com.dallmeier.evidencer.base.BaseApplication;
import com.dallmeier.evidencer.model.incident_response.Location;
import com.dallmeier.evidencer.utils.Utils;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import static com.dallmeier.evidencer.common.Statics.MIMETYPE_IMAGE;
import static com.dallmeier.evidencer.common.Statics.MIMETYPE_IMAGE_PNG;
import static com.dallmeier.evidencer.common.Statics.MIMETYPE_VIDEO;
import static com.dallmeier.evidencer.common.Statics.MIME_TYPE_AUDIO;
import static com.dallmeier.evidencer.common.Statics.MIME_TYPE_AUDIO_MPEG;
import static com.dallmeier.evidencer.common.Statics.MIME_TYPE_PDF;

public class Event {
    @SerializedName("created")
    @Expose
    private long created;
    @SerializedName("updated")
    @Expose
    private long updated;
    @SerializedName("version")
    @Expose
    private Integer version;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("criticality")
    @Expose
    private Integer criticality;
    @SerializedName("timeStampMillis")
    @Expose
    private Integer timeStampMillis;
    @SerializedName("expireTime")
    @Expose
    private Object expireTime;
    @SerializedName("categorization")
    @Expose
    private Categorization categorization;
    @SerializedName("location")
    @Expose
    private Location location;
    @SerializedName("uploader")
    @Expose
    private Uploader uploader;
    @SerializedName("lastModifiedBy")
    @Expose
    private String lastModifiedBy;
    @SerializedName("extraData")
    @Expose
    private ExtraData extraData;
    @SerializedName("systemLocationCode")
    @Expose
    private Integer systemLocationCode;
    @SerializedName("mediaAttachments")
    @Expose
    private List<MediaAttachment> mediaAttachments = null;
    @SerializedName("installationId")
    @Expose
    private Object installationId;
    @SerializedName("dateObserved")
    @Expose
    private Integer dateObserved;
    @SerializedName("author")
    @Expose
    private Author author;
    @SerializedName("encoderName")
    @Expose
    private Object encoderName;
    @SerializedName("encoderNumber")
    @Expose
    private Object encoderNumber;
    @SerializedName("type")
    @Expose
    private String type;
    private String urlMedia;

    public String getUrlMedia() {
        return urlMedia;
    }

    public void setUrlMedia(String urlMedia) {
        this.urlMedia = urlMedia;
    }

    public long getCreated() {
        return created;
    }

    public void setCreated(long created) {
        this.created = created;
    }

    public long getUpdated() {
        return updated;
    }

    public void setUpdated(long updated) {
        this.updated = updated;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCriticality() {
        return criticality;
    }

    public void setCriticality(Integer criticality) {
        this.criticality = criticality;
    }

    public Integer getTimeStampMillis() {
        return timeStampMillis;
    }

    public void setTimeStampMillis(Integer timeStampMillis) {
        this.timeStampMillis = timeStampMillis;
    }

    public Object getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Object expireTime) {
        this.expireTime = expireTime;
    }

    public Categorization getCategorization() {
        return categorization;
    }

    public void setCategorization(Categorization categorization) {
        this.categorization = categorization;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Uploader getUploader() {
        return uploader;
    }

    public void setUploader(Uploader uploader) {
        this.uploader = uploader;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public ExtraData getExtraData() {
        return extraData;
    }

    public void setExtraData(ExtraData extraData) {
        this.extraData = extraData;
    }

    public Integer getSystemLocationCode() {
        return systemLocationCode;
    }

    public void setSystemLocationCode(Integer systemLocationCode) {
        this.systemLocationCode = systemLocationCode;
    }

    public List<MediaAttachment> getMediaAttachments() {
        return mediaAttachments;
    }

    public void setMediaAttachments(List<MediaAttachment> mediaAttachments) {
        this.mediaAttachments = mediaAttachments;
    }

    public Object getInstallationId() {
        return installationId;
    }

    public void setInstallationId(Object installationId) {
        this.installationId = installationId;
    }

    public Integer getDateObserved() {
        return dateObserved;
    }

    public void setDateObserved(Integer dateObserved) {
        this.dateObserved = dateObserved;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public Object getEncoderName() {
        return encoderName;
    }

    public void setEncoderName(Object encoderName) {
        this.encoderName = encoderName;
    }

    public Object getEncoderNumber() {
        return encoderNumber;
    }

    public void setEncoderNumber(Object encoderNumber) {
        this.encoderNumber = encoderNumber;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String timelineString() {
        String typeString = "";
        switch (categorization.getValue()) {
            case MIMETYPE_IMAGE:
            case MIMETYPE_IMAGE_PNG:
                typeString = BaseApplication.getInstance().getApplicationContext().getString(R.string.new_image_evidence_added);
                break;
            case MIMETYPE_VIDEO:
                typeString = BaseApplication.getInstance().getApplicationContext().getString(R.string.new_video_evidence_added);
                break;
            case MIME_TYPE_AUDIO:
            case MIME_TYPE_AUDIO_MPEG:
                typeString = BaseApplication.getInstance().getApplicationContext().getString(R.string.new_voice_evidence_added);
                break;
            case MIME_TYPE_PDF:
            default:
                typeString = BaseApplication.getInstance().getApplicationContext().getString(R.string.new_file_evidence_added);
                break;
        }
        return typeString;
    }

    public Drawable typeDrawable() {
        Drawable drawable = null;
        switch (categorization.getValue()) {
            case MIMETYPE_IMAGE:
            case MIMETYPE_IMAGE_PNG:
                drawable = Utils.getDrawableAttr(BaseApplication.getInstance().getApplicationContext(), new int[]{R.attr.ic_photo});
                break;
            case MIMETYPE_VIDEO:
                drawable = Utils.getDrawableAttr(BaseApplication.getInstance().getApplicationContext(), new int[]{R.attr.ic_video});
                break;
            case MIME_TYPE_AUDIO:
            case MIME_TYPE_AUDIO_MPEG:
                drawable = Utils.getDrawableAttr(BaseApplication.getInstance().getApplicationContext(), new int[]{R.attr.ic_micro});
                break;
            case MIME_TYPE_PDF:
                drawable = Utils.getDrawableAttr(BaseApplication.getInstance().getApplicationContext(), new int[]{R.attr.ic_pdf});
                break;
            default:
                drawable = Utils.getDrawableAttr(BaseApplication.getInstance().getApplicationContext(), new int[]{R.attr.ic_file});
        }
        return drawable;
    }

    public String timeAgo() {
        Log.d("timeAgo", created + "");
        long timCurrent = System.currentTimeMillis();
        //  return DateUtils.getRelativeTimeSpanString(Long.parseLong("1605758071379"), timCurrent, DateUtils.FORMAT_ABBREV_RELATIVE).toString();
        return Utils.getTimeAgo(created, BaseApplication.getInstance().getApplicationContext(), false);
    }
}
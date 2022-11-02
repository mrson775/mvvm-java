package com.dallmeier.evidencer.model.evident;

import android.graphics.drawable.Drawable;
import android.util.Log;

import com.dallmeier.evidencer.R;
import com.dallmeier.evidencer.base.BaseApplication;
import com.dallmeier.evidencer.utils.Utils;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import static com.dallmeier.evidencer.common.Statics.MIMETYPE_IMAGE;
import static com.dallmeier.evidencer.common.Statics.MIMETYPE_IMAGE_PNG;
import static com.dallmeier.evidencer.common.Statics.MIMETYPE_VIDEO;
import static com.dallmeier.evidencer.common.Statics.MIME_TYPE_AUDIO;
import static com.dallmeier.evidencer.common.Statics.MIME_TYPE_AUDIO_MPEG;
import static com.dallmeier.evidencer.common.Statics.MIME_TYPE_PDF;

public class MediaAttachment {
    @SerializedName("source")
    @Expose
    private String source;
    @SerializedName("mimeType")
    @Expose
    private String mimeType;
    @SerializedName("properties")
    @Expose
    private Properties properties;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("showNow")
    @Expose
    private Boolean showNow;
    @SerializedName("order")
    @Expose
    private Integer order;
    @SerializedName("url")
    @Expose
    private String url;
    @Expose
    private Long dateObserved;
    @SerializedName("timeStampMillis")
    @Expose
    private Long timeStampMillis;

    public Long getDateObserved() {
        return dateObserved;
    }

    public void setDateObserved(Long dateObserved) {
        this.dateObserved = dateObserved;
    }

    public Long getTimeStampMillis() {
        return timeStampMillis;
    }

    public void setTimeStampMillis(Long timeStampMillis) {
        this.timeStampMillis = timeStampMillis;
    }

    @SerializedName("description")

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getShowNow() {
        return showNow;
    }

    public void setShowNow(Boolean showNow) {
        this.showNow = showNow;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Drawable typeDrawable() {
        Drawable drawable = null;
        switch (getMimeType()) {
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
        Log.d("timeAgo", getTimeStampMillis() + "");
        long timCurrent = System.currentTimeMillis();
        //  return DateUtils.getRelativeTimeSpanString(Long.parseLong("1605758071379"), timCurrent, DateUtils.FORMAT_ABBREV_RELATIVE).toString();
        return Utils.getTimeAgo(getTimeStampMillis(), BaseApplication.getInstance().getApplicationContext(), false);
    }

    public String timelineString() {
        String typeString = "";
        switch (getMimeType()) {
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
}

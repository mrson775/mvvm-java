package com.dallmeier.evidencer.common;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.ACCESS_NETWORK_STATE;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class Statics {
    public static final String PLACES_LON = "PLACES_LON";
    public static final String PLACES_LAT = "PLACES_LAT";
    public static final String ADDRESS = "ADDRESS";
    public static final String INCIDENT = "INCIDENT";
    public static final String MEDIA_ID = "MEDIA_ID";
    public static final String AGG_EVENT_ID = "AGG_EVENT_ID";
    public static final String ARG_SECTION_NUMBER_TIMELINE = "ARG_SECTION_NUMBER_TIMELINE";
    public static final String ARG_SECTION_NUMBER_COMMENT = "ARG_SECTION_NUMBER_COMMENT";
    public static final String ID_EVIDENT = "ID_EVIDENT";
    public static final String FORMAT_FILE_JPG = ".jpg";
    public static final String FORMAT_FILE_MP4 = ".mp4";
    public static final String FORMAT_FILE_PDF = ".pdf";
    public static final String MIMETYPE_IMAGE = "image/jpeg";
    public static final String MIMETYPE_IMAGE_PNG = "image/png";
    public static final String MIMETYPE_VIDEO = "video/mp4";
    public static final String MIME_TYPE_AUDIO = "audio/mp4";
    public static final String MIME_TYPE_AUDIO_MPEG = "audio/mpeg";
    public static final String MIME_TYPE_PDF = "application/pdf";
    public static final String URL_MEDIA = "/media-storage/rest/media/";
    public static final int EXIT_APP = 0;
    public static final int LOGOUT = 1;
    public static final int RECORDING = 1;
    public static final int DEFAULT_AUDIO = 0;
    public static final int INCIDENT_USER_CURRENT = 0;
    public static final int INCIDENT_ALL = 1;
    public static final String DARK_THEME = "dark_theme";
    public static final String LIGHT_THEME = "light_theme";
    public static final String POINT = "point";
    public static final String PHOTO_ALBUM = "Evidencer";
    public static final String userRequest = "{\"combine\":\"AND\",\"criterias\":[{\"property\":\"authGroups.permissions.authority\",\"operation\":\"IN\",\"values\":[\"AIMS_INCIDENT_READ\",\"AIMS_INCIDENT\"]},{\"property\":\"userEnabled\",\"operation\":\"=\",\"values\":[true]}]}";
    public static final String[] permission = {ACCESS_FINE_LOCATION, WRITE_EXTERNAL_STORAGE, ACCESS_NETWORK_STATE, ACCESS_COARSE_LOCATION, CAMERA};
}

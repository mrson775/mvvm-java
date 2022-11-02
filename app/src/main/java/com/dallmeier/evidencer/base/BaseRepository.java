package com.dallmeier.evidencer.base;

import com.dallmeier.evidencer.dao.AppDatabase;
import com.dallmeier.evidencer.model.ImageEntity;
import com.dallmeier.evidencer.model.evident.Author;
import com.dallmeier.evidencer.model.incident_response.Location;
import com.dallmeier.evidencer.model.upload.FileMeta;
import com.dallmeier.evidencer.network.ApiResponseCallback;
import com.dallmeier.evidencer.network.ApiService;
import com.dallmeier.evidencer.network.ApiTask;
import com.dallmeier.evidencer.network.ApiTaskType;
import com.google.gson.Gson;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static com.dallmeier.evidencer.utils.Utils.getMimeType;

public class BaseRepository implements ApiResponseCallback, IUploadMedia {
    /**
     * The Api Service
     */
    public ApiService mService;

    public Gson gson;


    @Inject
    public BaseRepository(ApiService apiService, Gson gson) {
        this.mService = apiService;
        this.gson = gson;
    }

    @Override
    public boolean onResponse(ApiTask task) {
        return false;
    }

    @Override
    public void createUploadMediaRequest(String filePath, FileMeta meta, long aggregatedEventId) {
        File file = new File(filePath);
        // Assume your file is PNG
        RequestBody requestFile =
                RequestBody.create(MediaType.parse(getMimeType(filePath)), file);
        // MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("data", file.getName(), requestFile);
        String metaString = gson.toJson(meta);
        ApiTask.execute(() -> mService.uploadMedia(metaString, body, aggregatedEventId), ApiTaskType.UPLOAD, this);
    }


    @Override
    public void upload(AppDatabase appDatabase, ImageEntity imageEntity, long AggId, FileMeta mFileMeta, Location location) {
        mFileMeta.setCriticality(0);
        mFileMeta.setTimeStampMillis(System.currentTimeMillis());
        mFileMeta.setDescription(imageEntity.getTitle());
        mFileMeta.setInstallationId(1);
        mFileMeta.setDateObserved(System.currentTimeMillis());
        List<Double> coordinates = new ArrayList<>(2);
        coordinates.add(0, appDatabase.userDao().getUserInfo().getLongitude());
        coordinates.add(1, appDatabase.userDao().getUserInfo().getLatitude());
        location.setCoordinates(coordinates);
        location.setType("Point");
        mFileMeta.setLocation(location);
        Author author = new Author();
        author.setUsername(appDatabase.userDao().getUserInfo().getUserName());
        author.setFirstname("");
        author.setLastname("");
        mFileMeta.setAuthor(author);
        mFileMeta.setDescription("");
        createUploadMediaRequest(imageEntity.getImage(), mFileMeta, AggId);
    }
}

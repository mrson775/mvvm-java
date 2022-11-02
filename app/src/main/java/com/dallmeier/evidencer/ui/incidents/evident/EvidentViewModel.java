package com.dallmeier.evidencer.ui.incidents.evident;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.provider.MediaStore;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.dallmeier.evidencer.base.BaseApplication;
import com.dallmeier.evidencer.base.BaseViewModel;
import com.dallmeier.evidencer.base.IUpdateEvidents;
import com.dallmeier.evidencer.dao.AppDatabase;
import com.dallmeier.evidencer.model.ImageEntity;
import com.dallmeier.evidencer.model.incident_response.IncidentEntity;
import com.dallmeier.evidencer.model.incident_response.Location;
import com.dallmeier.evidencer.model.upload.FileMeta;
import com.google.gson.JsonObject;

import java.util.List;

import javax.inject.Inject;

public class EvidentViewModel extends BaseViewModel<EvidentRepository> {
    private MutableLiveData<JsonObject> responseUpload = new MutableLiveData<>();
    private MutableLiveData<JsonObject> responseComment = new MutableLiveData<>();
    private final EvidentRepository mRepository;
    private LiveData<List<ImageEntity>> imagesLiveData = new MutableLiveData<>();
    private final String[] projection = {MediaStore.MediaColumns.DATA};
    private final FileMeta mFileMeta;
    private final Location location;
    private final AppDatabase appDatabase;
    private IUpdateEvidents iUpdateEvidents;

    @Inject
    public EvidentViewModel(EvidentRepository mRepository, FileMeta mFileMeta, Location location, AppDatabase appDatabase) {
        super(mRepository);
        this.mRepository = mRepository;
        this.mFileMeta = mFileMeta;
        this.location = location;
        this.appDatabase = appDatabase;
        this.iUpdateEvidents = this;
    }

    // get all images from external storage
    public void getAllImages() {
        imagesLiveData = appDatabase.imageSdDao().getImageEntitiesLive();
        Cursor cursor = BaseApplication.getInstance().getApplicationContext().getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, null, null, null);
        while (cursor.moveToNext()) {
            @SuppressLint("Range") String absolutePathOfImage = cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DATA));
            ImageEntity ImageModel = new ImageEntity();
            ImageModel.setImage(absolutePathOfImage);
            appDatabase.imageSdDao().insertOrUpdate(ImageModel);
        }
        cursor.close();
    }

    public void setUpload(ImageEntity imageEntity, long AggId, int size) {
        mRepository.uploadIml(getResponseUpload(), imageEntity, AggId, mFileMeta, location, size);
    }

    /**
     * get image list
     *
     * @return List
     */
    public LiveData<List<ImageEntity>> getImagesLive() {
        if (imagesLiveData == null) {
            imagesLiveData = new MutableLiveData<>();
        }
        return imagesLiveData;
    }

    /**
     * Response after upload image/video
     *
     * @return JsonObject
     */
    public MutableLiveData<JsonObject> getResponseUpload() {
        if (responseUpload == null) {
            responseUpload = new MutableLiveData<>();
        }
        return responseUpload;
    }

    /**
     * @return List<Incident>
     */
    public MutableLiveData<JsonObject> getResponseComment() {
        if (responseComment == null) {
            responseComment = new MutableLiveData<>();
        }
        return responseComment;
    }

    public AppDatabase getAppDatabase() {
        return appDatabase;
    }

    public void updateCountEvidents(AppDatabase appDatabase, long incidentId) {
        this.iUpdateEvidents.updateEvidents(appDatabase, incidentId);
    }
}

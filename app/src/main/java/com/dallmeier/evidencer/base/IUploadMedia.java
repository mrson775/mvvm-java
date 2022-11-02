package com.dallmeier.evidencer.base;

import com.dallmeier.evidencer.dao.AppDatabase;
import com.dallmeier.evidencer.model.ImageEntity;
import com.dallmeier.evidencer.model.incident_response.Location;
import com.dallmeier.evidencer.model.upload.FileMeta;

public interface IUploadMedia {
    void createUploadMediaRequest(String filePath, FileMeta meta, long aggregatedEventId);
    void upload(AppDatabase appDatabase, ImageEntity imageEntity, long AggId, FileMeta mFileMeta, Location location);
}

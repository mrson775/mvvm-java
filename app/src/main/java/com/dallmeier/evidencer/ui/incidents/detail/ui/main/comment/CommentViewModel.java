package com.dallmeier.evidencer.ui.incidents.detail.ui.main.comment;

import androidx.lifecycle.MutableLiveData;

import com.dallmeier.evidencer.base.BaseViewModel;
import com.dallmeier.evidencer.base.IUpdateEvidents;
import com.dallmeier.evidencer.dao.AppDatabase;
import com.dallmeier.evidencer.model.Comment;
import com.dallmeier.evidencer.model.incident_response.IncidentEntity;

import java.util.List;

import javax.inject.Inject;

public class CommentViewModel extends BaseViewModel<CommentRepository> {
    private MutableLiveData<List<Comment>> commentsLiveData = new MutableLiveData<List<Comment>>();
    private AppDatabase appDatabase;
    private IUpdateEvidents iUpdateEvidents;

    @Inject
    public CommentViewModel(CommentRepository commentRepository, AppDatabase appDatabase) {
        super(commentRepository);
        this.appDatabase = appDatabase;
        iUpdateEvidents = this;
    }

    public void setCommentsLiveData(long incidentId) {
        repository.getComments(incidentId, getCommentsLive());
    }

    public MutableLiveData<List<Comment>> getCommentsLive() {
        if (commentsLiveData == null) {
            commentsLiveData = new MutableLiveData<>();
        }
        return commentsLiveData;
    }

    public AppDatabase appDatabase() {
        return appDatabase;
    }

    /**
     * update
     * number of comments of the incident
     *
     * @param count      int
     * @param incidentId int
     */
    public void updateIncident(long incidentId) {
        iUpdateEvidents.updateComments(appDatabase, incidentId);
    }
}
package com.dallmeier.evidencer.base;

import com.dallmeier.evidencer.dao.AppDatabase;

public interface IUpdateEvidents {
    void updateEvidents(AppDatabase appDatabase, long incidentId);
    void updateComments(AppDatabase appDatabase, long incidentId);
}

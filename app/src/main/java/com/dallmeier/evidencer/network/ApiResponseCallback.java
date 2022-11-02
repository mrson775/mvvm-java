package com.dallmeier.evidencer.network;

/**
 * Copyright DmVn Vietnam
 * Created by Mr Son on 6/24/2021.
 */
public interface ApiResponseCallback {
    /**
     * Process Response
     *
     * @param task ApiTask
     * @return True if Task is finished and do next task
     */
    boolean onResponse(ApiTask task);
}

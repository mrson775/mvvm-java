package com.dallmeier.evidencer.network;

/**
 *
 * Created by Mr Son 6/24/2016.
 */
public interface OnResponseListener {
    /**
     * Receive Response
     * @param task ApiTask
     * @param status int
     * @return True if Task is Finished and Execute next Task
     */
    boolean onResponse(ApiTask task, int status);

    /**
     * Will Process on Child Classes
     * @param task ApiTask
     * @param status int
     * @return True if Response is Process on Child Classes
     */
    boolean willProcess(ApiTask task, int status);
}

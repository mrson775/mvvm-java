package com.dallmeier.evidencer.network;


import com.dallmeier.evidencer.model.AccessToken;
import com.dallmeier.evidencer.model.AggregatedEventId;
import com.dallmeier.evidencer.model.AssigneeEntity;
import com.dallmeier.evidencer.model.Comment;
import com.dallmeier.evidencer.model.CommentDto;
import com.dallmeier.evidencer.model.incident.create.IncidentDto;
import com.dallmeier.evidencer.model.incident_response.IncidentEntity;
import com.dallmeier.evidencer.model.incident_response.StateEntity;
import com.dallmeier.evidencer.model.place.Places;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    @FormUrlEncoded
    @POST("/auth/oauth2/token")
    Call<AccessToken> getAccessToken(@Field("username") String username,
                                     @Field("password") String password,
                                     @Field("grant_type") String grantType);


    @POST("/aims2/rest/mobile/eepevent")
    Call<JsonObject> getEvents(@Query("page") int page,
                               @Query("size") int size,
                               @Query("sort") String sort);

    @POST("aims2/rest/mobile/eepevent/mediaattachment/filter/")
    Call<JsonObject> getMediaAttachment(@Body AggregatedEventId aggregatedEventId,
                                        @Query("page") int page,
                                        @Query("size") int size,
                                        @Query("sort") String sort);


    @GET("/auth/rest/persons/currentPerson")
    Call<JsonObject> getCurrentPerson();

    @GET("/aims2/rest/incident/near")
    Call<JsonObject> getIncidentNear(@Query("latitude") double latitude, @Query("longitude") double longitude,
                                     @Query("maxDistanceKm") double maxDistanceKm,
                                     @Query("page") String page,
                                     @Query("size") int size,
                                     @Query("sort") String sort);

    @GET("aims2/rest/mobile/incident/near")
    Call<List<IncidentEntity>> getIncidentNearV1(@Query("latitude") double latitude, @Query("longitude") double longitude,
                                                 @Query("maxDistanceKm") double maxDistanceKm);

    @GET("/aims2/rest/mobile/incident")
    Call<JsonObject> getIncidents(@Query("page") String page,
                                  @Query("size") int size,
                                  @Query("sort") String sort);

    @GET("/aims2/rest/state/getAll")
    Call<List<StateEntity>> getIncidents();

    /**
     * request evident to show timeline screen...vv
     *
     * @param aggregatedEventId long
     * @return Evident
     */
    @GET("/eep/rest/event/aggregated/{aggregatedEventId}")
    Call<JsonObject> getEvidents(@Path("aggregatedEventId") long aggregatedEventId);

    @GET("/media-storage/rest/media/{uuid}/metadata")
    Call<JsonObject> getMetaMedia(@Path("uuid") String uuid);

    @GET("/aims2/rest/incidenttype")
    Call<JsonObject> getIncidentType(@Query("page") String page,
                                     @Query("size") int size,
                                     @Query("sort") String sort);

    @GET("/search.php?format=json&polygon=1&addressdetails=1")
    Call<List<Places>> filterAddress(@Query("q") String address);

    @POST("/aims2/rest/incident/remote")
    Call<com.dallmeier.evidencer.model.incident.create.Incident> createNewIncident(@Body IncidentDto incidentDto);

    @GET("/auth/oauth2/revoke")
    Call<JsonObject> logout(@Query("token") String token);

    @Multipart
    @POST("eep/rest/event/media")
    Call<JsonObject> uploadMedia(
            @Query("meta") String meta,
            @Part MultipartBody.Part data,
            @Query("aggregatedEventId") long aggregatedEventId);

    @POST("/aims2/rest/comment")
    Call<JsonObject> createComment(@Body CommentDto commentDto);

    @GET("/aims2/rest/comment/incident/{incidentId}")
    Call<List<Comment>> getCommentOfIncident(@Path("incidentId") long incidentId);

    @POST("/aims2/rest/incident/users")
    Call<List<AssigneeEntity>> getUsers(@Body JSONObject jsonObject);
}

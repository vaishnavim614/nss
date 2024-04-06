package com.test.nss.api;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface Api {

    @POST("/api/token/login")
    @FormUrlEncoded
    Call<ResponseBody> login(
            @Field("username") String username,
            @Field("password") String password
    );

    @GET("/diary/api/campdetails")
    Call<ResponseBody> getCampDetails(@Header("Authorization") String token);

    @GET("/diary/api/PoDetials/")
    Call<ResponseBody> getPoData(@Header("Authorization") String token);

    @POST("/diary/selfreg")
    @FormUrlEncoded
    Call<ResponseBody> signup(
            @Field("State") String state,
            @Field("DateOfRegistration") String dof,
            @Field("FirstName") String f_name,
            @Field("FatherName") String fath_name,
            @Field("MotherName") String mom_name,
            @Field("LastName") String last_name,
            @Field("VEC") String vec,
            @Field("Email") String email,
            @Field("CollegeName") String college_name,
            @Field("Contact") String contact,
            @Field("Password") String pass,

            @Field("AppAuth") String p
    );

    @GET("/diary/api/collegenames/")
    Call<ResponseBody> getClgList();

    @POST("/api/token/logout/")
    Call<Void> delToken(@Header("Authorization") String token);

    @GET("/diary/api/campactivitylist/")
    Call<ResponseBody> getCampList(@Header("Authorization") String token);

    @POST("diary/api/campactivities/")
    @FormUrlEncoded
    Call<ResponseBody> sendCampDetail(
            @Header("Authorization") String token,
            @Field("CollegeName") String clgName,
            @Field("CampActivityDescription") String desc,
            @Field("Day") int day,
            @Field("VEC") String vec,
            @Field("State") String state,
            @Field("AppAuth") String p,

            @Field("CampActivityTitle") String campTitle
    );

    @GET("/diary/api/leaders/")
    Call<ResponseBody> insertLeaders(@Header("Authorization") String token);

    @GET("/diary/api/campactivities/")
    Call<ResponseBody> getCampActListAll(@Header("Authorization") String token);

    @GET("/diary/api/allactivites/")
    Call<ResponseBody> getActList(@Header("Authorization") String token);

    @GET("/diary/api/level/")
    Call<ResponseBody> getHours(@Header("Authorization") String token);

    @GET("/diary/api/AreaProjectView/")
    Call<ResponseBody> getFyAct(@Header("Authorization") String token);

    @GET("/diary/api/AreaProjectPreviousView/")
    Call<ResponseBody> getSyAct(@Header("Authorization") String token);

    @GET("/diary/api/workhours/")
    Call<ResponseBody> getHoursId(@Header("Authorization") String token);

    @POST("/diary/dailyactivity")
    @FormUrlEncoded
    Call<ResponseBody> sendActList(
            @Header("Authorization") String token,
            @Field("VEC") String vec,
            @Field("AssignedActivityName") int actAssignName,
            @Field("Hours") int actHour,
            @Field("Date") String actDate,
            @Field("ActivityName") int actName,
            @Field("AppAuth") String p,
            @Field("ActDescription") String aa,

            @Field("State") int s
    );

    @GET("/diary/api/dailyactivity/")
    Call<ResponseBody> getDailyAct(@Header("Authorization") String token);

    @GET("/diary/api/GetSelfRegView/")
    Call<ResponseBody> getUserDetail(@Header("Authorization") String token);

    @PUT("diary/SelfRegContinue")
    Call<ResponseBody> putContinue(@Header("Authorization") String token, @Field("AppAuth") String p);

    @PATCH("/diary/dailyactivity/{id2}/")
    @FormUrlEncoded
    Call<ResponseBody> putHour(@Header("Authorization") String token,
                               @Field("Hours") int hours,
                               @Field("VEC") String vec,
                               @Field("ActivityName") int actName,
                               @Field("AssignedActivityName") int actId,
                               @Field("State") int s,
                               @Field("AppAuth") String p,

                               @Path("id2") int id2
    );

    @PATCH("/diary/dailyactivityLeaders/{id2}/")
    @FormUrlEncoded
    Call<ResponseBody> putDetailsLeader(@Header("Authorization") String token,
                                        @Field("Hours") int hours,
                                        @Field("VEC") String vec,
                                        @Field("ActivityName") int actName,
                                        @Field("AssignedActivityName") int actId,
                                        @Field("State") int s,
                                        @Field("AppAuth") String p,

                                        @Path("id2") int id2
    );

    @PUT("/diary/dailyactivityLeaders/{id2}/")
    @FormUrlEncoded
    Call<ResponseBody> putApprove(@Header("Authorization") String token,
                                  @Field("Hours") int hours,
                                  @Field("VEC") String vec,
                                  @Field("ActivityName") int actName,
                                  @Field("AssignedActivityName") int actId,
                                  @Field("State") int s,
                                  @Field("ApprovedBy") int by,
                                  @Field("AppAuth") String p,

                                  @Path("id2") int id2
    );

    @PUT("/diary/campactivity/{id2}/")
    @FormUrlEncoded
    Call<ResponseBody> putCamp(@Header("Authorization") String token,

                               @Field("VEC") String vec,
                               @Field("CampActivityTitle") int actId,
                               @Field("CampActivityDescription") String desc,
                               @Field("Day") int id,
                               @Field("CollegeName") String clgName,
                               @Field("State") int s,

                               @Field("AppAuth") String p,
                               @Path("id2") int id2
    );

    @PUT("/diary/workhours/{id}/")
    @FormUrlEncoded
    Call<ResponseBody> insertHour(@Header("Authorization") String token,
                                  @Field("CompletedHours") int compHour,
                                  @Field("RemainingHours") int remHour,
                                  @Field("VEC") String vec,
                                  @Field("ActivityName") int actName,
                                  @Field("Level") String level,

                                  @Field("AppAuth") String p,

                                  @Path("id") int id

    );

    @POST("/diary/api/password_reset/")
    @FormUrlEncoded
    Call<ResponseBody> sendEmail(@Field("email") String email);

    @POST("/diary/api/password_reset/confirm/")
    @FormUrlEncoded
    Call<ResponseBody> verifyPass(@Field("email") String email, @Field("token") String otp, @Field("password") String newPass);

    @GET("/diary/api/IsLeader/")
    Call<ResponseBody> isLeader(@Header("Authorization") String token);

    @GET("/diary/api/listVolNotApproved/")
    Call<ResponseBody> volAct(@Header("Authorization") String token);

    @GET("/diary/api/allVolList/")
    Call<ResponseBody> volActAll(@Header("Authorization") String token);

    @GET("/diary/getVolDailyAct/{vec}")
    Call<ResponseBody> volActVec(@Header("Authorization") String token,
                                 @Path("vec") String vec);
}

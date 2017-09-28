package com.project.seoulmarket.service;

import com.project.seoulmarket.detail.model.DetailResultData;
import com.project.seoulmarket.detail.model.FavoriteResult;
import com.project.seoulmarket.join.model.JoinData;
import com.project.seoulmarket.join.model.UserNickCheckResult;
import com.project.seoulmarket.login.model.Token;
import com.project.seoulmarket.main.model.ResultData;
import com.project.seoulmarket.main.model.ResultFilter;
import com.project.seoulmarket.mypage.model.LikeResult;
import com.project.seoulmarket.mypage.model.RecruitResult;
import com.project.seoulmarket.mypage.model.ReportResult;
import com.project.seoulmarket.recruit.model.AddReview;
import com.project.seoulmarket.recruit.model.ResultRecruitDetail;
import com.project.seoulmarket.splash.model.ConnectResult;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by kh on 2016. 8. 25..
 */
public interface NetworkService {

    //연결확인
    @GET("/connect")
    Call<ConnectResult> getConnection();

    // 페이스북 로그인 요청
    @POST("/auth/facebook/token")
    Call<ConnectResult> requestFacebookLogin(@Body Token token);

    // 카카오톡 로그인요청
    @GET("/auth/kakao/token")
    Call<ConnectResult> requestKakaoLogin(@Query("access_token") String access_token);

    // 닉네임 중복체크
    @GET("/me")
    Call<ConnectResult> nickNameDoubleCheck(@Query("nickname") String nickname);

    // 이미 가입된 회원인지 체크
    @GET("/me")
    Call<UserNickCheckResult> userCheck(@Query("action") String action);



    //회원가입 -- 닉네임
    @PUT("/me")
    Call<ConnectResult> joinPutNickName(@Body JoinData nickname);

    //메인페이지 데이터
    @GET("/main")
    Call<ResultData> getMainData(@Query("currentPage") String currentPage);

    //메인페이지  - 이름 검색 데이터
    @GET("/main/searchname/{name}")
    Call<ResultFilter> getNameFilterData(@Path("name") String name, @Query("currentPage") String currentPage);

    //메인페이지 - 위치 검색
    @GET("/main/search/{address}/{startdate}/{enddate}")
    Call<ResultFilter> getLocationFilterData(@Path("address") String address,@Path("startdate") String startdate,@Path("enddate") String enddate,@Query("currentPage") String currentPage);

    //상세페이지 데이터
    @GET("/main/{id}")
    Call<DetailResultData> getDetailData(@Path("id") String id,@Query("currentPage") String currentPage);

    //마켓 리뷰
    @POST("/main")
    @Multipart
    Call<ResponseBody> addMarketReview(@Part("market_idx") RequestBody id,
                                       @Part("review_contents") RequestBody review_contents,
                                       @Part MultipartBody.Part file);


    //좋아요
    @PUT("/main/{id}")
    Call<FavoriteResult> requestLikeFavorite(@Path("id") String id);
    //좋아요
    @DELETE("/main/{id}")
    Call<FavoriteResult> requestDeleteFavorite(@Path("id") String id);


    //나의 공간 - 내가 좋아하는 마켓
    @GET("/me/market/good")
    Call<LikeResult> getMyLikeMarketData(@Query("currentPage") String currentPage);

    //나의 공간 - 내가 제보한 마켓
    @GET("/me/market")
    Call<ReportResult> getMyReportMarketData(@Query("currentPage") String currentPage);

    //나의 공간 - 셀러모집
    @GET("/me/saller")
    Call<RecruitResult> getMyRecruitSellerData(@Query("currentPage") String currentPage);

    //셀러모집 리스트
    @GET("/me/market/saller")
    Call<RecruitResult> getRecruitSellerListData(@Query("currentPage") String currentPage);

    //셀러모집 상세보기
    @GET("/me/market/saller/{id}")
    Call<ResultRecruitDetail> getRecrutitDetailData(@Path("id") String id);

    //셀러모집 댓글달기
    @POST("/me/market/saller/{id}/reply")
    Call<ConnectResult> addSellerReview(@Path("id") String id, @Body AddReview AddReview);

    //셀러 모집 등록
    @POST("/me/market/saller")
    @Multipart
    Call<ResponseBody> addSeller(@Part MultipartBody.Part file,
                                 @Part("recruitment_title") RequestBody recruitment_title,
                                 @Part("recruitment_contents") RequestBody recruitment_contents);

    //셀러 삭제
    @DELETE("/me/market/saller/{id}")
    Call<ConnectResult> requestDeleteSeller(@Path("id") String market_id);

    //마켓삭제
    @DELETE("/me/market/{market_id}")
    Call<ConnectResult> requestDeleteMarket(@Path("market_id") String market_id);

    //마켓 제보
    @POST("/me/market")
    @Multipart
    Call<ResponseBody> addMarket(@Part("market_name") RequestBody market_name,
                                 @Part("market_address") RequestBody market_address,
                                 @Part("market_host") RequestBody market_host,
                                 @Part("market_contents") RequestBody market_contents,
                                 @Part("market_tag") RequestBody market_tag,
                                 @Part("market_longitude") RequestBody market_longitude,
                                 @Part("market_latitude") RequestBody market_latitude,
                                 @Part("market_tell") RequestBody market_tell,
                                 @Part("market_startdate") RequestBody market_startdate,
                                 @Part("market_enddate") RequestBody market_enddate,
                                 @Part("market_url") RequestBody market_url,
                                 @Part MultipartBody.Part file);

    //마켓 제보
    @POST("/me/market")
    @Multipart
    Call<ResponseBody> addMarket(@Part("market_name") RequestBody market_name,
                                 @Part("market_address") RequestBody market_address,
                                 @Part("market_host") RequestBody market_host,
                                 @Part("market_contents") RequestBody market_contents,
                                 @Part("market_tag") RequestBody market_tag,
                                 @Part("market_longitude") RequestBody market_longitude,
                                 @Part("market_latitude") RequestBody market_latitude,
                                 @Part("market_tell") RequestBody market_tell,
                                 @Part("market_startdate") RequestBody market_startdate,
                                 @Part("market_enddate") RequestBody market_enddate,
                                 @Part("market_url") RequestBody market_url,
                                 @Part MultipartBody.Part file1,
                                 @Part MultipartBody.Part file2);

    //마켓 제보
    @POST("/me/market")
    @Multipart
    Call<ResponseBody> addMarket(@Part("market_name") RequestBody market_name,
                                 @Part("market_address") RequestBody market_address,
                                 @Part("market_host") RequestBody market_host,
                                 @Part("market_contents") RequestBody market_contents,
                                 @Part("market_tag") RequestBody market_tag,
                                 @Part("market_longitude") RequestBody market_longitude,
                                 @Part("market_latitude") RequestBody market_latitude,
                                 @Part("market_tell") RequestBody market_tell,
                                 @Part("market_startdate") RequestBody market_startdate,
                                 @Part("market_enddate") RequestBody market_enddate,
                                 @Part("market_url") RequestBody market_url,
                                 @Part MultipartBody.Part file1,
                                 @Part MultipartBody.Part file2,
                                 @Part MultipartBody.Part file3);

    //마켓 제보
    @POST("/me/market")
    @Multipart
    Call<ResponseBody> addMarket(@Part("market_name") RequestBody market_name,
                                 @Part("market_address") RequestBody market_address,
                                 @Part("market_host") RequestBody market_host,
                                 @Part("market_contents") RequestBody market_contents,
                                 @Part("market_tag") RequestBody market_tag,
                                 @Part("market_longitude") RequestBody market_longitude,
                                 @Part("market_latitude") RequestBody market_latitude,
                                 @Part("market_tell") RequestBody market_tell,
                                 @Part("market_startdate") RequestBody market_startdate,
                                 @Part("market_enddate") RequestBody market_enddate,
                                 @Part("market_url") RequestBody market_url,
                                 @Part MultipartBody.Part file1,
                                 @Part MultipartBody.Part file2,
                                 @Part MultipartBody.Part file3,
                                 @Part MultipartBody.Part file4);

    //마켓 제보
    @POST("/me/market")
    @Multipart
    Call<ResponseBody> addMarket(@Part("market_name") RequestBody market_name,
                                 @Part("market_address") RequestBody market_address,
                                 @Part("market_host") RequestBody market_host,
                                 @Part("market_contents") RequestBody market_contents,
                                 @Part("market_tag") RequestBody market_tag,
                                 @Part("market_longitude") RequestBody market_longitude,
                                 @Part("market_latitude") RequestBody market_latitude,
                                 @Part("market_tell") RequestBody market_tell,
                                 @Part("market_startdate") RequestBody market_startdate,
                                 @Part("market_enddate") RequestBody market_enddate,
                                 @Part("market_url") RequestBody market_url,
                                 @Part MultipartBody.Part file1,
                                 @Part MultipartBody.Part file2,
                                 @Part MultipartBody.Part file3,
                                 @Part MultipartBody.Part file4,
                                 @Part MultipartBody.Part file5);

    //마켓 제보
    @POST("/me/market")
    @Multipart
    Call<ResponseBody> addMarket(@Part("market_name") RequestBody market_name,
                                 @Part("market_address") RequestBody market_address,
                                 @Part("market_host") RequestBody market_host,
                                 @Part("market_contents") RequestBody market_contents,
                                 @Part("market_tag") RequestBody market_tag,
                                 @Part("market_longitude") RequestBody market_longitude,
                                 @Part("market_latitude") RequestBody market_latitude,
                                 @Part("market_tell") RequestBody market_tell,
                                 @Part("market_startdate") RequestBody market_startdate,
                                 @Part("market_enddate") RequestBody market_enddate,
                                 @Part("market_url") RequestBody market_url,
                                 @Part MultipartBody.Part file1,
                                 @Part MultipartBody.Part file2,
                                 @Part MultipartBody.Part file3,
                                 @Part MultipartBody.Part file4,
                                 @Part MultipartBody.Part file5,
                                 @Part MultipartBody.Part file6);


}

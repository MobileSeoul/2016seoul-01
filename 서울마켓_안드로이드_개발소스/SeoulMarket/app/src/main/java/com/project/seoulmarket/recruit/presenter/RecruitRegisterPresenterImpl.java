package com.project.seoulmarket.recruit.presenter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.google.gson.Gson;
import com.project.seoulmarket.application.GlobalApplication;
import com.project.seoulmarket.recruit.register.RecruitRegisterView;
import com.project.seoulmarket.service.NetworkService;
import com.project.seoulmarket.splash.model.ConnectResult;
import com.project.seoulmarket.splash.model.MessageResult;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by kh on 2016. 10. 26..
 */
public class RecruitRegisterPresenterImpl implements RecruitRegisterPresenter{
    RecruitRegisterView view;
    NetworkService networkService;

    public RecruitRegisterPresenterImpl(RecruitRegisterView view) {
        this.view = view;
        this.networkService = GlobalApplication.getInstance().getNetworkService();
    }

    @Override
    public void addRecruit(String imgUrl, String recruitment_titlea, String recruitment_contents, Intent data) {
        /**
         * 서버로 보낼 파일의 전체 url을 이용해 작업
         */



        //Log.i("myTag","this file'name is "+ photo.getName());

        /**
         * 서버에 사진이외의 텍스트를 보낼 경우를 생각해서 일단 넣어둠
         */
        // add another part within the multipart request

        RequestBody title = RequestBody.create(MediaType.parse("multipart/form-data"), recruitment_titlea);
        RequestBody content = RequestBody.create(MediaType.parse("multipart/form-data"), recruitment_contents);

        /**
         * 사진 업로드하는 부분 // POST방식 이용
         */

        if (imgUrl.equals("")){

            Call<ResponseBody> call = networkService.addSeller(null, title, content);

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()){


                        Gson gson = new Gson();
                        try {

                            String getResult = response.body().string();

                            Log.i("myTag",getResult);


                            ConnectResult example = gson.fromJson(getResult, ConnectResult.class);
                            MessageResult resultMsg = example.result;

                            Log.i("myTag",String.valueOf(resultMsg.message));
//                            MessageResult resultMsg = example.result;
////
                            if(resultMsg.message.equals("Success")){
                                view.successMsg();
                            }
                            else
                                view.errorMsg();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }

                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.e("Upload error:", t.getMessage());

                }
            });
        }
        else{

            /**
             * 리사이징 적용
             */

            Log.i("myTag","resizing");

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.outWidth = 50; //pixels
            options.outHeight = 50; //pixels
            InputStream in = null; // here, you need to get your context.
            try {
                in = view.getNowContext().getContentResolver().openInputStream(data.getData());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            Bitmap bitmap = BitmapFactory.decodeStream(in, null, options);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
            byte[] bitmapdata = baos.toByteArray();


            /**
             *
             */
            File photo = new File(imgUrl);
            RequestBody photoBody = RequestBody.create(MediaType.parse("image/jpg"), bitmapdata);

            // MultipartBody.Part is used to send also the actual file name
            MultipartBody.Part body = MultipartBody.Part.createFormData("image", photo.getName(), photoBody);



            Call<ResponseBody> call = networkService.addSeller(body, title, content);

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()){

                        Gson gson = new Gson();
                        try {
                            String getResult = response.body().string();

                            Log.i("myTag",getResult);


                            ConnectResult example = gson.fromJson(getResult, ConnectResult.class);
                            MessageResult resultMsg = example.result;

                            Log.i("myTag",String.valueOf(resultMsg.message));
//                            MessageResult resultMsg = example.result;
////
                            if(resultMsg.message.equals("Success")){
                                view.successMsg();
                            }
                            else
                                view.errorMsg();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }

                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.e("Upload error:", t.getMessage());

                }
            });
        }


    }
}

package com.project.seoulmarket.report.presenter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.google.gson.Gson;
import com.project.seoulmarket.application.GlobalApplication;
import com.project.seoulmarket.report.view.ReportStepFourView;
import com.project.seoulmarket.service.NetworkService;
import com.project.seoulmarket.splash.model.ConnectResult;
import com.project.seoulmarket.splash.model.MessageResult;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by kh on 2016. 10. 27..
 */
public class ReportStepFourPresenterImpl implements ReportStepFourPresenter {
    ReportStepFourView view;
    NetworkService networkService;

    public ReportStepFourPresenterImpl(ReportStepFourView view) {
        this.view = view;
        networkService = GlobalApplication.getInstance().getNetworkService();
    }


    @Override
    public void addReportMarket(String market_name, String market_address
            , String market_host, String market_contents, String market_tag
            , String market_longitude, String market_latitude, String market_tell
            , String market_startdate, String market_enddate, String market_url
            , ArrayList images, Intent dataList1, Intent dataList2, Intent dataList3
            , Intent dataList4, Intent dataList5, Intent dataList6) {

        Log.i("myTag","report add");

        Log.i("myTag", String.valueOf(images.size()));

        if(images.size() == 1){
            /**
             * 서버로 보낼 파일의 전체 url을 이용해 작업
             */
//            Log.i("myTag",String.valueOf(images.get(0)));

            /**
             * 리사이징 적용
             */

            Log.i("myTag","resizing");

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.outWidth = 50; //pixels
            options.outHeight = 50; //pixels
            InputStream in = null; // here, you need to get your context.
            try {
                in = view.getNowContext().getContentResolver().openInputStream(dataList1.getData());
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


            File photo = new File(String.valueOf(images.get(0)));
            RequestBody photoBody = RequestBody.create(MediaType.parse("image/jpg"), bitmapdata);

            // MultipartBody.Part is used to send also the actual file name
            MultipartBody.Part body = MultipartBody.Part.createFormData("image", photo.getName(), photoBody);

            //Log.i("myTag","this file'name is "+ photo.getName());

            /**
             * 서버에 사진이외의 텍스트를 보낼 경우를 생각해서 일단 넣어둠
             */
            // add another part within the multipart request

            RequestBody name = RequestBody.create(MediaType.parse("multipart/form-data"), market_name);
            RequestBody address = RequestBody.create(MediaType.parse("multipart/form-data"), market_address);
            RequestBody host = RequestBody.create(MediaType.parse("multipart/form-data"), market_host);
            RequestBody content = RequestBody.create(MediaType.parse("multipart/form-data"), market_contents);
            RequestBody tag = RequestBody.create(MediaType.parse("multipart/form-data"), market_tag);
            RequestBody lon = RequestBody.create(MediaType.parse("multipart/form-data"), market_longitude);
            RequestBody lat = RequestBody.create(MediaType.parse("multipart/form-data"), market_latitude);
            RequestBody tell = RequestBody.create(MediaType.parse("multipart/form-data"), market_tell);
            RequestBody startDate = RequestBody.create(MediaType.parse("multipart/form-data"), market_startdate);
            RequestBody endDate = RequestBody.create(MediaType.parse("multipart/form-data"), market_enddate);
            RequestBody hompageUrl = RequestBody.create(MediaType.parse("multipart/form-data"), market_url);

            Call<ResponseBody> call
                    = networkService.addMarket(name,address,host,content,tag,lon,lat,tell,startDate,endDate,hompageUrl,body);

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
                    Log.i("myTag",String.valueOf(t));
                }
            });
        }
        else if(images.size() == 2){
            /**
             * 서버로 보낼 파일의 전체 url을 이용해 작업
             */
//            Log.i("myTag",String.valueOf(images.get(0)));

            File photo;
            RequestBody photoBody;
            MultipartBody.Part body1 = null;
            MultipartBody.Part body2 = null;

            for(int i=0; i<2; i++){
                photo = new File(String.valueOf(images.get(i)));

                if(i==0){

                    /**
                     * 리사이징 적용
                     */

                    Log.i("myTag","resizing");

                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.outWidth = 50; //pixels
                    options.outHeight = 50; //pixels
                    InputStream in = null; // here, you need to get your context.
                    try {
                        in = view.getNowContext().getContentResolver().openInputStream(dataList1.getData());
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    Bitmap bitmap = BitmapFactory.decodeStream(in, null, options);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
                    byte[] bitmapdata = baos.toByteArray();


                    photoBody = RequestBody.create(MediaType.parse("image/jpg"), bitmapdata);
                    body1 = MultipartBody.Part.createFormData("image", photo.getName(), photoBody);
                }
                else if(i==1) {
                    /**
                     * 리사이징 적용
                     */

                    Log.i("myTag","resizing");

                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.outWidth = 50; //pixels
                    options.outHeight = 50; //pixels
                    InputStream in = null; // here, you need to get your context.
                    try {
                        in = view.getNowContext().getContentResolver().openInputStream(dataList2.getData());
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    Bitmap bitmap = BitmapFactory.decodeStream(in, null, options);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
                    byte[] bitmapdata = baos.toByteArray();

                    photoBody = RequestBody.create(MediaType.parse("image/jpg"), bitmapdata);
                    body2 = MultipartBody.Part.createFormData("image", photo.getName(), photoBody);
                }

            }

            // MultipartBody.Part is used to send also the actual file name
//            MultipartBody.Part body = MultipartBody.Part.createFormData("image", photo.getName(), photoBody);



            /**
             * 서버에 사진이외의 텍스트를 보낼 경우를 생각해서 일단 넣어둠
             */
            // add another part within the multipart request

            RequestBody name = RequestBody.create(MediaType.parse("multipart/form-data"), market_name);
            RequestBody address = RequestBody.create(MediaType.parse("multipart/form-data"), market_address);
            RequestBody host = RequestBody.create(MediaType.parse("multipart/form-data"), market_host);
            RequestBody content = RequestBody.create(MediaType.parse("multipart/form-data"), market_contents);
            RequestBody tag = RequestBody.create(MediaType.parse("multipart/form-data"), market_tag);
            RequestBody lon = RequestBody.create(MediaType.parse("multipart/form-data"), market_longitude);
            RequestBody lat = RequestBody.create(MediaType.parse("multipart/form-data"), market_latitude);
            RequestBody tell = RequestBody.create(MediaType.parse("multipart/form-data"), market_tell);
            RequestBody startDate = RequestBody.create(MediaType.parse("multipart/form-data"), market_startdate);
            RequestBody endDate = RequestBody.create(MediaType.parse("multipart/form-data"), market_enddate);
            RequestBody hompageUrl = RequestBody.create(MediaType.parse("multipart/form-data"), market_url);

            Call<ResponseBody> call
                    = networkService.addMarket(name,address,host,content,tag,lon,lat,tell,startDate,endDate,hompageUrl,body1,body2);

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

                }
            });
        }
        else if(images.size() == 3){
            /**
             * 서버로 보낼 파일의 전체 url을 이용해 작업
             */
//            Log.i("myTag",String.valueOf(images.get(0)));

            File photo;
            RequestBody photoBody;
            MultipartBody.Part body1 = null;
            MultipartBody.Part body2 = null;
            MultipartBody.Part body3 = null;

            for(int i=0; i<3; i++){
                photo = new File(String.valueOf(images.get(i)));
                if(i==0){
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.outWidth = 50; //pixels
                    options.outHeight = 50; //pixels
                    InputStream in = null; // here, you need to get your context.
                    try {
                        in = view.getNowContext().getContentResolver().openInputStream(dataList1.getData());
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    Bitmap bitmap = BitmapFactory.decodeStream(in, null, options);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
                    byte[] bitmapdata = baos.toByteArray();

                    photoBody = RequestBody.create(MediaType.parse("image/jpg"), bitmapdata);
                    body1 = MultipartBody.Part.createFormData("image", photo.getName(), photoBody);
                }
                else if(i==1){
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.outWidth = 50; //pixels
                    options.outHeight = 50; //pixels
                    InputStream in = null; // here, you need to get your context.
                    try {
                        in = view.getNowContext().getContentResolver().openInputStream(dataList2.getData());
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    Bitmap bitmap = BitmapFactory.decodeStream(in, null, options);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
                    byte[] bitmapdata = baos.toByteArray();

                    photoBody = RequestBody.create(MediaType.parse("image/jpg"), bitmapdata);
                    body2 = MultipartBody.Part.createFormData("image", photo.getName(), photoBody);
                }
                else if(i==2){
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.outWidth = 50; //pixels
                    options.outHeight = 50; //pixels
                    InputStream in = null; // here, you need to get your context.
                    try {
                        in = view.getNowContext().getContentResolver().openInputStream(dataList3.getData());
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    Bitmap bitmap = BitmapFactory.decodeStream(in, null, options);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
                    byte[] bitmapdata = baos.toByteArray();

                    photoBody = RequestBody.create(MediaType.parse("image/jpg"), bitmapdata);
                    body3 = MultipartBody.Part.createFormData("image", photo.getName(), photoBody);
                }

            }

            // MultipartBody.Part is used to send also the actual file name
//            MultipartBody.Part body = MultipartBody.Part.createFormData("image", photo.getName(), photoBody);



            /**
             * 서버에 사진이외의 텍스트를 보낼 경우를 생각해서 일단 넣어둠
             */
            // add another part within the multipart request

            RequestBody name = RequestBody.create(MediaType.parse("multipart/form-data"), market_name);
            RequestBody address = RequestBody.create(MediaType.parse("multipart/form-data"), market_address);
            RequestBody host = RequestBody.create(MediaType.parse("multipart/form-data"), market_host);
            RequestBody content = RequestBody.create(MediaType.parse("multipart/form-data"), market_contents);
            RequestBody tag = RequestBody.create(MediaType.parse("multipart/form-data"), market_tag);
            RequestBody lon = RequestBody.create(MediaType.parse("multipart/form-data"), market_longitude);
            RequestBody lat = RequestBody.create(MediaType.parse("multipart/form-data"), market_latitude);
            RequestBody tell = RequestBody.create(MediaType.parse("multipart/form-data"), market_tell);
            RequestBody startDate = RequestBody.create(MediaType.parse("multipart/form-data"), market_startdate);
            RequestBody endDate = RequestBody.create(MediaType.parse("multipart/form-data"), market_enddate);
            RequestBody hompageUrl = RequestBody.create(MediaType.parse("multipart/form-data"), market_url);

            Call<ResponseBody> call
                    = networkService.addMarket(name,address,host,content,tag,lon,lat,tell,startDate,endDate,hompageUrl,body1,body2,body3);

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

                }
            });
        }
        else if(images.size() == 4){
            /**
             * 서버로 보낼 파일의 전체 url을 이용해 작업
             */
//            Log.i("myTag",String.valueOf(images.get(0)));

            File photo;
            RequestBody photoBody;
            MultipartBody.Part body1 = null;
            MultipartBody.Part body2 = null;
            MultipartBody.Part body3 = null;
            MultipartBody.Part body4 = null;

            for(int i=0; i<4; i++){
                photo = new File(String.valueOf(images.get(i)));
                if(i==0) {
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.outWidth = 50; //pixels
                    options.outHeight = 50; //pixels
                    InputStream in = null; // here, you need to get your context.
                    try {
                        in = view.getNowContext().getContentResolver().openInputStream(dataList1.getData());
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    Bitmap bitmap = BitmapFactory.decodeStream(in, null, options);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
                    byte[] bitmapdata = baos.toByteArray();

                    photoBody = RequestBody.create(MediaType.parse("image/jpg"), bitmapdata);
                    body1 = MultipartBody.Part.createFormData("image", photo.getName(), photoBody);
                }
                else if(i==1) {
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.outWidth = 50; //pixels
                    options.outHeight = 50; //pixels
                    InputStream in = null; // here, you need to get your context.
                    try {
                        in = view.getNowContext().getContentResolver().openInputStream(dataList2.getData());
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    Bitmap bitmap = BitmapFactory.decodeStream(in, null, options);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
                    byte[] bitmapdata = baos.toByteArray();

                    photoBody = RequestBody.create(MediaType.parse("image/jpg"), bitmapdata);
                    body2 = MultipartBody.Part.createFormData("image", photo.getName(), photoBody);
                }
                else if(i==2){
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.outWidth = 50; //pixels
                    options.outHeight = 50; //pixels
                    InputStream in = null; // here, you need to get your context.
                    try {
                        in = view.getNowContext().getContentResolver().openInputStream(dataList3.getData());
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    Bitmap bitmap = BitmapFactory.decodeStream(in, null, options);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
                    byte[] bitmapdata = baos.toByteArray();

                    photoBody = RequestBody.create(MediaType.parse("image/jpg"), bitmapdata);
                    body3 = MultipartBody.Part.createFormData("image", photo.getName(), photoBody);
                }
                else if(i==3) {
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.outWidth = 50; //pixels
                    options.outHeight = 50; //pixels
                    InputStream in = null; // here, you need to get your context.
                    try {
                        in = view.getNowContext().getContentResolver().openInputStream(dataList4.getData());
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    Bitmap bitmap = BitmapFactory.decodeStream(in, null, options);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
                    byte[] bitmapdata = baos.toByteArray();

                    photoBody = RequestBody.create(MediaType.parse("image/jpg"), bitmapdata);
                    body4 = MultipartBody.Part.createFormData("image", photo.getName(), photoBody);
                }

            }

            // MultipartBody.Part is used to send also the actual file name
//            MultipartBody.Part body = MultipartBody.Part.createFormData("image", photo.getName(), photoBody);



            /**
             * 서버에 사진이외의 텍스트를 보낼 경우를 생각해서 일단 넣어둠
             */
            // add another part within the multipart request

            RequestBody name = RequestBody.create(MediaType.parse("multipart/form-data"), market_name);
            RequestBody address = RequestBody.create(MediaType.parse("multipart/form-data"), market_address);
            RequestBody host = RequestBody.create(MediaType.parse("multipart/form-data"), market_host);
            RequestBody content = RequestBody.create(MediaType.parse("multipart/form-data"), market_contents);
            RequestBody tag = RequestBody.create(MediaType.parse("multipart/form-data"), market_tag);
            RequestBody lon = RequestBody.create(MediaType.parse("multipart/form-data"), market_longitude);
            RequestBody lat = RequestBody.create(MediaType.parse("multipart/form-data"), market_latitude);
            RequestBody tell = RequestBody.create(MediaType.parse("multipart/form-data"), market_tell);
            RequestBody startDate = RequestBody.create(MediaType.parse("multipart/form-data"), market_startdate);
            RequestBody endDate = RequestBody.create(MediaType.parse("multipart/form-data"), market_enddate);
            RequestBody hompageUrl = RequestBody.create(MediaType.parse("multipart/form-data"), market_url);

            Call<ResponseBody> call
                    = networkService.addMarket(name,address,host,content,tag,lon,lat,tell,startDate,endDate,hompageUrl,body1,body2,body3,body4);

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

                }
            });
        }
        else if(images.size() == 5){
            /**
             * 서버로 보낼 파일의 전체 url을 이용해 작업
             */
//            Log.i("myTag",String.valueOf(images.get(0)));

            File photo;
            RequestBody photoBody;
            MultipartBody.Part body1 = null;
            MultipartBody.Part body2 = null;
            MultipartBody.Part body3 = null;
            MultipartBody.Part body4 = null;
            MultipartBody.Part body5 = null;

            for(int i=0; i<5; i++){
                photo = new File(String.valueOf(images.get(i)));
                if(i==0) {
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.outWidth = 50; //pixels
                    options.outHeight = 50; //pixels
                    InputStream in = null; // here, you need to get your context.
                    try {
                        in = view.getNowContext().getContentResolver().openInputStream(dataList1.getData());
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    Bitmap bitmap = BitmapFactory.decodeStream(in, null, options);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
                    byte[] bitmapdata = baos.toByteArray();

                    photoBody = RequestBody.create(MediaType.parse("image/jpg"), bitmapdata);
                    body1 = MultipartBody.Part.createFormData("image", photo.getName(), photoBody);
                }
                else if(i==1) {
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.outWidth = 50; //pixels
                    options.outHeight = 50; //pixels
                    InputStream in = null; // here, you need to get your context.
                    try {
                        in = view.getNowContext().getContentResolver().openInputStream(dataList2.getData());
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    Bitmap bitmap = BitmapFactory.decodeStream(in, null, options);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
                    byte[] bitmapdata = baos.toByteArray();

                    photoBody = RequestBody.create(MediaType.parse("image/jpg"), bitmapdata);
                    body2 = MultipartBody.Part.createFormData("image", photo.getName(), photoBody);
                }
                else if(i==2) {
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.outWidth = 50; //pixels
                    options.outHeight = 50; //pixels
                    InputStream in = null; // here, you need to get your context.
                    try {
                        in = view.getNowContext().getContentResolver().openInputStream(dataList3.getData());
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    Bitmap bitmap = BitmapFactory.decodeStream(in, null, options);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
                    byte[] bitmapdata = baos.toByteArray();

                    photoBody = RequestBody.create(MediaType.parse("image/jpg"), bitmapdata);
                    body3 = MultipartBody.Part.createFormData("image", photo.getName(), photoBody);
                }
                else if(i==3) {
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.outWidth = 50; //pixels
                    options.outHeight = 50; //pixels
                    InputStream in = null; // here, you need to get your context.
                    try {
                        in = view.getNowContext().getContentResolver().openInputStream(dataList4.getData());
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    Bitmap bitmap = BitmapFactory.decodeStream(in, null, options);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
                    byte[] bitmapdata = baos.toByteArray();

                    photoBody = RequestBody.create(MediaType.parse("image/jpg"), bitmapdata);
                    body4 = MultipartBody.Part.createFormData("image", photo.getName(), photoBody);
                }
                else if(i==4) {
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.outWidth = 50; //pixels
                    options.outHeight = 50; //pixels
                    InputStream in = null; // here, you need to get your context.
                    try {
                        in = view.getNowContext().getContentResolver().openInputStream(dataList5.getData());
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    Bitmap bitmap = BitmapFactory.decodeStream(in, null, options);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
                    byte[] bitmapdata = baos.toByteArray();

                    photoBody = RequestBody.create(MediaType.parse("image/jpg"), bitmapdata);
                    body5 = MultipartBody.Part.createFormData("image", photo.getName(), photoBody);
                }

            }

            // MultipartBody.Part is used to send also the actual file name
//            MultipartBody.Part body = MultipartBody.Part.createFormData("image", photo.getName(), photoBody);



            /**
             * 서버에 사진이외의 텍스트를 보낼 경우를 생각해서 일단 넣어둠
             */
            // add another part within the multipart request

            RequestBody name = RequestBody.create(MediaType.parse("multipart/form-data"), market_name);
            RequestBody address = RequestBody.create(MediaType.parse("multipart/form-data"), market_address);
            RequestBody host = RequestBody.create(MediaType.parse("multipart/form-data"), market_host);
            RequestBody content = RequestBody.create(MediaType.parse("multipart/form-data"), market_contents);
            RequestBody tag = RequestBody.create(MediaType.parse("multipart/form-data"), market_tag);
            RequestBody lon = RequestBody.create(MediaType.parse("multipart/form-data"), market_longitude);
            RequestBody lat = RequestBody.create(MediaType.parse("multipart/form-data"), market_latitude);
            RequestBody tell = RequestBody.create(MediaType.parse("multipart/form-data"), market_tell);
            RequestBody startDate = RequestBody.create(MediaType.parse("multipart/form-data"), market_startdate);
            RequestBody endDate = RequestBody.create(MediaType.parse("multipart/form-data"), market_enddate);
            RequestBody hompageUrl = RequestBody.create(MediaType.parse("multipart/form-data"), market_url);

            Call<ResponseBody> call
                    = networkService.addMarket(name,address,host,content,tag,lon,lat,tell,startDate,endDate,hompageUrl,body1,body2,body3,body4,body5);

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

                }
            });
        }
        else if(images.size() == 6){
            /**
             * 서버로 보낼 파일의 전체 url을 이용해 작업
             */
//            Log.i("myTag",String.valueOf(images.get(0)));

            File photo;
            RequestBody photoBody;
            MultipartBody.Part body1 = null;
            MultipartBody.Part body2 = null;
            MultipartBody.Part body3 = null;
            MultipartBody.Part body4 = null;
            MultipartBody.Part body5 = null;
            MultipartBody.Part body6 = null;

            for(int i=0; i<6; i++){
                photo = new File(String.valueOf(images.get(i)));
                if(i==0) {
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.outWidth = 50; //pixels
                    options.outHeight = 50; //pixels
                    InputStream in = null; // here, you need to get your context.
                    try {
                        in = view.getNowContext().getContentResolver().openInputStream(dataList1.getData());
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    Bitmap bitmap = BitmapFactory.decodeStream(in, null, options);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
                    byte[] bitmapdata = baos.toByteArray();

                    photoBody = RequestBody.create(MediaType.parse("image/jpg"), bitmapdata);
                    body1 = MultipartBody.Part.createFormData("image", photo.getName(), photoBody);
                }
                else if(i==1) {
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.outWidth = 50; //pixels
                    options.outHeight = 50; //pixels
                    InputStream in = null; // here, you need to get your context.
                    try {
                        in = view.getNowContext().getContentResolver().openInputStream(dataList2.getData());
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    Bitmap bitmap = BitmapFactory.decodeStream(in, null, options);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
                    byte[] bitmapdata = baos.toByteArray();

                    photoBody = RequestBody.create(MediaType.parse("image/jpg"), bitmapdata);
                    body2 = MultipartBody.Part.createFormData("image", photo.getName(), photoBody);
                }
                else if(i==2) {
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.outWidth = 50; //pixels
                    options.outHeight = 50; //pixels
                    InputStream in = null; // here, you need to get your context.
                    try {
                        in = view.getNowContext().getContentResolver().openInputStream(dataList3.getData());
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    Bitmap bitmap = BitmapFactory.decodeStream(in, null, options);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
                    byte[] bitmapdata = baos.toByteArray();

                    photoBody = RequestBody.create(MediaType.parse("image/jpg"), bitmapdata);
                    body3 = MultipartBody.Part.createFormData("image", photo.getName(), photoBody);
                }
                else if(i==3) {
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.outWidth = 50; //pixels
                    options.outHeight = 50; //pixels
                    InputStream in = null; // here, you need to get your context.
                    try {
                        in = view.getNowContext().getContentResolver().openInputStream(dataList4.getData());
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    Bitmap bitmap = BitmapFactory.decodeStream(in, null, options);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
                    byte[] bitmapdata = baos.toByteArray();

                    photoBody = RequestBody.create(MediaType.parse("image/jpg"), bitmapdata);
                    body4 = MultipartBody.Part.createFormData("image", photo.getName(), photoBody);
                }
                else if(i==4) {
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.outWidth = 50; //pixels
                    options.outHeight = 50; //pixels
                    InputStream in = null; // here, you need to get your context.
                    try {
                        in = view.getNowContext().getContentResolver().openInputStream(dataList5.getData());
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    Bitmap bitmap = BitmapFactory.decodeStream(in, null, options);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
                    byte[] bitmapdata = baos.toByteArray();

                    photoBody = RequestBody.create(MediaType.parse("image/jpg"), bitmapdata);
                    body5 = MultipartBody.Part.createFormData("image", photo.getName(), photoBody);
                }
                else if(i==5) {
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.outWidth = 50; //pixels
                    options.outHeight = 50; //pixels
                    InputStream in = null; // here, you need to get your context.
                    try {
                        in = view.getNowContext().getContentResolver().openInputStream(dataList6.getData());
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    Bitmap bitmap = BitmapFactory.decodeStream(in, null, options);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
                    byte[] bitmapdata = baos.toByteArray();

                    photoBody = RequestBody.create(MediaType.parse("image/jpg"), bitmapdata);
                    body6 = MultipartBody.Part.createFormData("image", photo.getName(), photoBody);
                }

            }

            // MultipartBody.Part is used to send also the actual file name
//            MultipartBody.Part body = MultipartBody.Part.createFormData("image", photo.getName(), photoBody);



            /**
             * 서버에 사진이외의 텍스트를 보낼 경우를 생각해서 일단 넣어둠
             */
            // add another part within the multipart request

            RequestBody name = RequestBody.create(MediaType.parse("multipart/form-data"), market_name);
            RequestBody address = RequestBody.create(MediaType.parse("multipart/form-data"), market_address);
            RequestBody host = RequestBody.create(MediaType.parse("multipart/form-data"), market_host);
            RequestBody content = RequestBody.create(MediaType.parse("multipart/form-data"), market_contents);
            RequestBody tag = RequestBody.create(MediaType.parse("multipart/form-data"), market_tag);
            RequestBody lon = RequestBody.create(MediaType.parse("multipart/form-data"), market_longitude);
            RequestBody lat = RequestBody.create(MediaType.parse("multipart/form-data"), market_latitude);
            RequestBody tell = RequestBody.create(MediaType.parse("multipart/form-data"), market_tell);
            RequestBody startDate = RequestBody.create(MediaType.parse("multipart/form-data"), market_startdate);
            RequestBody endDate = RequestBody.create(MediaType.parse("multipart/form-data"), market_enddate);
            RequestBody hompageUrl = RequestBody.create(MediaType.parse("multipart/form-data"), market_url);

            Call<ResponseBody> call
                    = networkService.addMarket(name,address,host,content,tag,lon,lat,tell,startDate,endDate,hompageUrl,body1,body2,body3,body4,body5,body6);

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
//
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

                }
            });
        }

    }
}

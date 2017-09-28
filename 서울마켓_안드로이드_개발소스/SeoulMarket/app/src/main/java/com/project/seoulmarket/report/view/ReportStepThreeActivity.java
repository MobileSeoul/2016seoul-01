package com.project.seoulmarket.report.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.project.seoulmarket.R;
import com.project.seoulmarket.detail.model.MarkerItem;
import com.project.seoulmarket.dialog.DialogChoose;
import com.project.seoulmarket.report.model.JsonUtil;
import com.project.seoulmarket.report.presenter.FindPresenter;
import com.project.seoulmarket.report.presenter.FindPresenterImpl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class ReportStepThreeActivity extends AppCompatActivity implements OnMapReadyCallback ,StepThreeView{

    @BindView(R.id.inputAddress)
    EditText inputAddress;
    @BindView(R.id.marketAddress)
    TextView marketAddress;


    private GoogleMap mMap;
    View marker_root_view;
    ImageView iv_marker;

    String address ="";
    double lat = 37.5580798;
    double log = 126.9255336;

    Boolean fristCheck = false; // 먼저 주소를 입력한다.

    private FindPresenter presenter;

    DialogChoose dialog_choose;

    InputMethodManager imm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_step_three);

        ButterKnife.bind(this);

        /**
         * actionbar 설정
         */

        if (Build.VERSION.SDK_INT >= 21) {   //상태바 색
            getWindow().setStatusBarColor(Color.parseColor("#FFA700"));
        }

        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // ActionBar의 배경색 변경
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xFFFFFFFF));

        getSupportActionBar().setElevation(0); // 그림자 없애기

        LayoutInflater mInflater = LayoutInflater.from(this);
        View mCustomView = mInflater.inflate(R.layout.actionbar_back_layout, null);

        TextView actionbarTitle = (TextView)mCustomView.findViewById(R.id.mytext);
        actionbarTitle.setText("마켓 제보");
        actionbarTitle.setTypeface(Typeface.createFromAsset(getAssets(),"OTF_B.otf"));
        ImageView backBtn = (ImageView) mCustomView.findViewById(R.id.backBtn);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        getSupportActionBar().setCustomView(mCustomView);
        getSupportActionBar().setDisplayShowCustomEnabled(true);


        /**
         *
         */

        imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        presenter  = new FindPresenterImpl(this);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


    }

    @OnClick(R.id.completeAddress)
    public void nextBtn(){

        if(address.length()!=0){

            Intent getData = getIntent();
            Intent intent = new Intent(getApplicationContext(),ReportStepFourActivity.class);
            intent.putExtra("name",getData.getExtras().getString("name"));
            intent.putExtra("host",getData.getExtras().getString("host"));
            intent.putExtra("content",getData.getExtras().getString("content"));
            intent.putExtra("startDate",getData.getExtras().getString("startDate"));
            intent.putExtra("startTime",getData.getExtras().getString("startTime"));
            intent.putExtra("endDate",getData.getExtras().getString("endDate"));
            intent.putExtra("endTime",getData.getExtras().getString("endTime"));
            intent.putExtra("address",address);
            intent.putExtra("lat",String.valueOf(lat));
            intent.putExtra("log",String.valueOf(log));
            startActivity(intent);
        }
        else
            Toast.makeText(getApplicationContext(),"위치 설정을 해주세요.",Toast.LENGTH_SHORT).show();
    }

    public void getxlatlngToAddress(){
        OkHttpClient client = new OkHttpClient();


//        http://maps.googleapis.com/maps/api/geocode/json?sensor=false&language=ko&latlng=위도,경도
        HttpUrl httpUrl = new HttpUrl.Builder()
                .scheme("http")
                .host("maps.googleapis.com")
                .addPathSegment("maps")
                .addPathSegment("api")
                .addPathSegment("geocode")
                .addPathSegment("json")
                .addQueryParameter("sensor", "false")
                .addQueryParameter("language", "ko")
                .addQueryParameter("latlng", lat+","+log)
                .build();

        Request request = new Request.Builder()
                .url(httpUrl)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {

            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {

                final JSONObject jsonOutput = JsonUtil.getJSONObjectFrom(response.body().string());

                try {
                    String resultStatus = jsonOutput.getString("status");

                    if(resultStatus.equals("OK")){
                        //성공
                        //formatted_address
                        JSONArray resultsData = jsonOutput.getJSONArray("results");

                        String fullAddress = resultsData.getJSONObject(0).getString("formatted_address").replace("대한민국","").trim();
                        Log.i("myTag", "1주소 : "+fullAddress);

                        address = fullAddress;

                        JSONObject geometryData = resultsData.getJSONObject(0).getJSONObject("geometry");
                        JSONObject locationData = geometryData.getJSONObject("location");

                        Log.i("myTag", "1lat : " +  locationData.getString("lat"));
                        Log.i("myTag", "1lng : " +  locationData.getString("lng"));

                        lat = Double.parseDouble(locationData.getString("lat"));
                        log = Double.parseDouble(locationData.getString("lng"));


                        presenter.getResultValue();
                    }
                    else
                        presenter.nullDateValue();

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.i("myTagerror", String.valueOf(e));
                }

            }

        });
    }


    @OnClick(R.id.requestAddress)
    public void getAddressTolatlng(){
        OkHttpClient client = new OkHttpClient();

        HttpUrl httpUrl = new HttpUrl.Builder()
                .scheme("http")
                .host("maps.googleapis.com")
                .addPathSegment("maps")
                .addPathSegment("api")
                .addPathSegment("geocode")
                .addPathSegment("json")
                .addQueryParameter("sensor", "false")
                .addQueryParameter("language", "ko")
                .addQueryParameter("address", String.valueOf(inputAddress.getText()))
                .build();

        Request request = new Request.Builder()
                .url(httpUrl)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {

            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {

                final JSONObject jsonOutput = JsonUtil.getJSONObjectFrom(response.body().string());

                try {
                    String resultStatus = jsonOutput.getString("status");

                    if(resultStatus.equals("OK")){
                        //성공
                        //formatted_address
                        JSONArray resultsData = jsonOutput.getJSONArray("results");

                        String fullAddress = resultsData.getJSONObject(0).getString("formatted_address").replace("대한민국","").trim();
                        Log.i("myTag", "주소 : "+fullAddress);

                        address = fullAddress;

                        JSONObject geometryData = resultsData.getJSONObject(0).getJSONObject("geometry");
                        JSONObject locationData = geometryData.getJSONObject("location");

                        Log.i("myTag", "lat : " +  locationData.getString("lat"));
                        Log.i("myTag", "lng : " +  locationData.getString("lng"));

                        lat = Double.parseDouble(locationData.getString("lat"));
                        log = Double.parseDouble(locationData.getString("lng"));


                        presenter.getResultValue();
                        fristCheck = true;

                        imm.hideSoftInputFromWindow(inputAddress.getWindowToken(), 0);

                    }
                    else
                        requestInputData();

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.i("myTagerror", String.valueOf(e));
                }

            }

        });
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng marketPoint = new LatLng(lat,log);

        mMap.moveCamera(CameraUpdateFactory.newLatLng(marketPoint));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(17), 2000, null);


        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng latLng) {
//                Toast.makeText(getApplicationContext(),"좌표 "+latLng.latitude,Toast.LENGTH_SHORT).show();
                lat = latLng.latitude;
                log = latLng.longitude;

                if(fristCheck == true){


                    WindowManager.LayoutParams registerParams;
                    dialog_choose = new DialogChoose(ReportStepThreeActivity.this, chooseEvent,CancelEvent);

                    registerParams = dialog_choose.getWindow().getAttributes();

                    // Dialog 사이즈 조절 하기
                    registerParams.width = WindowManager.LayoutParams.MATCH_PARENT;
                    registerParams.height = WindowManager.LayoutParams.MATCH_PARENT;
                    dialog_choose.getWindow().setAttributes(registerParams);

                    dialog_choose.show();

                }
            }
        });


    }

    private View.OnClickListener chooseEvent = new View.OnClickListener() {
        public void onClick(View v) {
            dialog_choose.dismiss();
            getxlatlngToAddress();
        }

    };

    private View.OnClickListener CancelEvent = new View.OnClickListener() {
        public void onClick(View v) {
            dialog_choose.dismiss();
        }

    };


    private void setCustomMarkerView() {

        marker_root_view = LayoutInflater.from(this).inflate(R.layout.marker_maps_tag, null);
        iv_marker = (ImageView) marker_root_view.findViewById(R.id.iv_marker);
    }

    private void getSampleMarkerItems() {

        MarkerItem markerItem = new MarkerItem(lat, log, R.drawable.ic_picker);
        addMarker(markerItem, false);

    }

    private Marker addMarker(MarkerItem markerItem, boolean isSelectedMarker) {
        //기존 마커 지우기
        mMap.clear();

        LatLng position = new LatLng(markerItem.getLat(), markerItem.getLon());

        if (isSelectedMarker) {
            iv_marker.setBackgroundResource(R.drawable.ic_picker);
        } else {
            iv_marker.setBackgroundResource(R.drawable.ic_picker);
        }

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(position);
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(this, marker_root_view)));

        return mMap.addMarker(markerOptions);
    }
    // View를 Bitmap으로 변환
    private Bitmap createDrawableFromView(Context context, View view) {

        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);

        return bitmap;
    }


    @Override
    public void changeValue() {
        runOnUiThread(new Runnable() {
            public void run() {

                marketAddress.setText(address);

                LatLng marketPoint = new LatLng(lat,log);

                mMap.moveCamera(CameraUpdateFactory.newLatLng(marketPoint));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(17), 2000, null);

                setCustomMarkerView();
                getSampleMarkerItems();
            }

        });
    }

    @Override
    public void changeNullValue() {
        runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(getApplicationContext(),"검색 결과가 없습니다..",Toast.LENGTH_SHORT).show();
                marketAddress.setText("검색 결과가 없습니다..");
            }

        });
    }

    public void requestInputData() {
        runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(getApplicationContext(),"도로명 주소로 입력해주세요.",Toast.LENGTH_SHORT).show();
                marketAddress.setText("검색 결과가 없습니다..");
            }

        });
    }

}


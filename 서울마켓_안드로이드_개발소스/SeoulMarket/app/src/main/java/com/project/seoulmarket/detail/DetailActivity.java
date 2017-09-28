package com.project.seoulmarket.detail;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.emilsjolander.components.StickyScrollViewItems.StickyScrollView;
import com.matthewtamlin.sliding_intro_screen_library.DotIndicator;
import com.project.seoulmarket.R;
import com.project.seoulmarket.application.GlobalApplication;
import com.project.seoulmarket.detail.maps.MapsActivity;
import com.project.seoulmarket.detail.model.Result;
import com.project.seoulmarket.detail.model.ReviewData;
import com.project.seoulmarket.detail.presenter.DetailPresenter;
import com.project.seoulmarket.detail.presenter.DetailPresenterImpl;
import com.project.seoulmarket.detail.presenter.ViewpagerAdapter;
import com.project.seoulmarket.detail.review.RegisterReviewActivity;
import com.project.seoulmarket.detail.review.ReviewDetailActivity;
import com.project.seoulmarket.dialog.DialogLogin;
import com.project.seoulmarket.login.LoginActivity;
import com.tsengvn.typekit.TypekitContextWrapper;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DetailActivity extends AppCompatActivity implements DetailView{

    @BindView(R.id.pager)
    ViewPager pager;
    @BindView(R.id.pager_indicator)
    DotIndicator indicator;
    @BindView(R.id.basicInfo)
    RelativeLayout basicInfo;
    @BindView(R.id.reviewInfo)
    RelativeLayout reviewInfo;
    @BindView(R.id.contentArea)
    LinearLayout inflatedLayout;
    @BindView(R.id.likehHeart)
    ImageView likeHeart;

    @BindView(R.id.marketName)
    TextView marketName;
    @BindView(R.id.finderName)
    TextView finderName;
    //tag정보
    @BindView(R.id.marketLocation)
    TextView marketLocation;
    @BindView(R.id.likeCount)
    TextView likeCount;
    @BindView(R.id.sticky_scroll)
    StickyScrollView sticky_scroll;
    @BindView(R.id.moveTopBtn)
    ImageView moveTopBtn;
    @BindView(R.id.basicInfoArea)
    View basicInfoArea;
    @BindView(R.id.reviewInfoArea)
    View reviewInfoArea;


    Boolean heartCheck = false;
    ArrayList<String> imgUrl;
    DialogLogin dialog_login;
    DetailPresenter presenter;

    String marketId = "";

    String mName="";
    String mTag = "";
    String mtellNum = "";
    String mStartTime="";
    String mEndTime="";
    String mStartDate="";
    String mEndDate="";
    String mContent="";
    String mHost="";
    String mAddress="";
    String longitude="";
    String latitude="";
    String mFavorite="";
    int heartStat = -1;
    String mURL="";

    ArrayList<ReviewData> reviewDatas;

    int currentPage = 0;
    String pageInfo = "basic";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ButterKnife.bind(this);

        if (Build.VERSION.SDK_INT >= 21) {   //상태바 색
            getWindow().setStatusBarColor(Color.parseColor("#FFA700"));
        }


        presenter = new DetailPresenterImpl(this);
        imgUrl = new ArrayList<String>();
        /**
         * get id
         */

        Intent intent = getIntent();
        marketId = intent.getExtras().getString("market_id");

        Log.i("myTag",marketId);


        /**
         * getData
         */
        reviewDatas = new ArrayList<ReviewData>();
        presenter.getDetail(marketId,String.valueOf(currentPage++));


        /**
         * 스크롤
         */
        sticky_scroll.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {

            @Override
            public void onScrollChanged() {

                int scrollY = sticky_scroll.getScrollY(); //for verticalScrollView

            }
        });

    }

    @Override
    protected void onRestart() {
        super.onRestart();

        //부모 뷰
        LinearLayout listview = (LinearLayout)findViewById(R.id.reviewList);

        imgUrl.clear();

        if(pageInfo.equals("review")) {
            listview.removeAllViews();
        }
        else{
            ;
        }


        reviewDatas.clear();
        currentPage = 0;
        presenter.getDetail(marketId,String.valueOf(currentPage++));

    }

    @OnClick(R.id.likehHeart)
    public void likeHeartEvent(){

        if(GlobalApplication.loginInfo.getBoolean("Login_check", false)) {

            if(heartCheck == false) {
                Log.i("myTag","request like");
                presenter.requestLikeFavorite(marketId);
            }
            else {
                Log.i("myTag","request delete");
                presenter.requestDeleteFavorite(marketId);
            }
        }
        else{
            WindowManager.LayoutParams loginParams;
            dialog_login = new DialogLogin(DetailActivity.this, loginEvent,loginCancelEvent);

            loginParams = dialog_login.getWindow().getAttributes();

            // Dialog 사이즈 조절 하기
            loginParams.width = WindowManager.LayoutParams.MATCH_PARENT;
            loginParams.height = WindowManager.LayoutParams.MATCH_PARENT;
            dialog_login.getWindow().setAttributes(loginParams);

            dialog_login.show();
        }


    }

    @OnClick(R.id.showLocation)
    public void moveLocationPage(){
        /**
         * 넘길때
         * 마켓이름 , 주소 , 위도 , 경도 보내야함
         */

        Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
        intent.putExtra("name",mName);
        intent.putExtra("address",mAddress);
        intent.putExtra("longitude",longitude);
        intent.putExtra("latitude",latitude);

        startActivity(intent);
    }

    @OnClick(R.id.marketReview)
    public void moveRigisterReview(){

        if(GlobalApplication.loginInfo.getBoolean("Login_check", false)) {
            Intent intent = new Intent(getApplicationContext(), RegisterReviewActivity.class);
            intent.putExtra("market_name",mName);
            intent.putExtra("market_id",marketId);
            startActivity(intent);
        }
        else{
            WindowManager.LayoutParams loginParams;
            dialog_login = new DialogLogin(DetailActivity.this, loginEvent,loginCancelEvent);

            loginParams = dialog_login.getWindow().getAttributes();

            // Dialog 사이즈 조절 하기
            loginParams.width = WindowManager.LayoutParams.MATCH_PARENT;
            loginParams.height = WindowManager.LayoutParams.MATCH_PARENT;
            dialog_login.getWindow().setAttributes(loginParams);

            dialog_login.show();
        }


    }

    private View.OnClickListener loginEvent = new View.OnClickListener() {
        public void onClick(View v) {
            dialog_login.dismiss();
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        }

    };

    private View.OnClickListener loginCancelEvent = new View.OnClickListener() {
        public void onClick(View v) {
            dialog_login.dismiss();
        }

    };

    @OnClick(R.id.basicInfo)
    public void changeBasicArea(){

        basicInfoArea.setVisibility(View.VISIBLE);
        reviewInfoArea.setVisibility(View.INVISIBLE);


        pageInfo = "basic";
        moveTopBtn.setVisibility(View.INVISIBLE);

        inflatedLayout.removeAllViews();

        /**
         * 미리 정보를 받아와서 객체로 저장해놓고 있어야함!
         */
        LayoutInflater inflater =  (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        // Inflated_Layout.xml로 구성한 레이아웃을 inflatedLayout 영역으로 확장
        inflater.inflate(R.layout.content_basicinfo, inflatedLayout);

        TextView progressGroup =  (TextView)findViewById(R.id.progressGroup);
        TextView progressDate = (TextView)findViewById(R.id.progressDate);
        TextView progressTime =  (TextView)findViewById(R.id.progressTime);
        TextView marketTell = (TextView)findViewById(R.id.marketTell);
        TextView marketContent =  (TextView)findViewById(R.id.marketContent);
        TextView marketURL = (TextView)findViewById(R.id.marketURL);

        progressGroup.setText(mHost);
        progressDate.setText(mStartDate.replace("-",".")+" ~ "+mEndDate.replace("-","."));
        progressTime.setText(mStartTime+" ~ "+mEndTime);
        marketTell.setText(mtellNum);
        marketContent.setText(mContent);
        marketURL.setText(mURL);


        marketTell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent page = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+mtellNum));
                startActivity(page);
            }
        });

        marketURL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent page = new Intent(Intent.ACTION_VIEW, Uri.parse(mURL));
                startActivity(page);
            }
        });

    }

    @OnClick(R.id.moveTopBtn)
    public void moveTop(){
        sticky_scroll.smoothScrollTo(0,0);

    }


    @OnClick(R.id.reviewInfo)
    public void changeReviewArea(){


        basicInfoArea.setVisibility(View.INVISIBLE);
        reviewInfoArea.setVisibility(View.VISIBLE);

        pageInfo = "review";
        moveTopBtn.setVisibility(View.VISIBLE);

        inflatedLayout.removeAllViews();

        /**
         * 미리 정보를 받아와서 객체로 저장해놓고 있어야함!
         */

        LayoutInflater inflater =  (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        // Inflated_Layout.xml로 구성한 레이아웃을 inflatedLayout 영역으로 확장
        inflater.inflate(R.layout.content_reviewinfo, inflatedLayout);

        //부모 뷰
        LinearLayout listview = (LinearLayout)findViewById(R.id.reviewList);

        LayoutInflater child;
        LinearLayout childLayout;

        TextView reviewNickname;
        TextView reviewDate;
        TextView reviewContent;


        for(int i=0; i<reviewDatas.size();i++){

            child = (LayoutInflater) getSystemService (Context.LAYOUT_INFLATER_SERVICE);
            childLayout = (LinearLayout) child.inflate(R.layout.detail_market_review_item, null);

            reviewNickname =  (TextView)childLayout.findViewById(R.id.reviewNickname);
            reviewDate = (TextView)childLayout.findViewById(R.id.reviewDate);
            reviewContent =  (TextView)childLayout.findViewById(R.id.reviewContent);

            final String nick = reviewDatas.get(i).user_nickname;
            final String uploadTime = reviewDatas.get(i).review_uploadtime;
            final String contents = reviewDatas.get(i).review_contents;
            final String imgUrl = reviewDatas.get(i).review_img;

            reviewNickname.setText(nick);
            reviewDate.setText(uploadTime);
            if (contents.length() > 25)
                reviewContent.setText(contents.substring(0,25)+"...");
            else
                reviewContent.setText(contents);


            childLayout.setId(Integer.parseInt(reviewDatas.get(i).review_idx));
            childLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Log.i("myTag : ", String.valueOf(v.getId()));
                    Intent intent = new Intent(getApplicationContext(), ReviewDetailActivity.class);
                    intent.putExtra("review_id",String.valueOf(v.getId()));
                    intent.putExtra("review_nick",nick);
                    intent.putExtra("review_uploadtime",uploadTime);
                    intent.putExtra("review_content",contents);
                    intent.putExtra("review_img",imgUrl);
                    startActivity(intent);
                }
            });

            listview.addView(childLayout);

        }


        LinearLayout requestReview = (LinearLayout)findViewById(R.id.requestReview);

        if (reviewDatas.size() == 10)
            requestReview.setVisibility(View.VISIBLE);
        else
            requestReview.setVisibility(View.INVISIBLE);



        requestReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.getDetail(marketId,String.valueOf(currentPage++));
            }
        });

    }

    @Override
    public void setDetailData(Result itemDatas) {
        marketName.setText(itemDatas.market_name);


//        marketTag.setText(itemDatas.market_tag.replace(","," "));
//
//        LayoutInflater inflater =  (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        // Inflated_Layout.xml로 구성한 레이아웃을 inflatedLayout 영역으로 확장
//        inflater.inflate(R.layout.content_tag, inflatedLayout);

        //부모 뷰
        LinearLayout tagView = (LinearLayout)findViewById(R.id.tagArea);
        tagView.removeAllViews();

        String[] tempTagList = itemDatas.market_tag.split(",");

        LayoutInflater child;
        LinearLayout childLayout;

        TextView tagContent;

        DisplayMetrics dm = this.getResources().getDisplayMetrics();

        for(int i=0; i<tempTagList.length;i++) {

            child = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            childLayout = (LinearLayout) child.inflate(R.layout.content_tag, null);
            tagContent = (TextView) childLayout.findViewById(R.id.marketTag);

            tagContent.setText(tempTagList[i]);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.bottomMargin = 0;
            params.leftMargin = 0;
            params.rightMargin = convertDpToPx(5, dm);
            params.topMargin = 0;

            childLayout.setLayoutParams(params);

            tagView.addView(childLayout);

        }



        finderName.setText(itemDatas.user_nickname);
        marketLocation.setText(itemDatas.market_address);
        likeCount.setText(itemDatas.market_count);

        for(int i=0;i<itemDatas.image.size();i++){
            imgUrl.add(itemDatas.image.get(i).img_url);
//                Log.i("myTag", itemDatas.image.get(i).img_url);
        }

        ViewpagerAdapter adapter= new ViewpagerAdapter(getLayoutInflater(),imgUrl);

        //ViewPager에 Adapter 설정
        pager.setAdapter(adapter);


        /**
         * 도트 색 지정
         */
        indicator.setSelectedDotColor( Color.parseColor( "#927DFF" ) );
        indicator.setUnselectedDotColor( Color.parseColor( "#727272" ) );

        /**
         * indicator 초기화
         */
        indicator.setNumberOfItems( imgUrl.size());


        /**
         * 스크롤 등으로 다음 페이지로 넘어갈 때 도트도 옮김
         */
        pager.addOnPageChangeListener( new ViewPager.OnPageChangeListener()
        {
            @Override
            public void onPageScrolled( int position, float positionOffset, int positionOffsetPixels )
            {

            }

            @Override
            public void onPageSelected( int position )
            {
                indicator.setSelectedItem( pager.getCurrentItem(), true );
            }

            @Override
            public void onPageScrollStateChanged( int state )
            {

            }
        } );




        mName = itemDatas.market_name;
        mTag = itemDatas.market_tag;
        mtellNum = itemDatas.market_tell;
        mStartTime = itemDatas.market_openTime;
        mEndTime = itemDatas.market_endTime;
        mStartDate = itemDatas.market_startdate;
        mEndDate = itemDatas.market_enddate;
        mContent = itemDatas.market_contents;
        mHost = itemDatas.market_host;
        mAddress = itemDatas.market_address;
        longitude = itemDatas.market_longitude;
        latitude = itemDatas.market_latitude;
        mURL = itemDatas.market_url;


        reviewDatas.addAll(itemDatas.review);

        mFavorite = itemDatas.favorite;
        heartStat =  Integer.valueOf(mFavorite);

        if(heartStat == -1 || heartStat == 0) {
            likeHeart.setImageResource(R.drawable.ic_heart_empty);
            heartCheck = false;
        }
        else{
            likeHeart.setImageResource(R.drawable.ic_heart_fill);
            heartCheck = true;
        }

        changeBasicArea();

    }

    @Override
    public void addReviewData(ArrayList<ReviewData> reviewDatas) {
        /**
         * 미리 정보를 받아와서 객체로 저장해놓고 있어야함!
         */

        LayoutInflater inflater =  (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        // Inflated_Layout.xml로 구성한 레이아웃을 inflatedLayout 영역으로 확장
        inflater.inflate(R.layout.content_reviewinfo, inflatedLayout);

        //부모 뷰
        LinearLayout listview = (LinearLayout)findViewById(R.id.reviewList);

        LayoutInflater child;
        LinearLayout childLayout;

        TextView reviewNickname;
        TextView reviewDate;
        TextView reviewContent;

        for(int i=0; i<reviewDatas.size();i++) {

            child = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            childLayout = (LinearLayout) child.inflate(R.layout.detail_market_review_item, null);

            reviewNickname = (TextView) childLayout.findViewById(R.id.reviewNickname);
            reviewDate = (TextView) childLayout.findViewById(R.id.reviewDate);
            reviewContent = (TextView) childLayout.findViewById(R.id.reviewContent);

            final String nick = reviewDatas.get(i).user_nickname;
            final String uploadTime = reviewDatas.get(i).review_uploadtime;
            final String contents = reviewDatas.get(i).review_contents;
            final String imgUrl = reviewDatas.get(i).review_img;

            reviewNickname.setText(nick);
            reviewDate.setText(uploadTime);
            if (contents.length() > 10)
                reviewContent.setText(contents.substring(0, 10) + "...");
            else
                reviewContent.setText(contents);


            childLayout.setId(Integer.parseInt(reviewDatas.get(i).review_idx));
            childLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Log.i("myTag : ", String.valueOf(v.getId()));
                    Intent intent = new Intent(getApplicationContext(), ReviewDetailActivity.class);
                    intent.putExtra("review_id", String.valueOf(v.getId()));
                    intent.putExtra("review_nick", nick);
                    intent.putExtra("review_uploadtime", uploadTime);
                    intent.putExtra("review_content", contents);
                    intent.putExtra("review_img", imgUrl);
                    startActivity(intent);
                }
            });

            listview.addView(childLayout);

        }

        LinearLayout requestReview = (LinearLayout)findViewById(R.id.requestReview);

        if(reviewDatas.size() == 0)
            Toast.makeText(getApplicationContext(),"더 이상 리뷰가 없습니다.",Toast.LENGTH_SHORT).show();

        if (reviewDatas.size() == 10)
            requestReview.setVisibility(View.VISIBLE);
        else
            requestReview.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

    @Override
    public void setLikeHeart() {
        likeHeart.setImageResource(R.drawable.ic_heart_fill);
        int temp = Integer.valueOf(likeCount.getText().toString());
        likeCount.setText(String.valueOf(++temp));

        heartCheck = true;
    }
    @Override
    public void setDeleteHeart() {
        likeHeart.setImageResource(R.drawable.ic_heart_empty);
        int temp = Integer.valueOf(likeCount.getText().toString());
        likeCount.setText(String.valueOf(--temp));
        heartCheck = false;
    }

    private int convertDpToPx(int dp, DisplayMetrics displayMetrics) {
        float pixels = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, displayMetrics);
        return Math.round(pixels);
    }
}

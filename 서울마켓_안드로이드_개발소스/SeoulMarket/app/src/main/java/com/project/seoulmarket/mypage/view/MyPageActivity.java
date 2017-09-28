package com.project.seoulmarket.mypage.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.login.LoginManager;
import com.gigamole.navigationtabbar.ntb.NavigationTabBar;
import com.kakao.kakaolink.KakaoLink;
import com.kakao.kakaolink.KakaoTalkLinkMessageBuilder;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.kakao.util.KakaoParameterException;
import com.project.seoulmarket.R;
import com.project.seoulmarket.application.GlobalApplication;
import com.project.seoulmarket.detail.DetailActivity;
import com.project.seoulmarket.detail.model.DetailResultData;
import com.project.seoulmarket.detail.model.Result;
import com.project.seoulmarket.dialog.DialogLogout;
import com.project.seoulmarket.dialog.DialogOther;
import com.project.seoulmarket.main.view.MainTabActivity;
import com.project.seoulmarket.mypage.model.LikeDetailData;
import com.project.seoulmarket.mypage.model.RecruitDetailData;
import com.project.seoulmarket.mypage.model.ReportDetailData;
import com.project.seoulmarket.mypage.presenter.MyPageAdapter;
import com.project.seoulmarket.mypage.presenter.MyPagePresenter;
import com.project.seoulmarket.mypage.presenter.MyPagePresenterImpl;
import com.project.seoulmarket.mypage.presenter.MyPageRecruitAdapter;
import com.project.seoulmarket.mypage.presenter.MyPageReportAdapter;
import com.project.seoulmarket.mypage.presenter.MyPageViewPagerAdapter;
import com.project.seoulmarket.recruit.view.RecruitDetailActivity;
import com.project.seoulmarket.service.NetworkService;
import com.tsengvn.typekit.TypekitContextWrapper;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyPageActivity extends AppCompatActivity implements MyPageView{
    @BindView(R.id.pager)
    ViewPager pager;
    @BindView(R.id.ntb)
    NavigationTabBar navigationTabBar;

    MyPageAdapter mLikeAdapter;

    ArrayList<LikeDetailData> likeItemDatas;
    LinearLayoutManager mLikeLayoutManager;
    MyPageReportAdapter mReportAdapter;

    ArrayList<ReportDetailData> reportItemDatas;
    LinearLayoutManager mReportLayoutManager;
    MyPageRecruitAdapter mRecruitAdapter;

    ArrayList<RecruitDetailData> recruitItemDatas;
    LinearLayoutManager mRecruitLayoutManager;

    MyPagePresenter presenter;

    int likeCurrentPage = 0;
    int recruitCurrentPage = 0;
    int reportCurrentPage = 0;

    DialogOther dialog_other;

    DialogLogout dialog_logout;

    int reportPosition;
    String reportMId;


    int recruitPosition;
    String recruitMId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_page);

        if (Build.VERSION.SDK_INT >= 21) {   //상태바 색
            getWindow().setStatusBarColor(Color.parseColor("#FFA700"));
        }

        ButterKnife.bind(this);

        presenter = new MyPagePresenterImpl(this);


        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // ActionBar의 배경색 변경
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xFFFFFFFF));

        getSupportActionBar().setElevation(0); // 그림자 없애기

        LayoutInflater mInflater = LayoutInflater.from(this);
        View mCustomView = mInflater.inflate(R.layout.actionbar_back_layout, null);

        TextView actionbarTitle = (TextView)mCustomView.findViewById(R.id.mytext);
        actionbarTitle.setText("나의 공간");

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



        likeItemDatas = new ArrayList<LikeDetailData>();
        reportItemDatas = new ArrayList<ReportDetailData>();
        recruitItemDatas = new ArrayList<RecruitDetailData>();


        mLikeAdapter = new MyPageAdapter(likeItemDatas, this);
        mReportAdapter = new MyPageReportAdapter(reportItemDatas, this);
        mRecruitAdapter = new MyPageRecruitAdapter(recruitItemDatas, this);

        /**
         * viewpager
         */

        MyPageViewPagerAdapter adapter = new MyPageViewPagerAdapter(getSupportFragmentManager(),this);
        pager.setAdapter(adapter);



        presenter.getMyLikeMarketData(String.valueOf(likeCurrentPage++));
        presenter.getMyReportMarketData(String.valueOf(reportCurrentPage++));
        presenter.getMyRecruitSellerData(String.valueOf(recruitCurrentPage++));

        /**
         *
         */

//        final NavigationTabBar navigationTabBar = (NavigationTabBar) findViewById(R.id.ntb);
        final ArrayList<NavigationTabBar.Model> models = new ArrayList<>();
        final String[] colors = getResources().getStringArray(R.array.default_preview);

        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_myplace_1),
                        Color.parseColor(colors[0])
                ).title("좋아하는 마켓")
                        .badgeTitle("NTB")
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_myplace_2),
                        Color.parseColor(colors[1])
                ).title("제보한 마켓")
                        .badgeTitle("with")
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_myplace_3),
                        Color.parseColor(colors[2])
                ).title("작성한 모집")
                        .badgeTitle("state")
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_myplace_4),
                        Color.parseColor(colors[3])
                ).title("나의 정보")
                        .badgeTitle("icon")
                        .build()
        );

        navigationTabBar.setModels(models);
        navigationTabBar.setViewPager(pager, 0);

        navigationTabBar.setTitleMode(NavigationTabBar.TitleMode.ACTIVE);
        navigationTabBar.setBadgeGravity(NavigationTabBar.BadgeGravity.BOTTOM);
        navigationTabBar.setBadgePosition(NavigationTabBar.BadgePosition.CENTER);
        navigationTabBar.setIsBadged(true);
        navigationTabBar.setIsTitled(true);
        navigationTabBar.setIsTinted(true);
        navigationTabBar.setIsBadgeUseTypeface(true);
        navigationTabBar.setBadgeBgColor(Color.RED);
        navigationTabBar.setBadgeTitleColor(Color.WHITE);
        navigationTabBar.setIsSwiped(true);
        navigationTabBar.setBgColor(Color.WHITE);
        navigationTabBar.setBadgeSize(10);
        navigationTabBar.setTitleSize(15);
        navigationTabBar.setIconSizeFraction((float) 0.5);


    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

    @Override
    protected void onRestart(){
        super.onRestart();

        Log.i("myTag","Restart");

        likeItemDatas.clear();
//        recruitItemDatas.clear();
//        reportItemDatas.clear();


        likeCurrentPage = 0;

        presenter.getMyLikeMarketData(String.valueOf(likeCurrentPage++));
//        presenter.getMyReportMarketData();
//        presenter.getMyRecruitSellerData();


        mLikeAdapter.notifyDataSetChanged();
//        mRecruitAdapter.notifyDataSetChanged();
//        mReportAdapter.notifyDataSetChanged();
    }


    @Override
    public void makeLikeView(LinearLayout view) {

        final RecyclerView recyclerView = (RecyclerView)view.findViewById(R.id.likeMarketList);
        TextView nickNameTitle = (TextView)view.findViewById(R.id.userNickName);
        nickNameTitle.setText(GlobalApplication.loginInfo.getString("nickname", ""));

        /**
         * recyclerview
         */
        //각 item의 크기가 일정할 경우 고정
        recyclerView.setHasFixedSize(true);

        // layoutManager 설정
        mLikeLayoutManager = new LinearLayoutManager(MyPageActivity.this);
        mLikeLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mLikeLayoutManager);

        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int scrollOffset = recyclerView.computeVerticalScrollOffset();
                int scrollExtend = recyclerView.computeVerticalScrollExtent();
                int scrollRange = recyclerView.computeVerticalScrollRange();

                if (scrollOffset + scrollExtend == scrollRange || scrollOffset + scrollExtend - 1 == scrollRange) {

                    presenter.getMyLikeMarketData(String.valueOf(likeCurrentPage++));

                }
            }
        });

        recyclerView.setAdapter(mLikeAdapter);

        ImageView moveTop = (ImageView)view.findViewById(R.id.moveTopBtn);
        moveTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.smoothScrollToPosition(0);
            }
        });

    }


    @Override
    public void makeReportView(LinearLayout view) {

        final RecyclerView recyclerView = (RecyclerView)view.findViewById(R.id.reportMarketList);
        TextView nickNameTitle = (TextView)view.findViewById(R.id.userNickName);

        nickNameTitle.setText(GlobalApplication.loginInfo.getString("nickname", ""));

        /**
         * recyclerview
         */
        //각 item의 크기가 일정할 경우 고정
        recyclerView.setHasFixedSize(true);

        // layoutManager 설정
        mReportLayoutManager = new LinearLayoutManager(MyPageActivity.this);
        mReportLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mReportLayoutManager);

        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int scrollOffset = recyclerView.computeVerticalScrollOffset();
                int scrollExtend = recyclerView.computeVerticalScrollExtent();
                int scrollRange = recyclerView.computeVerticalScrollRange();

                if (scrollOffset + scrollExtend == scrollRange || scrollOffset + scrollExtend - 1 == scrollRange) {

                    presenter.getMyReportMarketData(String.valueOf(reportCurrentPage++));

                }
            }
        });

        recyclerView.setAdapter(mReportAdapter);

        ImageView moveTop = (ImageView)view.findViewById(R.id.moveTopBtn);
        moveTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.smoothScrollToPosition(0);
            }
        });

    }

    @Override
    public void makeRecruitView(LinearLayout view) {

        final RecyclerView recyclerView = (RecyclerView)view.findViewById(R.id.recruitMarketList);
        TextView nickNameTitle = (TextView)view.findViewById(R.id.userNickName);

        nickNameTitle.setText(GlobalApplication.loginInfo.getString("nickname", ""));

        /**
         * recyclerview
         */
        //각 item의 크기가 일정할 경우 고정
        recyclerView.setHasFixedSize(true);

        // layoutManager 설정
        mRecruitLayoutManager = new LinearLayoutManager(MyPageActivity.this);
        mRecruitLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mRecruitLayoutManager);


        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int scrollOffset = recyclerView.computeVerticalScrollOffset();
                int scrollExtend = recyclerView.computeVerticalScrollExtent();
                int scrollRange = recyclerView.computeVerticalScrollRange();

                if (scrollOffset + scrollExtend == scrollRange || scrollOffset + scrollExtend - 1 == scrollRange) {

                    presenter.getMyRecruitSellerData(String.valueOf(recruitCurrentPage++));

                }
            }
        });


        recyclerView.setAdapter(mRecruitAdapter);

        ImageView moveTop = (ImageView)view.findViewById(R.id.moveTopBtn);
        moveTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.smoothScrollToPosition(0);
            }
        });

//        recruitItemDatas.add(new RecruitDetailData("12","프리마켓1 셀러모집","","nick","2016.10.22","1"));

    }

    @Override
    public void makeInfoView(LinearLayout view) {
        TextView nickNameTitle = (TextView)view.findViewById(R.id.userNickName);

        nickNameTitle.setText(GlobalApplication.loginInfo.getString("nickname", ""));
        CircleImageView thumbnail =  (CircleImageView)view.findViewById(R.id.profile_image);
        TextView loginMethod = (TextView)view.findViewById(R.id.loginMethod);
        Button logoutBtn = (Button)view.findViewById(R.id.logoutBtn);

        TextView nickNameTextview = (TextView)view.findViewById(R.id.userNickNameTextview);
        nickNameTextview.setText(GlobalApplication.loginInfo.getString("nickname", ""));

//        Log.i("myTag1",GlobalApplication.loginInfo.getString("thumbnail", ""));

        /**
         * 사용자 프로필이미지 적용
         */
        Glide.with(this)
                .load(GlobalApplication.loginInfo.getString("thumbnail", ""))
                .placeholder(R.drawable.ic_blanket)
                .error(R.drawable.ic_blanket)
                .into(thumbnail);

        loginMethod.setText(GlobalApplication.loginInfo.getString("method", ""));
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //logoutEvent();
                requestLogout();
            }
        });
    }

    @Override
    public void dataNull() {
//        Toast.makeText(getApplicationContext(),"데이터가 없습니다.",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setLikeData(ArrayList<LikeDetailData> getDatas) {
        likeItemDatas.addAll(getDatas);
        mLikeAdapter.notifyDataSetChanged();
    }

    @Override
    public void setReportData(ArrayList<ReportDetailData> getDatas) {
        reportItemDatas.addAll(getDatas);
        mReportAdapter.notifyDataSetChanged();
    }

    @Override
    public void setRecruitData(ArrayList<RecruitDetailData> getDatas) {
        recruitItemDatas.addAll(getDatas);
        mRecruitAdapter.notifyDataSetChanged();
    }


    public void requestLogout(){


        WindowManager.LayoutParams loginParams;
        dialog_logout = new DialogLogout(MyPageActivity.this, logoutComplete, logoutCancel,this);

        loginParams = dialog_logout.getWindow().getAttributes();

        // Dialog 사이즈 조절 하기
        loginParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        loginParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        dialog_logout.getWindow().setAttributes(loginParams);

        dialog_logout.show();

    }

    private View.OnClickListener logoutComplete = new View.OnClickListener() {
        public void onClick(View v) {
            dialog_logout.dismiss();
            logoutEvent();
        }

    };
    private View.OnClickListener logoutCancel = new View.OnClickListener() {
        public void onClick(View v) {
            dialog_logout.dismiss();
        }

    };



    public void logoutEvent(){
        /**
         * 로그인 방법에 따른 로그아웃 메소드/방식이 다름
         */
        if(GlobalApplication.loginInfo.getString("method", "").equals("kakao")){
            //kakao 로그아웃
            UserManagement.requestLogout(new LogoutResponseCallback() {
                @Override
                public void onCompleteLogout() {
                }
            });
        }
        else{
            LoginManager.getInstance().logOut();
        }

        GlobalApplication.editor.putBoolean("Login_check", false);
        GlobalApplication.editor.commit();

        /**
         * 성공시 메인페이지로 이동한다.
         */
        Intent intent = new Intent(this, MainTabActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();

    }


    @Override
    public void moveDetailPage(String mId) {
        Intent intent = new Intent(getApplicationContext(), DetailActivity.class);
        intent.putExtra("market_id",mId);
        startActivity(intent);
    }

    @Override
    public void moveRecruitPage(String mId) {
        Intent intent = new Intent(getApplicationContext(), RecruitDetailActivity.class);
        intent.putExtra("recruitId",mId);
        startActivity(intent);
    }

    @Override
    public void deleteReport(final int position, final String mId) {


        reportPosition = position;
        reportMId = mId;

        WindowManager.LayoutParams loginParams;
        dialog_other = new DialogOther(MyPageActivity.this, deleteReportComplete, deleteCancel,this, position, mId);

        loginParams = dialog_other.getWindow().getAttributes();

        // Dialog 사이즈 조절 하기
        loginParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        loginParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        dialog_other.getWindow().setAttributes(loginParams);

        dialog_other.show();


    }

    @Override
    public void requestDeleteReport(int position, String mId){
        presenter.deleteMyReportMarket(mId);
        reportItemDatas.remove(position);
        mReportAdapter.notifyDataSetChanged();
    }

    public void requestDeleteRecruit(int position, String mId){
        presenter.deleteMyRecruitMarket(mId);
        recruitItemDatas.remove(position);
        mRecruitAdapter.notifyDataSetChanged();
    }

    @Override
    public void cancelDialog() {
        dialog_other.dismiss();
    }

    private View.OnClickListener deleteReportComplete = new View.OnClickListener() {
        public void onClick(View v) {
            dialog_other.dismiss();
            requestDeleteReport(reportPosition,reportMId);
        }

    };

    private View.OnClickListener deleteRecruitComplete = new View.OnClickListener() {
        public void onClick(View v) {
            dialog_other.dismiss();
            requestDeleteRecruit(recruitPosition,recruitMId);
        }

    };


    private View.OnClickListener deleteCancel = new View.OnClickListener() {
        public void onClick(View v) {
            dialog_other.dismiss();
        }
    };


    @Override
    public void deleteRecruit(final int position, final String mId) {


        recruitPosition = position;
        recruitMId = mId;

        WindowManager.LayoutParams loginParams;
        dialog_other = new DialogOther(MyPageActivity.this, deleteRecruitComplete, deleteCancel,this, position, mId);

        loginParams = dialog_other.getWindow().getAttributes();

        // Dialog 사이즈 조절 하기
        loginParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        loginParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        dialog_other.getWindow().setAttributes(loginParams);

        dialog_other.show();


    }


    @Override
    public void sendKakao(String marketId) {
//        Toast.makeText(getApplicationContext(),"kakao " +marketId,Toast.LENGTH_SHORT).show();

        /**
         * 해당하는 market 에 대한 정보 리턴!
         */
        NetworkService networkService = GlobalApplication.getInstance().getNetworkService();

        Call<DetailResultData> detailData = networkService.getDetailData(marketId,"0");
        detailData.enqueue(new Callback<DetailResultData>() {
            @Override
            public void onResponse(Call<DetailResultData> call, Response<DetailResultData> response) {
                if(response.isSuccessful()){
                    Result getDatas = response.body().result;

                    String mName = getDatas.market_name;
                    String imageSrc = getDatas.image.get(0).img_url;
                    String marketUrl = getDatas.market_url;
                    String content = getDatas.market_contents;

                    /**
                     * 카톡 내보내는 곳
                     */
                    try {
                        final KakaoLink kakaoLink = KakaoLink.getKakaoLink(getApplicationContext());
                        final KakaoTalkLinkMessageBuilder kakaoTalkLinkMessageBuilder = kakaoLink.createKakaoTalkLinkMessageBuilder();

                        int width = 150;
                        int height = 150;

                        if(marketUrl.equals("")){
                            kakaoTalkLinkMessageBuilder
                                    .addText(mName)
                                    .addImage(imageSrc, width, height)
                                    .build();
                        }
                        else{
                            kakaoTalkLinkMessageBuilder
                                    .addText(mName)
                                    .addImage(imageSrc, width, height)
                                    .addWebButton("마켓 정보 홈페이지", marketUrl)
                                    .addText(content)
                                    .build();
                        }

                        //메시지 전달
                        kakaoLink.sendMessage(kakaoTalkLinkMessageBuilder, MyPageActivity.this);

                    } catch (KakaoParameterException e) {
                        e.printStackTrace();
                        Log.i("myTag", String.valueOf(e));
                    }

                }
            }

            @Override
            public void onFailure(Call<DetailResultData> call, Throwable t) {
                Log.i("myTag fail",t.toString());
            }
        });


    }

    @Override
    public void successDeleteRecruit() {
        Toast.makeText(getApplicationContext(),"삭제 성공",Toast.LENGTH_SHORT).show();

    }

    @Override
    public void successDeleteReport() {
        Toast.makeText(getApplicationContext(),"삭제 성공",Toast.LENGTH_SHORT).show();

    }

    @Override
    public void NetworkError() {
        Toast.makeText(getApplicationContext(),R.string.error_network,Toast.LENGTH_SHORT).show();
    }


}


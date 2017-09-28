package com.project.seoulmarket.main.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.project.seoulmarket.R;
import com.project.seoulmarket.application.GlobalApplication;
import com.project.seoulmarket.detail.DetailActivity;
import com.project.seoulmarket.dialog.DialogDate;
import com.project.seoulmarket.dialog.DialogLocation;
import com.project.seoulmarket.dialog.DialogLogin;
import com.project.seoulmarket.dialog.DialogName;
import com.project.seoulmarket.login.LoginActivity;
import com.project.seoulmarket.main.model.MarketFilterData;
import com.project.seoulmarket.main.model.MarketFirstData;
import com.project.seoulmarket.main.presenter.CardViewAdapter;
import com.project.seoulmarket.main.presenter.FilterViewAdapter;
import com.project.seoulmarket.main.presenter.MainPresenter;
import com.project.seoulmarket.main.presenter.MainPresenterImpl;
import com.project.seoulmarket.mypage.view.MyPageActivity;
import com.project.seoulmarket.recruit.view.RecruitActivity;
import com.project.seoulmarket.report.view.ReportMarketActivity;
import com.tsengvn.typekit.TypekitContextWrapper;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class MainTabActivity extends AppCompatActivity implements MainView, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.my_recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.mainArea)
    SwipeRefreshLayout mainArea;
    @BindView(R.id.middleUserName)
    TextView middleUserName;
    @BindView(R.id.middleTitle)
    TextView middleTitle;
    @BindView(R.id.findLocationBtn)
    TextView findLocationBtn;
    @BindView(R.id.findDateBtn)
    TextView findDateBtn;
    @BindView(R.id.moveTopBtn)
    ImageView moveTopBtn;

    @BindView(R.id.searchMarketArea)
    LinearLayout searchMarketArea;
    @BindView(R.id.requestInputEdit)
    EditText requestInputEdit;
    @BindView(R.id.requestSearch)
    TextView requestSearch;

    @BindView(R.id.findLocationArea)
    View findLocationArea;

    @BindView(R.id.findDateArea)
    View findDateArea;

    RecyclerView.Adapter mAdapter;
    ArrayList<MarketFirstData> itemDatas;
    LinearLayoutManager mLayoutManager;

    FilterViewAdapter filterAdapter;
    ArrayList<MarketFilterData> filterDatas;
    LinearLayoutManager FilterLayoutManager;


    DialogLocation dialog_location;
    DialogDate dialog_date;
    DialogName dialog_name;
    DialogLogin dialog_login;

    //Back 키 두번 클릭 여부 확인
    private final long FINSH_INTERVAL_TIME = 2000;
    private long backPressedTime = 0;

    //요청한 검색 값
    String chooseName = "";
    String chooseAddress = "*";
    String chooseStartDate = "*";
    String chooseEndDate = "*";

    //페이지 카운터
    int currentPage = 0;
    MainPresenter presenter;

    //검색 필터 사용 중인지
    Boolean setNameFilterPage = false;
    Boolean setFilterPage = false;
    Boolean setNameSearch = false;

    InputMethodManager imm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_tab);

        ButterKnife.bind(this);

        /**
         * toolbar
         */

        if (Build.VERSION.SDK_INT >= 21) {   //상태바 색
            getWindow().setStatusBarColor(Color.parseColor("#FFA700"));
        }



        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

//        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
//        getSupportActionBar().setCustomView(R.layout.actionbar_layout);

        // ActionBar의 배경색 변경
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xFFFFFFFF));

        getSupportActionBar().setElevation(0); // 그림자 없애기

        LayoutInflater mInflater = LayoutInflater.from(this);
        View mCustomView = mInflater.inflate(R.layout.actionbar_layout, null);

        TextView title = (TextView)mCustomView.findViewById(R.id.mytext);
        title.setTypeface(Typeface.createFromAsset(getAssets(),"OTF_B.otf"));

        ImageView findNameBtn = (ImageView) mCustomView.findViewById(R.id.findNameBtn);

        findNameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(setNameSearch){
                    findLocationBtn.setClickable(true);
                    findDateBtn.setClickable(true);
                    searchMarketArea.setVisibility(View.INVISIBLE);
                    setNameSearch = false;

                }
                else{
                    requestInputEdit.setText("");
                    findLocationBtn.setClickable(false);
                    findDateBtn.setClickable(false);
                    searchMarketArea.setVisibility(View.VISIBLE);
                    setNameSearch = true;

                }

                //findName();


            }
        });


        getSupportActionBar().setCustomView(mCustomView);
        getSupportActionBar().setDisplayShowCustomEnabled(true);


        imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);

        mainArea.setOnRefreshListener(this);
        mainArea.setColorSchemeResources(R.color.mainYellow, R.color.white);


        /**
         * presenter 초기화
         */

        presenter = new MainPresenterImpl(this);

        /**
         * 로그인 유무 체크
         */
        //loginCheck();

        /**
         * FCM
         */

        //추가한 라인
        FirebaseMessaging.getInstance().subscribeToTopic("news");
        FirebaseInstanceId.getInstance().getToken();



        /**
         * recyclerview
         */

        itemDatas = new ArrayList<MarketFirstData>();
        filterDatas = new ArrayList<MarketFilterData>();
        mAdapter = new CardViewAdapter(itemDatas,this);
        recyclerView.setAdapter(mAdapter);

        //각 item의 크기가 일정할 경우 고정
        recyclerView.setHasFixedSize(true);

        // layoutManager 설정
        mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mLayoutManager);

        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
                int firstPos=mLayoutManager.findFirstCompletelyVisibleItemPosition();

                if (firstPos>0)
                {
                    mainArea.setEnabled(false);
                }
                else {
                    mainArea.setEnabled(true);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int scrollOffset = recyclerView.computeVerticalScrollOffset();
                int scrollExtend = recyclerView.computeVerticalScrollExtent();
                int scrollRange = recyclerView.computeVerticalScrollRange();

                if (scrollOffset + scrollExtend == scrollRange || scrollOffset + scrollExtend - 1 == scrollRange) {

                    presenter.requestMainData(String.valueOf(currentPage++));
                }
            }

        });



        /**
         * 데이터 삽입
         */
        //String idx; String address; String state; String image; String marketname;
        presenter.requestMainData(String.valueOf(currentPage++));

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @OnClick(R.id.moveTopBtn)
    public void moveTop(){
        recyclerView.smoothScrollToPosition(0);
    }

    //나의 공간
    @OnClick(R.id.myPage)
    public void moveMyPage(){

        if(GlobalApplication.loginInfo.getBoolean("Login_check", false)) {

            Intent intent = new Intent(getApplicationContext(), MyPageActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        else{
            WindowManager.LayoutParams loginParams;
            dialog_login = new DialogLogin(MainTabActivity.this, loginEvent,loginCancelEvent);

            loginParams = dialog_login.getWindow().getAttributes();

            // Dialog 사이즈 조절 하기
            loginParams.width = WindowManager.LayoutParams.MATCH_PARENT;
            loginParams.height = WindowManager.LayoutParams.MATCH_PARENT;
            dialog_login.getWindow().setAttributes(loginParams);

            dialog_login.show();

        }


    }

    //마켓 제보
    @OnClick(R.id.noitfyMarket)
    public void moveNoitfyMarket(){

        if(GlobalApplication.loginInfo.getBoolean("Login_check", false)) {

            Intent intent = new Intent(getApplicationContext(), ReportMarketActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        else{
            WindowManager.LayoutParams loginParams;
            dialog_login = new DialogLogin(MainTabActivity.this, loginEvent,loginCancelEvent);

            loginParams = dialog_login.getWindow().getAttributes();

            // Dialog 사이즈 조절 하기
            loginParams.width = WindowManager.LayoutParams.MATCH_PARENT;
            loginParams.height = WindowManager.LayoutParams.MATCH_PARENT;
            dialog_login.getWindow().setAttributes(loginParams);

            dialog_login.show();

        }

    }

    //셀러 모집
    @OnClick(R.id.recruitSeller)
    public void moveRecruitSeller(){
        Intent intent = new Intent(getApplicationContext(), RecruitActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }


    public void findName(){
        WindowManager.LayoutParams params;

        dialog_name = new DialogName(MainTabActivity.this, getNameEvent);

        params = dialog_name.getWindow().getAttributes();

        // Dialog 사이즈 조절 하기
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;


        dialog_name.getWindow().setAttributes(params);

        dialog_name.show();

    }

    @OnClick(R.id.findLocationBtn)
    public void findLocation(){

        findLocationArea.setVisibility(View.VISIBLE);

        WindowManager.LayoutParams params;

        dialog_location = new DialogLocation(MainTabActivity.this, getLocationEvent, this);

        params = dialog_location.getWindow().getAttributes();

        // Dialog 사이즈 조절 하기
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        dialog_location.getWindow().setAttributes(params);

        dialog_location.show();
    }

    @OnClick(R.id.findDateBtn)
    public void findDate(){

        findDateArea.setVisibility(View.VISIBLE);

        WindowManager.LayoutParams params;

        dialog_date = new DialogDate(MainTabActivity.this,this,getDateEvent);

        params = dialog_date.getWindow().getAttributes();

        // Dialog 사이즈 조절 하기
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        dialog_date.getWindow().setAttributes(params);

        dialog_date.show();
    }

    private View.OnClickListener getLocationEvent = new View.OnClickListener() {
        public void onClick(View v) {

            if(dialog_location.giveAddress() == "null") {
                Toast.makeText(getApplicationContext(),"지역을 선택해주세요.",Toast.LENGTH_SHORT).show();
            }
            else {
                chooseAddress = dialog_location.giveAddress();

//                Toast.makeText(getApplicationContext(),chooseAddress,Toast.LENGTH_SHORT).show();

                findLocationBtn.setText(chooseAddress);
                middleTitle.setText("검색된 마켓");

                dialog_location.dismiss();
                findLocationArea.setVisibility(View.INVISIBLE);


                if(chooseAddress.equals("전체"))
                    chooseAddress = "*";

                setFilterPage = true;
                setNameFilterPage = false;

                currentPage = 0;
                recyclerView.removeAllViews();

                filterDatas.clear();
                presenter.requestLocationFilterData(chooseAddress,chooseStartDate,chooseEndDate,String.valueOf(currentPage++));

            }
        }

    };

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


    private View.OnClickListener getDateEvent = new View.OnClickListener() {
        public void onClick(View v) {

            if(dialog_date.getStartDate() == null || dialog_date.getEndDate() == null ){
                Toast.makeText(getApplicationContext(),"기간을 선택해주세요.",Toast.LENGTH_SHORT).show();
            }
            else{
                chooseStartDate = dialog_date.getStartDate();
                chooseEndDate = dialog_date.getEndDate();

                findDateBtn.setText(chooseStartDate.replace("-",".")+"~"+chooseEndDate.replace("-","."));
                middleTitle.setText("검색된 마켓");

//                Toast.makeText(getApplicationContext(),chooseStartDate + " ~ "+chooseEndDate,Toast.LENGTH_SHORT).show();
                dialog_date.dismiss();
                findDateArea.setVisibility(View.INVISIBLE);


                setFilterPage = true;
                setNameFilterPage = false;

                currentPage = 0;
                recyclerView.removeAllViews();

                filterDatas.clear();
                presenter.requestDateFilterData(chooseAddress,chooseStartDate,chooseEndDate,String.valueOf(currentPage++));

            }

        }

    };

    private View.OnClickListener getNameEvent = new View.OnClickListener() {
        public void onClick(View v) {

            if (dialog_name.getName().length() == 0 ){
                Toast.makeText(getApplicationContext(),"검색할 마켓을 입력해주세요.",Toast.LENGTH_SHORT).show();
            }
            else{
                dialog_name.dismiss();

                middleUserName.setText('"'+dialog_name.getName()+'"');
                middleTitle.setText(" 마켓정보");
                findLocationBtn.setText("위치별검색");
                findDateBtn.setText("날짜별검색");
                chooseAddress = "*";
                chooseStartDate ="*";
                chooseEndDate = "*";


                setFilterPage = true;

                currentPage = 0;
                recyclerView.removeAllViews();

                filterDatas.clear();
                presenter.requestNameFilterData(dialog_name.getName(), String.valueOf(currentPage++));

            }

        }

    };

    @OnClick(R.id.requestSearch)
    public void searchName(){
        if (requestInputEdit.getText().length() == 0 ){
            Toast.makeText(getApplicationContext(),"검색할 마켓을 입력해주세요.",Toast.LENGTH_SHORT).show();
        }
        else{

            chooseName = requestInputEdit.getText().toString();
            middleUserName.setText('"'+chooseName+'"');
            middleTitle.setText(" 마켓정보");
            findLocationBtn.setText("위치별검색");
            findDateBtn.setText("날짜별검색");
            chooseAddress = "*";
            chooseStartDate ="*";
            chooseEndDate = "*";

            setFilterPage = true;

            currentPage = 0;
            recyclerView.removeAllViews();

            setNameFilterPage = true;

            filterDatas.clear();
            presenter.requestNameFilterData(chooseName, String.valueOf(currentPage++));

            imm.hideSoftInputFromWindow(requestInputEdit.getWindowToken(), 0);
        }
    }

    @Override
    public void onBackPressed() {
        long tempTime        = System.currentTimeMillis();
        long intervalTime    = tempTime - backPressedTime;

        //검색 필터 중이면...다시 처음 화면으로 돌리기

        if(setNameSearch){
            searchMarketArea.setVisibility(View.INVISIBLE);
            setNameSearch = false;
            findLocationBtn.setClickable(true);
            findDateBtn.setClickable(true);
        }
        else{
            if(setFilterPage == true){
                setFilterPage = false;
                setNameFilterPage = false;

                currentPage = 0;

                findLocationBtn.setText("위치별검색");
                findDateBtn.setText("날짜별검색");
                middleUserName.setText("");
                middleTitle.setText("인기 마켓 순위");
                chooseAddress = "*";
                chooseStartDate ="*";
                chooseEndDate = "*";

                mAdapter = new CardViewAdapter(itemDatas,this);
                recyclerView.setAdapter(mAdapter);

                //각 item의 크기가 일정할 경우 고정
                recyclerView.setHasFixedSize(true);

                // layoutManager 설정
                mLayoutManager = new LinearLayoutManager(this);
                mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                recyclerView.setLayoutManager(mLayoutManager);

                recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {

                    @Override
                    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
                        int firstPos=mLayoutManager.findFirstCompletelyVisibleItemPosition();

                        if (firstPos>0)
                        {
                            mainArea.setEnabled(false);
                        }
                        else {
                            mainArea.setEnabled(true);
                        }
                    }

                    @Override
                    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                        int scrollOffset = recyclerView.computeVerticalScrollOffset();
                        int scrollExtend = recyclerView.computeVerticalScrollExtent();
                        int scrollRange = recyclerView.computeVerticalScrollRange();


                        if (scrollOffset + scrollExtend == scrollRange || scrollOffset + scrollExtend - 1 == scrollRange) {

                            presenter.requestMainData(String.valueOf(currentPage++));

                        }
                    }
                });


                itemDatas.clear();
                presenter.requestMainData(String.valueOf(currentPage++));

            }
            else{
                /**
                 * Back키 두번 연속 클릭 시 앱 종료
                 */
                if ( 0 <= intervalTime && FINSH_INTERVAL_TIME >= intervalTime ) {
                    super.onBackPressed();
                }
                else {
                    backPressedTime = tempTime;
                    Toast.makeText(getApplicationContext(),"뒤로 가기 키을 한번 더 누르시면 종료됩니다.",Toast.LENGTH_SHORT).show();
                }
            }
        }


    }


    @Override
    public void checkDate() {
        Toast.makeText(getApplicationContext(),"날짜를 다시 선택해주세요",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void prevousStart() {
        Toast.makeText(getApplicationContext(),"선택 날짜는 현재 날짜보다 과거일 수 없습니다.",Toast.LENGTH_SHORT).show();

    }

    @Override
    public void prevousEnd() {
        Toast.makeText(getApplicationContext(),"마지막 날짜는 시작 날짜보다 과거일 수 없습니다.",Toast.LENGTH_SHORT).show();
    }
    @Override
    public void nextEnd() {
        Toast.makeText(getApplicationContext(),"시작 날짜는 마지막 날짜보다 미래일 수 없습니다.",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void DataNull() {
        Toast.makeText(getApplicationContext(),"더 이상 데이터가 없습니다.",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void FilterDataNull() {
        Toast.makeText(getApplicationContext(),"검색 결과가 없습니다.",Toast.LENGTH_SHORT).show();
        recyclerView.removeAllViews();

        filterAdapter = new FilterViewAdapter(filterDatas, this);
        recyclerView.setAdapter(filterAdapter);
    }

    @Override
    public void moveDetailPage(String id) {
        Intent intent = new Intent(getApplicationContext(), DetailActivity.class);
        intent.putExtra("market_id",id);
        startActivity(intent);
    }

    @Override
    public void firstSetData(ArrayList<MarketFirstData> getDatas) {
        Log.i("myTag",String.valueOf(getDatas.size()));

        itemDatas.addAll(getDatas);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void filterSetData(String fName, ArrayList<MarketFilterData> getDatas) {

        Log.i("myTag",String.valueOf(currentPage));

        if(currentPage == 1){

            filterDatas.addAll(getDatas);
            filterAdapter = new FilterViewAdapter(filterDatas, this);
            recyclerView.setAdapter(filterAdapter);

            recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
                    int firstPos=mLayoutManager.findFirstCompletelyVisibleItemPosition();

                    if (firstPos>0)
                    {
                        mainArea.setEnabled(false);
                    }
                    else {
                        mainArea.setEnabled(true);
                    }
                }

                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    int scrollOffset = recyclerView.computeVerticalScrollOffset();
                    int scrollExtend = recyclerView.computeVerticalScrollExtent();
                    int scrollRange = recyclerView.computeVerticalScrollRange();

                    if (scrollOffset + scrollExtend == scrollRange || scrollOffset + scrollExtend - 1 == scrollRange) {
//                    Toast.makeText(getApplicationContext(),"get",Toast.LENGTH_SHORT).show();
                        presenter.requestNameFilterData(middleUserName.getText().toString(), String.valueOf(currentPage++));
//                    Toast.makeText(getApplicationContext(),String.valueOf(currentPage),Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        else{
            filterDatas.addAll(getDatas);
            filterAdapter.notifyDataSetChanged();
        }


    }

    @Override
    public void filterSetData(final String address, final String startDate, final String endDate, ArrayList<MarketFilterData> getDatas) {


        if(currentPage == 1){

            filterDatas.addAll(getDatas);
            filterAdapter = new FilterViewAdapter(filterDatas, this);

            recyclerView.setAdapter(filterAdapter);

            recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
                    int firstPos=mLayoutManager.findFirstCompletelyVisibleItemPosition();

                    if (firstPos>0)
                    {
                        mainArea.setEnabled(false);
                    }
                    else {
                        mainArea.setEnabled(true);
                    }
                }

                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    int scrollOffset = recyclerView.computeVerticalScrollOffset();
                    int scrollExtend = recyclerView.computeVerticalScrollExtent();
                    int scrollRange = recyclerView.computeVerticalScrollRange();

                    if (scrollOffset + scrollExtend == scrollRange || scrollOffset + scrollExtend - 1 == scrollRange) {
                        Log.i("myTag","dfd");
                        presenter.requestLocationFilterData(chooseAddress,chooseStartDate,chooseEndDate,String.valueOf(currentPage++));
                    }
                }
            });
        }
        else{
            filterDatas.addAll(getDatas);
            filterAdapter.notifyDataSetChanged();

        }


    }

    @Override
    public void cancelLocationDialog() {
        dialog_location.cancel();
        findLocationArea.setVisibility(View.INVISIBLE);
    }

    @Override
    public void cancelDateDialog() {
        dialog_date.cancel();
        findDateArea.setVisibility(View.INVISIBLE);
    }


    @Override
    public void onRefresh() {
//        Toast.makeText(getApplicationContext(),"fresh",Toast.LENGTH_SHORT).show();

        mainArea.setRefreshing(true);
        //3초후에 해당 adapoter를 갱신하고 동글뱅이를 닫아준다.setRefreshing(false);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                //필터 검색인 상태
                if(setFilterPage){
                    currentPage = 0;
                    filterDatas.clear();

                    //이름검색일 경우
                    if(setNameFilterPage){

                        presenter.requestNameFilterData(dialog_name.getName(), String.valueOf(currentPage++));
                    }
                    //지역,날짜일 경우
                    else{
                        presenter.requestLocationFilterData(chooseAddress,chooseStartDate,chooseEndDate,String.valueOf(currentPage++));

                    }
                }
                else{
                    itemDatas.clear();
                    currentPage = 0;
                    presenter.requestMainData(String.valueOf(currentPage++));

                }

                mainArea.setRefreshing(false);
            }
        },1500);
    }
}

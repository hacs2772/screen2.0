package com.example.screen20;


import android.app.AlarmManager;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class routescreen extends AppCompatActivity {
    private static final int JOB_KEY = 101;
    TextView textView;
    TextView textView2;
    TextView textView3;
    TextView detail0;
    TextView detail1;
    TextView detail2;
    TextView detail3;
    TextView detail4;
    TextView detail5;
    TextView detail6;
    TextView arrow1;
    TextView arrow2;
    TextView arrow3;
    TextView arrow4;
    TextView arrow5;
    TableLayout detail;
    Button button;
    private SuttleDao suttleDao;
    private FincityDao fincityDao;
    private FoutcityDao foutcityDao;
    private DayTimeDao dayTimeDao;
    private FinallincityDao finallincityDao;
    private FinalloutcityDao finalloutcityDao;
    private Suttle suttle = new Suttle();
    private DayTime dayTime = new DayTime();
    private Fincity fincity = new Fincity();
    private Foutcity foutcity = new Foutcity();
    private Finallincity finallincity = new Finallincity();
    private Finalloutcity finalloutcity = new Finalloutcity();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        View();
        // JobScheduler ??????
//        initJobScheduler();

    }

    void View() {
        String nowDay = "";
        switch (Calendar.getInstance().get(Calendar.DAY_OF_WEEK)) {
            case 1:
                nowDay = "???";
                break;
            case 2:
                nowDay = "???";
                break;
            case 3:
                nowDay = "???";
                break;
            case 4:
                nowDay = "???";
                break;
            case 5:
                nowDay = "???";
                break;
            case 6:
                nowDay = "???";
                break;
            case 7:
                nowDay = "???";
                break;
        }
        FinallincityDatabase finallincityDatabase = Room.databaseBuilder(getApplicationContext(), FinallincityDatabase.class, "finallincity.db")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();
        finallincityDao = finallincityDatabase.finallincityDao();
        FinalloutcityDatabase database1 = Room.databaseBuilder(getApplicationContext(), FinalloutcityDatabase.class, "finalloutcity.db")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();
        finalloutcityDao = database1.finalloutcityDao();
        List<Finallincity> finallincities = finallincityDao.getData(nowDay);
        List<Finalloutcity> finalloutcities = finalloutcityDao.getData(nowDay);
        textView = (TextView) findViewById(R.id.textView);
        textView2 = (TextView) findViewById(R.id.textView2);
        textView3 = (TextView) findViewById(R.id.textView3);
        TableLayout layout = (TableLayout) findViewById(R.id.detail);
        detail0 = (TextView) findViewById(R.id.detail0);
        detail1 = (TextView) findViewById(R.id.detail1);
        detail2 = (TextView) findViewById(R.id.detail2);
        detail3 = (TextView) findViewById(R.id.detail3);
        detail4 = (TextView) findViewById(R.id.detail4);
        detail5 = (TextView) findViewById(R.id.detail5);
        detail6 = (TextView) findViewById(R.id.detail6);
        arrow1 = (TextView) findViewById(R.id.arrow1);
        arrow2 = (TextView) findViewById(R.id.arrow2);
        arrow3 = (TextView) findViewById(R.id.arrow3);
        arrow4 = (TextView) findViewById(R.id.arrow4);
        arrow5 = (TextView) findViewById(R.id.arrow5);
        Button button = (Button) findViewById(R.id.bu);
        if (finallincities.get(0).getArrtime() == (null)) {
            Log.i("arrtime", "null");
        }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (layout.getVisibility() == View.INVISIBLE) {
                    button.setText("\uD83D\uDD3A");
                    layout.setVisibility(View.VISIBLE);
                } else {
                    button.setText("\uD83D\uDD3B");
                    layout.setVisibility(View.INVISIBLE);
                }
            }
        });
        if (finallincities.size() > 0 && finalloutcities.size() > 0) {
            if (finallincities.get(0).getTotalTime() > finalloutcities.get(0).getTotalTime()) {
                try {
                    DecimalFormat df = new DecimalFormat("###,###");
                    String money = df.format(finalloutcities.get(0).getFare());
                    int hour = (int) Math.floor((double) finalloutcities.get(0).getTotalTime() / 60);
                    int minute = finalloutcities.get(0).getTotalTime() % 60;
                    JSONObject jsonObject = new JSONObject(finalloutcities.get(0).getFirstpath());
                    JSONArray jsonArray = (JSONArray) jsonObject.get("subpath");
                    JSONObject object = (JSONObject) jsonArray.getJSONObject(1);
                    JSONObject jsonObject1 = new JSONObject(finalloutcities.get(0).getSecondpath());
                    JSONObject jsonObject2 = new JSONObject(finalloutcities.get(0).getThirdpath());
                    JSONArray jsonArray1 = (JSONArray) jsonObject2.get("subpath");
                    JSONObject object1 = (JSONObject) jsonArray1.getJSONObject(1);
                    textView.setText(finalloutcities.get(0).getName());
                    textView2.setText("???????????? : " + finalloutcities.get(0).getSchedule());
                    if(object.get("type").equals("??????")){
                        textView3.setText("??????????????? : " + finalloutcities.get(0).getStart() + " (" + object.get("name") + " ??????)" + "    ??? " + finalloutcities.get(0).getArrtime() + " ???");
                    } else {
                        textView3.setText("??????????????? : " + finalloutcities.get(0).getStart() + " (" + object.get("name") + " ???)");
                    }
                    detail0.setText("???????????? : " + hour + "?????? " + minute + "???" + "    ?????? : " + money + "???");
                    if(object.get("type").equals("??????")){
                        detail1.setText(jsonObject.get("start").toString() + " (" + object.get("name") + " ??????)");
                    } else {
                        detail1.setText(jsonObject.get("start").toString() + " (" + object.get("name") + " ???)");
                    }
                    arrow1.setText("???");
                    detail2.setText(jsonObject.get("end").toString());
                    arrow2.setText("???");
                    detail3.setText(jsonObject1.get("start").toString() + " (" + finalloutcities.get(0).getName() + ")");
                    arrow3.setText("???");
                    detail4.setText(jsonObject1.get("end").toString());
                    arrow4.setText("???");
                    detail5.setText(jsonObject2.get("start").toString() + " (" + object1.get("name") + " ??????)");
                    arrow5.setText("???");
                    detail6.setText(jsonObject2.get("end").toString());
                } catch (Exception e) {
                    e.toString();
                }
            } else {
                try {
                    JSONArray jsonArray = new JSONArray(finallincities.get(0).getSubpath());
                    int i = jsonArray.length();
                    DecimalFormat df = new DecimalFormat("###,###");
                    String money = df.format(finallincities.get(0).getFare());
                    int hour = (int) Math.floor((double) finallincities.get(0).getTotalTime() / 60);
                    int minute = finallincities.get(0).getTotalTime() % 60;
                    textView.setText(finallincities.get(0).getName());
                    textView2.setText("???????????? : " + finallincities.get(0).getSchedule());
                    if (jsonArray.getJSONObject(1).getString("type").equals("??????")) {
                        textView3.setText("??????????????? : " + finallincities.get(0).getStart() + " (" + finallincities.get(0).getName() + " ??????)" + "    ??? " + finallincities.get(0).getArrtime()+" ???");
                    } else {
                        textView3.setText("??????????????? : " + finallincities.get(0).getStart() + "??? (" + finallincities.get(0).getName() + ")");
                    }
                    detail0.setText("???????????? : " + hour + "?????? " + minute + "???" + "     ?????? : " + money + "???");
                    if (jsonArray.getJSONObject(1).getString("type").equals("?????????")) {
                        detail1.setText(jsonArray.getJSONObject(1).getString("start") + "??? (" + finallincities.get(0).getName() + ")");
                    } else {
                        detail1.setText(jsonArray.getJSONObject(1).getString("start") + " (" + finallincities.get(0).getName() + " ??????)");
                    }
                    arrow1.setText("???");
                    detail2.setText(jsonArray.getJSONObject(1).getString("end"));
                    if (i > 4) {
                        arrow2.setText("???");
                        if (jsonArray.getJSONObject(2).get("type").equals("?????????")) {
                            detail3.setText(jsonArray.getJSONObject(3).getString("start"));
                        } else {
                            detail3.setText(jsonArray.getJSONObject(3).getString("start") + " (" + jsonArray.getJSONObject(3).getString("name") + " ??????)");
                        }
                        arrow3.setText("???");
                        detail4.setText(jsonArray.getJSONObject(3).getString("end"));
                    }
                } catch (Exception e) {
                    e.toString();
                }
            }
        } else if (finallincities.size() < 0 && finalloutcities.size() < 0) {
            textView.setText("????????? ????????????.");
        } else {
            if (finallincities.size()>0){
                try {
                    JSONArray jsonArray = new JSONArray(finallincities.get(0).getSubpath());
                    int i = jsonArray.length();
                    DecimalFormat df = new DecimalFormat("###,###");
                    String money = df.format(finallincities.get(0).getFare());
                    int hour = (int) Math.floor((double) finallincities.get(0).getTotalTime() / 60);
                    int minute = finallincities.get(0).getTotalTime() % 60;
                    textView.setText(finallincities.get(0).getName());
                    textView2.setText("???????????? : " + finallincities.get(0).getSchedule());
                    if (jsonArray.getJSONObject(1).getString("type").equals("??????")) {
                        textView3.setText("??????????????? : " + finallincities.get(0).getStart() + " (" + finallincities.get(0).getName() + " ??????)" + "    ??? " + finallincities.get(0).getArrtime()+" ???");
                    } else {
                        textView3.setText("??????????????? : " + finallincities.get(0).getStart() + "??? (" + finallincities.get(0).getName() + ")");
                    }
                    detail0.setText("???????????? : " + hour + "?????? " + minute + "???" + "     ?????? : " + money + "???");
                    if (jsonArray.getJSONObject(1).getString("type").equals("?????????")) {
                        detail1.setText(jsonArray.getJSONObject(1).getString("start") + "??? (" + finallincities.get(0).getName() + ")");
                    } else {
                        detail1.setText(jsonArray.getJSONObject(1).getString("start") + " (" + finallincities.get(0).getName() + " ??????)");
                    }
                    arrow1.setText("???");
                    detail2.setText(jsonArray.getJSONObject(1).getString("end"));
                    if (i > 4) {
                        arrow2.setText("???");
                        if (jsonArray.getJSONObject(2).get("type").equals("?????????")) {
                            detail3.setText(jsonArray.getJSONObject(3).getString("start"));
                        } else {
                            detail3.setText(jsonArray.getJSONObject(3).getString("start") + " (" + jsonArray.getJSONObject(3).getString("name") + " ??????)");
                        }
                        arrow3.setText("???");
                        detail4.setText(jsonArray.getJSONObject(3).getString("end"));
                    }
                } catch (Exception e) {
                    e.toString();
                }
            } else {
                try {
                    DecimalFormat df = new DecimalFormat("###,###");
                    String money = df.format(finalloutcities.get(0).getFare());
                    int hour = (int) Math.floor((double) finalloutcities.get(0).getTotalTime() / 60);
                    int minute = finalloutcities.get(0).getTotalTime() % 60;
                    JSONObject jsonObject = new JSONObject(finalloutcities.get(0).getFirstpath());
                    JSONArray jsonArray = (JSONArray) jsonObject.get("subpath");
                    JSONObject object = (JSONObject) jsonArray.getJSONObject(1);
                    JSONObject jsonObject1 = new JSONObject(finalloutcities.get(0).getSecondpath());
                    JSONObject jsonObject2 = new JSONObject(finalloutcities.get(0).getThirdpath());
                    JSONArray jsonArray1 = (JSONArray) jsonObject2.get("subpath");
                    JSONObject object1 = (JSONObject) jsonArray1.getJSONObject(1);
                    textView.setText(finalloutcities.get(0).getName());
                    textView2.setText("???????????? : " + finalloutcities.get(0).getSchedule());
                    textView3.setText("??????????????? : " + finalloutcities.get(0).getStart() + " (" + object.get("name") + " ??????)" + "    ??? " + finalloutcities.get(0).getArrtime()+" ???");
                    detail0.setText("???????????? : " + hour + "?????? " + minute + "???" + "    ?????? : " + money + "???");
                    detail1.setText(jsonObject.get("start").toString() + " (" + object.get("name") + " ??????)");
                    arrow1.setText("???");
                    detail2.setText(jsonObject.get("end").toString());
                    arrow2.setText("???");
                    detail3.setText(jsonObject1.get("start").toString() + " (" + finalloutcities.get(0).getName() + ")");
                    arrow3.setText("???");
                    detail4.setText(jsonObject1.get("end").toString());
                    arrow4.setText("???");
                    detail5.setText(jsonObject2.get("start").toString() + " (" + object1.get("name") + " ??????)");
                    arrow5.setText("???");
                    detail6.setText(jsonObject2.get("end").toString());
                } catch (Exception e){
                    e.toString();
                }
            }
        }

    }

}
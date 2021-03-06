package com.example.simplemp3player;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView listMp3;
    Button btnPlay, btnStop, btnPause;
    TextView tvMp3;
    ProgressBar pbMp3;
    ArrayList<String> mp3List; //음악 파일만 넣기
    ArrayAdapter<String> adapter;
    String selectedMp3, sdPath;//텍스트 뷰에 노래 제목 나타내기
    MediaPlayer mPlayer;
    boolean PAUSED=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listMp3=findViewById(R.id.listMp3);
        btnPlay=findViewById(R.id.btnStart);
        btnStop=findViewById(R.id.btnStop);
        btnPause=findViewById(R.id.btnPause);
        tvMp3=findViewById(R.id.tvMp3);
        pbMp3=findViewById(R.id.pdMp3);
        //동적 배열
        mp3List=new ArrayList<String>();

        if(Build.VERSION.SDK_INT>=26){
            int pCheck= ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if(pCheck== PackageManager.PERMISSION_DENIED){
                ActivityCompat.requestPermissions(this, new String[]{
                        Manifest.permission.WRITE_EXTERNAL_STORAGE}, MODE_PRIVATE);
            }else{
                sdcardProcess();;
            }
        } else{
            sdcardProcess();
        }
        listMp3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedMp3=mp3List.get(position);
            }
        });
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    mPlayer=new MediaPlayer();
                    mPlayer.setDataSource(sdPath+"/"+selectedMp3);
                    mPlayer.prepare();
                    mPlayer.start();
                    tvMp3.setText("실행중인 음악 : " + selectedMp3);
                    pbMp3.setVisibility(View.VISIBLE);
                    btnPlay.setEnabled(false);
                    btnStop.setEnabled(true);
                    btnPause.setEnabled(true);
                } catch (IOException e){
                    showToast("재생할 수 없는 파일입니다.");
                }
            }
        });
        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(PAUSED==false){
                    mPlayer.pause();
                    btnPause.setText("이어듣기");
                    PAUSED=true;
                    pbMp3.setVisibility(View.INVISIBLE);
                }else {
                    mPlayer.start();
                    btnPause.setText("일시정지");
                    PAUSED=false;
                    pbMp3.setVisibility(View.VISIBLE);
                }
            }
        });
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPlayer.stop();
                mPlayer.reset();;
                btnPlay.setEnabled(true);
                btnStop.setEnabled(false);
                btnPause.setEnabled(false);
                btnPause.setText("일시정지");
                tvMp3.setText("실행중인 음악 : ");
                pbMp3.setVisibility(View.INVISIBLE);
            }
        });
    }
    //SD카드 처리 메서드
    void sdcardProcess(){
        sdPath= Environment.getExternalStorageDirectory().getAbsolutePath()+"/Music";
        File[] listFiles=new File(sdPath).listFiles();
        String fileName, extName;
        for(File file : listFiles){
            fileName=file.getName();
            extName=fileName.substring(fileName.length()-3); // 확장자 빼기
            if(extName.equals("mp3")){
                mp3List.add(fileName);
            }
        }
        adapter=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_single_choice, mp3List);
        listMp3.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listMp3.setAdapter(adapter);
        listMp3.setItemChecked(0, true);
        selectedMp3=mp3List.get(0);
    }
    //토스트 메서드
    void showToast(String msg){
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT ).show();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults[0] != PackageManager.PERMISSION_GRANTED){
            showToast("SD카드 접근을 거부했습니다.");
        } else{
            sdcardProcess();
        }
    }
}
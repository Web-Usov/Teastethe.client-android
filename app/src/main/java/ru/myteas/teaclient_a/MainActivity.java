package ru.myteas.teaclient_a;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import io.socket.client.Socket;
import ru.myteas.teaclient_a.Auth.LoginActivity;

public class MainActivity extends AppCompatActivity implements TeaApp.SocketListener {
    private static final int LOGIN_REQ=1;
    private static final int HOME_REQ=2;
    private Thread checkStatus;
    private Socket mSocket;
    private  Intent homePage;

    private int mAnswer = -1 ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Init();


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable final Intent data) {
        final TeaApp app = (TeaApp) getApplication();
        if(requestCode==LOGIN_REQ){
            if(resultCode!=RESULT_OK) {
                finish();
                return;
            }



            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    if(data==null) {
                        Toast.makeText(getApplicationContext(), "Data is null", Toast.LENGTH_LONG).show();
                        return;
                    }
                    JSONObject _user;
                    try {
                        _user = new JSONObject(data.getStringExtra("user"));
                        User.name = _user.getString("name");
                        User.id = _user.getString("_id");
                    } catch (JSONException e) {
                        e.printStackTrace();
                        return;
                    }
//                    Toast.makeText(getApplicationContext(), "Welcome, "+mUser.getName(), Toast.LENGTH_LONG).show();
                    setTitle("Welcome, "+User.name);
                }
            });
            StartHomePage();

            return;
        }else if(requestCode==HOME_REQ){
            Log.i("_DEV","onActivityResult HOME_REQ");
        }

        super.onActivityResult(requestCode, resultCode, data);
        finish();
    }

    private void Init(){

        final TeaApp app = (TeaApp) getApplication();
        app.ReqForServer();
        checkStatus = new Thread(new Runnable() {
            int count = 0;
            final int maxCount = 100;
            final int timeOut = 100;
            @Override
            public void run() {
                while (app.getFethcStatus() == 0 && count < maxCount){
                    count++;
                    try {
                        Thread.sleep(timeOut);
                        Log.i("_DEV",count+" - Ответ от сервера в Main "+mAnswer);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if(app.getFethcStatus() == 1) mAnswer = 1;
                else {
                    app.setFetchStatus(-1);
                    mAnswer = 0;
                }
                ChangedFetchStatus();
            }
        });

        checkStatus.setDaemon(true);
        checkStatus.start();
    }

    private void ChangedFetchStatus(){

        if(mAnswer == 1){
            checkStatus.interrupt();
            Log.i("_DEV", "LOADING FALSE");
            final TeaApp app = (TeaApp) getApplication();
            mSocket = app.getSocket();
            mSocket.connect();
            app.MainSocketEventsOn(this);
            mSocket.emit("getUser");
//            if(mUser == null){
//                Intent LoginPage = new Intent(this,LoginActivity.class );
//                startActivityForResult(LoginPage,REQUEST_ACCESS_TYPE);
//            }

        }else{

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(),"Нет ответа от сервера ", Toast.LENGTH_LONG).show();
                }
            });
            finish();
        }
    }
    @Override
    protected void onDestroy() {
        TeaApp app = (TeaApp) getApplication();
        app.MainSocketEventsOff();
        super.onDestroy();
    }

    @Override
    public void GetUser() {
        if(User.name==null){
            Intent LoginPage = new Intent(this,LoginActivity.class );
            startActivityForResult(LoginPage,LOGIN_REQ);

        }else{
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(),"User already logged : "+User.name, Toast.LENGTH_LONG).show();
                }
            });
            StartHomePage();
        }
        FragmentManager fm = getSupportFragmentManager();
        Fragment mainFragment = fm.findFragmentByTag("mainFragment");
        fm.beginTransaction()
                .hide(mainFragment)
                .remove(mainFragment).commit();
    }


    private void StartHomePage(){
        homePage = new Intent(this, HomeActivity.class);
        startActivityForResult(homePage,HOME_REQ);
    }
}

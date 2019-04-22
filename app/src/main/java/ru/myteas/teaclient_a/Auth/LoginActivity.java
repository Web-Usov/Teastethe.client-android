package ru.myteas.teaclient_a.Auth;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import ru.myteas.teaclient_a.R;
import ru.myteas.teaclient_a.TeaApp;


public class LoginActivity extends AppCompatActivity
        implements LoginFragment.LoginListener, SignupFragment.SignupListener {

    private Socket mSocket;
//    private EditText mInputUserName;
    private Fragment mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        mFragment = new LoginFragment();
        ft.add(R.id.LoginPageFragment,mFragment);
        ft.commit();

        TeaApp app = (TeaApp) getApplication();
        mSocket = app.getSocket();
        mSocket.on("login", onLogin);
        mSocket.on("register", onSignup);
    }



    public void changeAuthType(View view){
        Fragment newFragment = null;
        switch (view.getId()){
            case R.id.setLogin:{
                newFragment = new LoginFragment();
                break;
            }
            case R.id.setSignup:{
                newFragment = new SignupFragment();
                break;
            }
            default:{
                newFragment = new LoginFragment();
                break;
            }
        }
        if(mFragment.getClass() != newFragment.getClass()){
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.LoginPageFragment, newFragment);
            ft.commit();

            mFragment = newFragment;
        }
    }

    private Emitter.Listener onLogin = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread( new Runnable() {
                public void run() {

                    JSONObject data = (JSONObject) args[0];


                    String error = null;
                    try {
                        error = data.getString("error");
                    } catch (JSONException e) {
                        Log.e("E", e.getMessage());
                        return;
                    }
                    if(!error.equals("null")) {
                        LoginFragment lf = (LoginFragment) mFragment;
                        lf.UpdateState(error,"");
                        Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG).show();
                        return;
                    }

                    JSONObject user;
                    try {
                        user = data.getJSONObject("user");
                    } catch (JSONException e) {
                        Log.e("E", e.getMessage());
                        return;
                    }


                    Intent output = new Intent();
                    output.putExtra("user", user.toString());
                    setResult(RESULT_OK, output);
                    finish();
                }
            });
        }
    };
    private Emitter.Listener onSignup = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread( new Runnable() {
                public void run() {

                    JSONObject data = (JSONObject) args[0];

                    String error;
                    try {
                        error = data.getString("error");
                    } catch (JSONException e) {
                        Log.e("E", e.getMessage());
                        return;
                    }
                    if(!error.equals("null")) {
                        SignupFragment sf = (SignupFragment) mFragment;
                        sf.UpdateState(error,"");
                        Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG).show();
                        return;
                    }

                    String message;
                    try {
                        message = data.getString("message");
                    } catch (JSONException e) {
                        Log.e("E", e.getMessage());
                        return;
                    }

                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                }
            });
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mSocket.off("login");
        mSocket.off("register");
    }

    @Override
    public void BtnClickLogin(String name) {
        Log.i("_DEV","BtnClickLogin");
        JSONObject obj = new JSONObject();
        try {
            obj.put("name", name);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mSocket.emit("login",obj);
    }

    @Override
    public void BtnClickSignup(String name) {
        Log.i("_DEV","BtnClickSignup");
        JSONObject obj = new JSONObject();
        try {
            obj.put("name", name);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mSocket.emit("register",obj);
    }

}

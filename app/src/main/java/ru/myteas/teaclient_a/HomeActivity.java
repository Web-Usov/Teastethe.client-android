package ru.myteas.teaclient_a;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import ru.myteas.teaclient_a.Teas.Tea;
import ru.myteas.teaclient_a.Teas.TeaList;
import ru.myteas.teaclient_a.Teas.TeaListFragment;

public class HomeActivity extends AppCompatActivity {
    private Fragment mFragment;
    private Socket mSocket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        TeaApp app = (TeaApp) getApplication();
        mSocket = app.getSocket();

        init();


//        TeaList.List = InitTeaList();

        mFragment = new TeaListFragment();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.homeFragment, new TeaListFragment());
        ft.commit();

    }

    private void init() {
        TeaList.List = new ArrayList<>();
        mSocket.emit("allTeas");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }



}

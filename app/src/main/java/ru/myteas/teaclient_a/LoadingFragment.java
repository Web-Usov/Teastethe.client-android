package ru.myteas.teaclient_a;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class LoadingFragment extends Fragment {

    private TextView mLoading;
    private int countDot = 0;
    private  String[] arrDot = {"",".","..","..."};
    private Thread LoadingThread;
    public LoadingFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_loading, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mLoading = view.findViewById(R.id.label_loading);
        StartLoading();

    }

    @Override
    public void onDetach() {
        super.onDetach();
        LoadingThread.interrupt();

    }



    private void StartLoading() {

        final TeaApp app = (TeaApp) getActivity().getApplication();

        LoadingThread = new Thread() {
            public void run() {
                while (app.getFethcStatus() == 0) {
                    try {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (countDot != 3) countDot++;
                                else countDot = 0;
                                mLoading.setText("Please wait" + arrDot[countDot]);
                                Log.i("_DEV", "LOADING...");
                            }
                        });
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        LoadingThread.setDaemon(true);
        LoadingThread.start();
    }

}

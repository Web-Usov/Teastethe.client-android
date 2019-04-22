package ru.myteas.teaclient_a.Teas;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import ru.myteas.teaclient_a.R;

public class TeaListFragment extends Fragment {

    private ListView mTeaListView;
    private TeaAdapter mTeaAdapter;
    private FloatingActionButton mFabBtn;

    public TeaListFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void Init() {
        mFabBtn = getActivity().findViewById(R.id.btnFabAddTea);
        mTeaListView = getActivity().findViewById(R.id.teasListView);

        if(TeaList.List == null) TeaList.List = new ArrayList<>();

        mTeaAdapter = new TeaAdapter(getContext(),TeaList.List);
        mTeaListView.setAdapter(mTeaAdapter);


        mFabBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TeaAddDialogFragment tadf = new TeaAddDialogFragment();
                tadf.show(getFragmentManager(),"TeaAddDialogFragment");
            }
        });
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tea_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Init();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.i("_DEV-TeaListFragment","onDetach");
    }


}

package ru.myteas.teaclient_a.Teas;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import ru.myteas.teaclient_a.R;
import ru.myteas.teaclient_a.TeaApp;
import ru.myteas.teaclient_a.User;


public class TeaAddDialogFragment extends DialogFragment {
    private EditText mName, mType;
    private SeekBar choseRating;
    private TextView viewChoseRating;
    public TeaAddDialogFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final TeaApp app = (TeaApp) getActivity().getApplication();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.fragment_tea_add_diaolg,null);

        builder.setTitle("New tea")
            .setView(view)
            .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    mName = getDialog().findViewById(R.id.inputTeaName);
                    mType = getDialog().findViewById(R.id.inputTeaType);
                    choseRating = getDialog().findViewById(R.id.choseTeaRating);
                    if(mName.getText().toString().trim().equals("")) {
                        mName.setError("Required");
                    }else if(mType.getText().toString().trim().equals("")){
                        mType.setError("Required");
                    }else{
                        try {
                            JSONObject tea = new JSONObject();
                            tea.put("name",mName.getText().toString().trim());
                            tea.put("type",mType.getText().toString().trim());
                            tea.put("reating",choseRating.getProgress());

                            app.getSocket().emit("addTea",tea);

                        }catch (JSONException e){
                            Log.e("_DEV", e.toString());
                        }
                    }
                }
            })
            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                    TeaAddDialogFragment.this.getDialog().cancel();

                }
            });

        viewChoseRating = view.findViewById(R.id.viewChoseTeaRating);
        choseRating = view.findViewById(R.id.choseTeaRating);
        choseRating.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                viewChoseRating.setText(String.valueOf(choseRating.getProgress()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        // Create the AlertDialog object and return it
        return builder.create();
    }


    @Override
    public void onViewCreated(View view,Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.i("_DEV-TeaDialog","onViewCreated");


    }
}

package ru.myteas.teaclient_a.Auth;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import ru.myteas.teaclient_a.R;

public class SignupFragment extends Fragment {

    public interface SignupListener {
        void BtnClickSignup(String name);
    }

    private SignupListener mListener;
    private Button mBtnSendSignup;
    private EditText mUsername;

    public SignupFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_signup, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        mBtnSendSignup = view.findViewById(R.id.btnSignup);
        mUsername = view.findViewById(R.id.inputUserName);

        mBtnSendSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBtnSendSignup.setEnabled(false);
                mUsername.setEnabled(false);
                mListener.BtnClickSignup(mUsername.getText().toString().trim());
            }
        });
//        super.onViewCreated(view, savedInstanceState);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(!(getActivity() instanceof SignupListener)){
            try {
                throw new Exception("Activity is not instance of BtnClickListenerSignup");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        mListener = (SignupListener) getActivity();
    }
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void UpdateState(String error,String typeError){
        mUsername.setEnabled(true);
        mBtnSendSignup.setEnabled(true);
        mUsername.setError(error);
    }
    public void UpdateState(){
        mUsername.setEnabled(true);
        mBtnSendSignup.setEnabled(true);
    }
}
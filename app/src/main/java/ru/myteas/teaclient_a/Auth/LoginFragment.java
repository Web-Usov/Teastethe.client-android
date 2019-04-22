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

public class LoginFragment extends Fragment {

    public interface LoginListener {
        void BtnClickLogin(String name);
    }

    private Button mBtnSendLogin;
    private EditText mUsername;
    private LoginListener mListener;
    public LoginFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(View view,Bundle savedInstanceState) {


        mBtnSendLogin = view.findViewById(R.id.btnLogin);
        mUsername = view.findViewById(R.id.inputUserName);
        mBtnSendLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBtnSendLogin.setEnabled(false);
                mUsername.setEnabled(false);
                mListener.BtnClickLogin(mUsername.getText().toString().trim());
            }
        });
//        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(!(getActivity() instanceof LoginListener)){
            try {
                throw new Exception("Activity is not instance of BtnClickListenerLogin");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        mListener = (LoginListener) getActivity();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void UpdateState(String error,String typeError){
        mUsername.setEnabled(true);
        mBtnSendLogin.setEnabled(true);
        mUsername.setError(error);
    }
    public void UpdateState(){
        mUsername.setEnabled(true);
        mBtnSendLogin.setEnabled(true);
    }
}
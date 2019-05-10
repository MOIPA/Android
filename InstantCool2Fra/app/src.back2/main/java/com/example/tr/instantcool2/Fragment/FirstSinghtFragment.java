package com.example.tr.instantcool2.Fragment;

//import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.tr.instantcool2.R;

/**
 * Created by TR on 2017/10/11.
 */

public class FirstSinghtFragment extends Fragment {

    private Button btnSignUp;
    private Button btnSignIn;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_first_singht,container,false);
        btnSignIn = (Button) view.findViewById(R.id.btn_first_singht_sign_in);
        btnSignUp = (Button) view.findViewById(R.id.btn_first_singht_sign_up);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.start_frame_container,new SignUpFragment()).commit();
            }
        });

//        btnSignIn
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.start_frame_container,new SignInFragment()).commit();
            }
        });

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}

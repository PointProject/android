package com.pointproject.pointproject.ui.userInfo;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pointproject.pointproject.AbstractFragment;
import com.pointproject.pointproject.R;
import com.pointproject.pointproject.model.User;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UserInfoFragment extends AbstractFragment implements UserInfoContract.View{

    public static final String EXTRA_USER = "fragment_user";

    @BindView(R.id.login_info) TextView login;
    @BindView(R.id.first_name_info) TextView firstName;
    @BindView(R.id.last_name_info) TextView lastName;
    @BindView(R.id.age_info) TextView age;
    @BindView(R.id.phone_number_info) TextView phoneNumber;

    @Inject UserInfoContract.Presenter presenter;

    private User currentUser;

    @Inject
    public UserInfoFragment(){
    }

    @Override
    public void onStart() {
        super.onStart();
        presenter.takeView(this);
    }

    @Override
    public void onStop() {
        presenter.dropView();
        super.onStop();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_info, container, false);
        ButterKnife.bind(this, view);

        assert savedInstanceState != null;
        if(!savedInstanceState.isEmpty())
            currentUser = (User)savedInstanceState.getSerializable(EXTRA_USER);

        return view;
    }

    @Override
    public void showUserInfo() {
        login.setText(getString(R.string.user_login, currentUser.getLogin()));
        firstName.setText(getString(R.string.user_name, currentUser.getFirstName()));
        lastName.setText(getString(R.string.user_last_name, currentUser.getLastName()));
        age.setText(getString(R.string.user_age, currentUser.getAge()));
        phoneNumber.setText(getString(R.string.user_phone_number, currentUser.getPhone()));
    }
}

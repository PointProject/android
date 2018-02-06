package com.pointproject.pointproject.ui.userInfo;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
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

    @BindView(R.id.login_info) TextView login;
    @BindView(R.id.first_name_info) TextView firstName;
    @BindView(R.id.last_name_info) TextView lastName;
    @BindView(R.id.age_info) TextView age;
    @BindView(R.id.phone_number_info) TextView phoneNumber;

    @Inject UserInfoContract.Presenter presenter;

    @Inject
    public UserInfoFragment(){

    }

    @Override
    public void onStart() {
        super.onStart();
        presenter.takeView(this);
        presenter.getUserInfo();
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

        return view;
    }

    @Override
    public void showUserInfo(User user) {
        login.setText(getString(R.string.user_login, user.getLogin()));
        firstName.setText(getString(R.string.user_name, user.getFirstName()));
        lastName.setText(getString(R.string.user_last_name, user.getLastName()));
        age.setText(getString(R.string.user_age, user.getAge()));
        phoneNumber.setText(getString(R.string.user_phone_number, user.getPhone()));
    }
}

package com.procialize.eventapp.ui.login.viewmodel;

import android.text.TextUtils;
import android.util.Patterns;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.library.baseAdapters.BR;

import com.procialize.eventapp.ui.login.model.Login;

public class LoginViewModel extends BaseObservable {
    private Login login;


    private String successMessage = "Login was successful";
    private String errorMessage = "Email or Password not valid";


    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String userEmail;
    public String userPassword;
    @Bindable
    private String toastMessage = null;


    public String getToastMessage() {
        return toastMessage;
    }


    private void setToastMessage(String toastMessage) {

        this.toastMessage = toastMessage;
        notifyPropertyChanged(BR.toastMessage);
    }


    public void setloginEmail(String userEmail) {
        login.setEmail(userEmail);
        notifyPropertyChanged(BR.loginEmail);
    }

    @Bindable
    public String getloginEmail() {
        return userEmail;
    }

    @Bindable
    public String getloginPassword() {
        return userPassword;
    }

    public void setloginPassword(String password) {
        login.setPassword(password);
        notifyPropertyChanged(BR.loginPassword);
    }

    public LoginViewModel() {
        login = new Login(getloginEmail(), getloginPassword());
    }

    public void onLoginClicked() {

        if (isInputDataValid())
            setToastMessage(successMessage);
        else
            setToastMessage(errorMessage);
    }

    public boolean isInputDataValid() {
        if (getloginEmail()==null || getloginEmail().isEmpty())
            return false;
        else if (getloginPassword()==null || getloginPassword().isEmpty())
            return false;
        else
            return !TextUtils.isEmpty(getloginEmail()) || Patterns.EMAIL_ADDRESS.matcher(getloginEmail()).matches() || getloginPassword().length() > 5;

    }
}

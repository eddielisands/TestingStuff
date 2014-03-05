package com.gt.snssharinglibrary.service.facebook_2_0;

import android.os.Bundle;

import com.facebook_2_0.android.DialogError;
import com.facebook_2_0.android.FacebookError;
import com.facebook_2_0.android.Facebook.DialogListener;
import com.gt.snssharinglibrary.util.LogController;

public class LoginDialogListener implements DialogListener {
    public void onComplete(Bundle values) {
    	LogController.log("Facebook LoginDialogListener onComplete");
        SessionEvents.onLoginSuccess();
    }

    public void onFacebookError(FacebookError error) {
    	LogController.log("Facebook LoginDialogListener onFacebookError");
        SessionEvents.onLoginError(error.getMessage());
    }
    
    public void onError(DialogError error) {
    	LogController.log("Facebook LoginDialogListener onError");
        SessionEvents.onLoginError(error.getMessage());
    }

    public void onCancel() {
    	LogController.log("Facebook LoginDialogListener onCancel");
        SessionEvents.onLoginError("Action Canceled");
    }
}

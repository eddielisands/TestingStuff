package com.gt.snssharinglibrary.service.facebook_2_0;

import com.facebook_2_0.android.DialogError;
import com.facebook_2_0.android.Facebook.DialogListener;
import com.facebook_2_0.android.FacebookError;

/**
 * Skeleton base class for RequestListeners, providing default error 
 * handling. Applications should handle these error conditions.
 *
 */
public abstract class BaseDialogListener implements DialogListener {

    public void onFacebookError(FacebookError e) {
        e.printStackTrace();
    }

    public void onError(DialogError e) {
        e.printStackTrace();        
    }

    public void onCancel() {        
    }
    
}

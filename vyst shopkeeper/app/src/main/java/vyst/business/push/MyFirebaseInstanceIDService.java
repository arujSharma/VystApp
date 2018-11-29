package vyst.business.push;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import vyst.business.util.SharedPrefUtil;

/**
 * Created by Prerna on 4/3/2018.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    @Override
    public void onTokenRefresh() {
        String refreshedToken= FirebaseInstanceId.getInstance().getToken();
        Log.e("token",refreshedToken);
        SharedPrefUtil.setNotificationToken(refreshedToken);
    }
}

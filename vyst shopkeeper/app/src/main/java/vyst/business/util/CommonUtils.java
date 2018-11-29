package vyst.business.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.androidadvance.topsnackbar.TSnackbar;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import vyst.business.R;

/**
 * Created by Admin on 8/24/2017.
 */

public class CommonUtils {

    static Dialog dialog;

    public static Boolean validateEmail(final String email) {
        final String EMAIL_PATTERN = Patterns.EMAIL_ADDRESS.toString();
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }


    /**
     * Show the circular progress bar as loading with the message
     *
     * @param activity on which it is to be displayed
     * @param message  that is to be shown
     */
    public static void showProgressDialog(final Context activity, String message) {
        try {
            hideDialog();
            dialog = new Dialog(activity,
                    R.style.Theme_AppCompat_Translucent);

            // Configure dialog box
            dialog.setContentView(R.layout.progress_layout);
            WindowManager.LayoutParams layoutParams = dialog.getWindow()
                    .getAttributes();
            layoutParams.dimAmount = 0.6f;
            dialog.getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);

            // set progress drawable
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                ProgressBar progressBar = (ProgressBar) dialog.findViewById(R.id.circular_progress);
                progressBar.setIndeterminateDrawable(activity.getResources().getDrawable(R.drawable.progress));
            }

            // set the message
            if (!message.isEmpty()) {
                final TextView dialogMsg = (TextView) dialog.findViewById(R.id.progress_msg);
                dialogMsg.setText(message);
            }

            try {
                dialog.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // hide the progress dialog
    public static void hideDialog() {
        try {
            if (dialog != null) {
                if (dialog.isShowing()) {
                    dialog.dismiss();

                }
                dialog = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

   /* public static void showSnackbar(View v,String message) {
        Snackbar snackbar = Snackbar
                .make(v, message, Snackbar.LENGTH_LONG);


        snackbar.show();
    }*/


    public static void showSnackbar(Activity activity, String msg){
        TSnackbar snackbar = TSnackbar.make(activity.findViewById(android.R.id.content), msg, TSnackbar.LENGTH_LONG);
        snackbar.setActionTextColor(Color.WHITE);
        View snackbarView = snackbar.getView();
        snackbarView.setBackgroundColor(activity.getResources().getColor(R.color.colorWhite));
        TextView textView = (TextView) snackbarView.findViewById(com.androidadvance.topsnackbar.R.id.snackbar_text);
        textView.setTextColor(activity.getResources().getColor(R.color.colorPrimary));
        snackbar.show();
    }

    public static boolean isConnectedToInternet(Context act) {
        boolean isNetConnected;
        if(act!=null){
            try {
                ConnectivityManager ConMgr = (ConnectivityManager) act
                        .getSystemService(Context.CONNECTIVITY_SERVICE);
                isNetConnected = ConMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED
                        || ConMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
                        .getState() == NetworkInfo.State.CONNECTED;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            return isNetConnected;
        }
        else{
            return true;
        }
    }






    public static void openLink(final Activity context, final String url) {

        try {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            context.startActivity(i);
        } catch (Exception e) {
            e.printStackTrace();
            showSnackbar(context,context.getString(R.string.check_your_web_address));
        }
    }



}

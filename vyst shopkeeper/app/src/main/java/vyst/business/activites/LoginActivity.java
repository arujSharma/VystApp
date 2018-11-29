package vyst.business.activites;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import vyst.business.R;
import vyst.business.util.SharedPrefUtil;

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    Button tv_be_a_shopkeeper,tv_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        if(SharedPrefUtil.isUserLoggedIn(LoginActivity.this)){
            Intent intent1 = new Intent(getApplication(), MainActivity.class);
            startActivity(intent1);
            finish();
        }
        init();

    }

    private void init() {

        tv_be_a_shopkeeper = findViewById(R.id.tv_be_a_shopkeeper);
        tv_login = findViewById(R.id.tv_login);

        tv_be_a_shopkeeper.setOnClickListener(this);
        tv_login.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.tv_login:

                Intent intent1 = new Intent(getApplication(), OTPActivity.class);
                startActivity(intent1);
                finish();

                Log.d("fcm_token","token "+SharedPrefUtil.getNotificationToken());


                break;

            case R.id.tv_be_a_shopkeeper:

                Intent intent = new Intent(getApplication(), RegisterShopkeeper.class);
                startActivity(intent);


                break;




        }

    }
}

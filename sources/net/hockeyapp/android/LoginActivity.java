package net.hockeyapp.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.facebook.AppEventsConstants;
import com.facebook.Response;
import com.google.android.gms.appstate.AppStateClient;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import net.hockeyapp.android.tasks.LoginTask;
import net.hockeyapp.android.utils.AsyncTaskUtils;
import net.hockeyapp.android.views.LoginView;
/* loaded from: classes.dex */
public class LoginActivity extends Activity implements View.OnClickListener {
    private Handler loginHandler;
    private LoginTask loginTask;
    private int mode;
    private String secret;
    private String url;

    @Override // android.app.Activity
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new LoginView(this));
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            this.url = extras.getString("url");
            this.secret = extras.getString("secret");
            this.mode = extras.getInt("mode");
        }
        configureView();
        initLoginHandler();
        Object object = getLastNonConfigurationInstance();
        if (object != null) {
            this.loginTask = (LoginTask) object;
            this.loginTask.attach(this, this.loginHandler);
        }
    }

    private void configureView() {
        if (this.mode == 1) {
            EditText passwordInput = (EditText) findViewById(12292);
            passwordInput.setVisibility(4);
        }
        Button loginButton = (Button) findViewById(LoginView.LOGIN_BUTTON_ID);
        loginButton.setOnClickListener(this);
    }

    private void initLoginHandler() {
        this.loginHandler = new Handler() { // from class: net.hockeyapp.android.LoginActivity.1
            @Override // android.os.Handler
            public void handleMessage(Message msg) {
                Bundle bundle = msg.getData();
                boolean success = bundle.getBoolean(Response.SUCCESS_KEY);
                if (success) {
                    LoginActivity.this.finish();
                    if (LoginManager.listener != null) {
                        LoginManager.listener.onSuccess();
                        return;
                    }
                    return;
                }
                Toast.makeText(LoginActivity.this, "Login failed. Check your credentials.", (int) AppStateClient.STATUS_WRITE_OUT_OF_DATE_VERSION).show();
            }
        };
    }

    private void performAuthentication() {
        String email = ((EditText) findViewById(12291)).getText().toString();
        String password = ((EditText) findViewById(12292)).getText().toString();
        boolean ready = false;
        Map<String, String> params = new HashMap<>();
        if (this.mode == 1) {
            ready = !TextUtils.isEmpty(email);
            params.put("email", email);
            params.put("authcode", md5(this.secret + email));
        } else if (this.mode == 2) {
            ready = (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) ? false : true;
            params.put("email", email);
            params.put("password", password);
        }
        if (ready) {
            this.loginTask = new LoginTask(this, this.loginHandler, this.url, this.mode, params);
            AsyncTaskUtils.execute(this.loginTask);
            return;
        }
        Toast.makeText(this, Strings.get(Strings.LOGIN_MISSING_CREDENTIALS_TOAST_ID), 1000).show();
    }

    public String md5(String s) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(s.getBytes());
            byte[] messageDigest = digest.digest();
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(aMessageDigest & 255);
                while (h.length() < 2) {
                    h = AppEventsConstants.EVENT_PARAM_VALUE_NO + h;
                }
                hexString.append(h);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View v) {
        switch (v.getId()) {
            case LoginView.LOGIN_BUTTON_ID /* 12293 */:
                performAuthentication();
                return;
            default:
                return;
        }
    }

    @Override // android.app.Activity, android.view.KeyEvent.Callback
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == 4) {
            if (LoginManager.listener != null) {
                LoginManager.listener.onBack();
            } else {
                Intent intent = new Intent(this, LoginManager.mainActivity);
                intent.setFlags(67108864);
                intent.putExtra("net.hockeyapp.android.EXIT", true);
                startActivity(intent);
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override // android.app.Activity
    public Object onRetainNonConfigurationInstance() {
        if (this.loginTask != null) {
            this.loginTask.detach();
        }
        return this.loginTask;
    }
}

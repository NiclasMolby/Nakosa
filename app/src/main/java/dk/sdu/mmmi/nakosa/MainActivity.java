package dk.sdu.mmmi.nakosa;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    CallbackManager callbackManager;
    UserData userData;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        userData = UserData.getInstance();

        intent = getIntent();

        performAction();


        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        setFacebookData(loginResult);
                    }

                    @Override
                    public void onCancel() {
                        ((TextView) findViewById(R.id.textView)).setText("Logged in cancelled...!");
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        ((TextView) findViewById(R.id.textView)).setText("Logged in failed :(");
                    }
                });
    }

    private void performAction() {
        if(intent.getBooleanExtra("Logout", false)) {
            LoginManager.getInstance().logOut();
        }
        else {
            checkLoginStatus();
        }
    }

    private void checkLoginStatus() {
        if(AccessToken.getCurrentAccessToken() != null){
            userData.setFirstName(Profile.getCurrentProfile().getFirstName());
            userData.setLastName(Profile.getCurrentProfile().getLastName());
            changeView();
        }
    }

    private void changeView() {
        Intent intent = new Intent(MainActivity.this, FrontActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    @OnClick(R.id.bypass)
    public void bypass(View view){
        userData.setFirstName("TEST");
        userData.setLastName("ACCOUNT");
        changeView();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void setFacebookData(final LoginResult loginResult)
    {
        GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        try {
                            userData.setFirstName(response.getJSONObject().getString("first_name"));
                            userData.setLastName(response.getJSONObject().getString("last_name"));
                            changeView();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,email,first_name,last_name,gender");
        request.setParameters(parameters);
        request.executeAsync();
    }
}
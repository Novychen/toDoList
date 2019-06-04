package at.fhooe.mc.android;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends Activity implements View.OnClickListener {

    private FirebaseAuth mAuthentication;
    static long mTaskNumber;

    private final static String TAG = "at.fhooe.mc.toDoList";
    private EditText mEmail = null;
    private EditText mPassword = null;


    public static long getTaskNumber() {
        return mTaskNumber;
    }

    public static void setTaskNumber(long _number){
        mTaskNumber = _number;
    }

    @Override
    protected void onCreate(Bundle _savedInstanceState) {
        super.onCreate(_savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);

        mEmail = findViewById(R.id.main_Activity_Auth_LogIn_Email);
        mPassword = findViewById(R.id.main_Activity_Auth_LogIn_Password);

        Button b;
        b = findViewById(R.id.main_Activity_LogIn_Button);
        b.setOnClickListener(this);

        b = findViewById(R.id.main_Activity_SignIn_Button);
        b.setOnClickListener(this);

        mAuthentication = FirebaseAuth.getInstance();
        Repository.getInstance().setUserId(mAuthentication.getUid());
        if (Repository.getInstance().getUserId() != null) { // is user already logged in?
            logIn();
        }
    }

    /**
     * logs the user in and changes the activity to {@link ActivityList}
     */
    private void logIn() {
        Intent i = new Intent(this, ActivityList.class);
        Repository.getInstance().setUserId(mAuthentication.getUid());
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child(Repository.getInstance().getUserId()).child("CurrentTask");
        Repository.getInstance().getLongData(ref);

        startActivity(i);
        finish();
    }

    /**
     * starts the login process for the user
     */
    private void signIn() {
        mAuthentication.signInWithEmailAndPassword(mEmail.getText().toString(), mPassword.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "main_Activity::signInWithEmail:success");
                            FirebaseUser user = mAuthentication.getCurrentUser();
                            logIn();

                        } else {

                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "main_Activity::signInWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, getText(R.string.main_Activity_LogInFail_Toast), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    @Override
    public void onClick(View _v) {
        switch (_v.getId()) {
            case R.id.main_Activity_LogIn_Button: {
                signIn();
            }
            break;
            case R.id.main_Activity_SignIn_Button: {
                createAccount();
            }
            break;
            default:
                Log.e(TAG, "main_Activity::onClick unexpected ID encountered");
        }
    }

    /**
     * creates Account in database
     */
    private void createAccount() {

        mAuthentication.createUserWithEmailAndPassword(mEmail.getText().toString(), mPassword.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "main_Activity::createUserWithEmail success");
                            FirebaseUser user = mAuthentication.getCurrentUser();
                            Repository.getInstance().setUserId(mAuthentication.getUid());
                            mTaskNumber = 0;
                            Repository.getInstance().saveData(mTaskNumber);
                            Log.i(TAG, "MainActivity :: createAccount mTaskNumber is" + mTaskNumber);
                            Toast.makeText(MainActivity.this, getText(R.string.main_Activity_SignIn_Toast), Toast.LENGTH_SHORT).show();
                            logIn();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "main_Activity::createUserWithEmail failure - could not create account", task.getException());
                            Toast.makeText(MainActivity.this, getText(R.string.main_Activity_SignInFail_Toast), Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

}






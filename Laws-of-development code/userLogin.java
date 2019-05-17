
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;

public class userLogin extends AppCompatActivity {
    private EditText uEmail,uPass;
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener fbStateListner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);

        uEmail = findViewById(R.id.email);
        uEmail.requestFocus();
        uPass = findViewById(R.id.password);
        auth = FirebaseAuth.getInstance();
        fbStateListner = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if(user != null){
                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                    finish();
                    return;
                }

            }
        };

    }

    @Override
    protected void onStart() {
        super.onStart();
        auth.addAuthStateListener(fbStateListner);
    }

    @Override
    protected void onStop() {
        super.onStop();
        auth.removeAuthStateListener(fbStateListner);
    }

    public void logUserIn(View v){
        try{
        final String email = uEmail.getText().toString();
        final String pass = uPass.getText().toString();
        auth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(userLogin.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(!task.isSuccessful()){
                    FirebaseAuthException e = (FirebaseAuthException) task.getException();
                    Toast.makeText(userLogin.this, "Failed Registration: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    return;                }

            }
        });}catch(Exception e){
            Toast.makeText(this,e.getMessage(),Toast.LENGTH_SHORT).show();

    }}
}




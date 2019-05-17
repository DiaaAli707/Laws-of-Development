
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class firstPage_loginOrReg extends AppCompatActivity {
    public FirebaseAuth.AuthStateListener fbStateListner;
    private FirebaseAuth auth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_page_login_or_reg);
        auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null){
            startActivity(new Intent(firstPage_loginOrReg.this, MainActivity.class));
            finish();
            return;
        }
    }

    public void userClick(View v){
        switch(v.getId()){
            case R.id.login:
                startActivity(new Intent(this, userLogin.class));
                break;

            case R.id.register:
                startActivity(new Intent(this, userRegistration.class));
                break;
        }
    }
}

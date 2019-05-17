
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;

public class userRegistration extends AppCompatActivity {
    private EditText uEmail, uPass, uName;
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener fbStateListner;
    private ImageView userPic;
    private final int REQUEST_IMAGE_CAPTURE = 1;
    private final int REQUEST_IMAGE_FROM_GALLERY = 2;
    private Uri userPicChoice, download;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_registration);
        userPic = findViewById(R.id.userPic);
        uName = findViewById(R.id.name);
        uName.requestFocus();
        uEmail = findViewById(R.id.email);
        uPass = findViewById(R.id.password);
        auth = FirebaseAuth.getInstance();
        fbStateListner = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    startActivity(new Intent(userRegistration.this, MainActivity.class));
                    finish();
                    return;
                }

            }
        };


        userPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImg();

            }
        });
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

    public void chooseImg() {
        final String[] choice = {"Camera", "Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Image");
        builder.setItems(choice, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (choice[which].equals("Camera")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (intent.resolveActivity(getPackageManager()) != null) {
                        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
                    }
                } else if (choice[which].equals("Gallery")) {
                    Intent intent2 = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent2.setType("image/*");
                    startActivityForResult(intent2, REQUEST_IMAGE_FROM_GALLERY);
                } else if (choice[which].equals("Cancel")) {
                    dialog.dismiss();

                }
            }
        });
        builder.show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_IMAGE_CAPTURE:
                if (resultCode == RESULT_OK) {
                    Bundle extras = data.getExtras();
                    Bitmap imageBitmap = (Bitmap) extras.get("data");
                    userPic.setImageBitmap(imageBitmap);
                    //userPicChoice = imageBitmap;
                }
                break;

            case REQUEST_IMAGE_FROM_GALLERY:
                if (resultCode == RESULT_OK) {
                    final Uri selectedImage = data.getData();
                    userPicChoice = selectedImage;
                    userPic.setImageURI(selectedImage);


                }
        }
    }
    public Uri savePic(String userID){
        if (userPicChoice != null) {
            final StorageReference filepath = FirebaseStorage.getInstance().getReference().child("profileImages").child(userID);
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getApplication().getContentResolver(), userPicChoice);
            } catch (IOException e) {
                e.printStackTrace();
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
            byte[] data = baos.toByteArray();
            UploadTask uploadTask = filepath.putBytes(data);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    download = taskSnapshot.getUploadSessionUri();
                    //download = taskSnapshot.getStorage().getDownloadUrl().getResult();

                }
            });
        }


        return download;
    }

    public void registerUser(View v) {
        final String email = uEmail.getText().toString();
        final String pass = uPass.getText().toString();
        final String name = uName.getText().toString();
        auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(userRegistration.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()) {
                    FirebaseAuthException e = (FirebaseAuthException) task.getException();
                    Toast.makeText(userRegistration.this, "Failed Registration: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    final String userID = auth.getCurrentUser().getUid();
                    final DatabaseReference currentUserDb = FirebaseDatabase.getInstance().getReference();
                    Uri userSavedPic = savePic(userID);
                    currentUserDb.child("Users").child(userID).child("email").setValue(email);
                    currentUserDb.child("Users").child(userID).child("name").setValue(name);
                    currentUserDb.child("Users").child(userID).child("url").setValue(userSavedPic);


                }
            }
        });
    }
}

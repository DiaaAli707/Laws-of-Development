
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private ArrayList<topic> items = new ArrayList<>();
    private customTopicAdapter arrayAdapter;
    private int i;
    private String currentUID;
    private FirebaseAuth auth;
    private DatabaseReference userDB, topicDB, user_Topics, user_Saved_Topics;
    private SwipeFlingAdapterView flingContainer;
    private Dialog popUpDialog;
    private NavigationView navigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        flingContainer = findViewById(R.id.frame);
        popUpDialog = new Dialog(this);
        userDB = FirebaseDatabase.getInstance().getReference().child("Users");
        topicDB = FirebaseDatabase.getInstance().getReference().child("Topics");
        auth = FirebaseAuth.getInstance();
        currentUID = auth.getCurrentUser().getUid();
        user_Topics = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUID).child("connections");
        user_Saved_Topics = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUID).child("connections").child("Interested");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        userDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                View headerView = navigationView.getHeaderView(0);
                TextView navHeaderUserName = headerView.findViewById(R.id.userName_);
                navHeaderUserName.setText(dataSnapshot.child(currentUID).child("name").getValue().toString());
                TextView navHeaderUserEmail = headerView.findViewById(R.id.email_);
                navHeaderUserEmail.setText(dataSnapshot.child(currentUID).child("email").getValue().toString());
                ImageView navHeaderUserImage = headerView.findViewById(R.id.imageView);
                Glide.with(getApplicationContext()).load(dataSnapshot.child(currentUID).child("url").getValue()).into(navHeaderUserImage);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        topicDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();

                for (final DataSnapshot child : children) {
                    user_Topics.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (!dataSnapshot.child("Interested").hasChild(child.getKey()) && !dataSnapshot.child("notInterested").hasChild(child.getKey())) {
                                topic t = new topic(child.getKey(), child.child("URL").getValue().toString(), child.child("topicID").getValue().toString());
                                items.add(t);
                                arrayAdapter.notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        arrayAdapter = new customTopicAdapter(this, items);
        flingContainer.setAdapter(arrayAdapter);
        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                Log.d("LIST", "removed object!");
                items.remove(0);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onLeftCardExit(Object dataObject) {
                topic obj = (topic) dataObject;
                String TopicName = obj.getName();
                userDB.child(currentUID).child("connections").child("notInterested").child(TopicName).setValue(true);
                Toast.makeText(MainActivity.this, "Left!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRightCardExit(Object dataObject) {
                topic obj = (topic) dataObject;
                String TopicName = obj.getName();
                userDB.child(currentUID).child("connections").child("Interested").child(TopicName).setValue(true);
                Toast.makeText(getApplicationContext(), "Right", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {

            }

            @Override
            public void onScroll(float scrollProgressPercent) {
            }
        });

        flingContainer.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
            @Override
            public void onItemClicked(int itemPosition, Object dataObject) {
                popUpDialog.setContentView(R.layout.topic_pop_up);
                topic obj = (topic) dataObject;
                TextView Tname = popUpDialog.findViewById(R.id.topicName);
                TextView TID = popUpDialog.findViewById(R.id.topicID);
                ImageView img = popUpDialog.findViewById(R.id.topicImg);
                Tname.setText(obj.getName());
                Glide.with(getApplicationContext()).load(obj.getImgg()).into(img);
                TID.setText(obj.getTopicID());
                popUpDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                popUpDialog.show();

            }
        });

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.liked) {
            startActivity(new Intent(this, savedTopics.class));
            return super.onOptionsItemSelected(item);


        } else if (id == R.id.disliked) {
            startActivity(new Intent(this, dislikedTopics.class));
            return super.onOptionsItemSelected(item);

        } else if (id == R.id.sign_out) {
            auth.signOut();
            startActivity(new Intent(this, firstPage_loginOrReg.class));
            return super.onOptionsItemSelected(item);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}







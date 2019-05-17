
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class dislikedTopics extends AppCompatActivity {

    private DatabaseReference user_Saved_Topics, topicDB;
    private String currentUID;
    private FirebaseAuth auth;
    private ArrayList<topic> tList = new ArrayList<>();
    private customTopicListAdapter arrayListAdapter;
    private ListView LV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_disliked_topics);
        getSupportActionBar().setTitle("Disliked Topics");
        displayDislikedTopicsList();

    }

    public void displayDislikedTopicsList() {
        LV = findViewById(R.id.savedTopicList);
        auth = FirebaseAuth.getInstance();
        currentUID = auth.getCurrentUser().getUid();
        topicDB = FirebaseDatabase.getInstance().getReference().child("Topics");
        user_Saved_Topics = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUID).child("connections").child("notInterested");
        topicDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                for (final DataSnapshot child : children) {
                    user_Saved_Topics.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.hasChild(Objects.requireNonNull(child.getKey()))) {
                                topic t = new topic(child.getKey(), child.child("URL").getValue().toString(), child.child("topicID").getValue().toString());
                                tList.add(t);
                                arrayListAdapter.notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
                arrayListAdapter = new customTopicListAdapter(getApplicationContext(), tList);
                LV.setAdapter(arrayListAdapter);
                LV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        topic currentTopic = arrayListAdapter.getItem(position);
                        Intent intent = new Intent(dislikedTopics.this,displayTopicsFromSelectedTables.class);
                        intent.putExtra("name",currentTopic.getName());
                        intent.putExtra("URL",currentTopic.getImgg());
                        intent.putExtra("ID",currentTopic.getTopicID());
                        startActivity(intent);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}

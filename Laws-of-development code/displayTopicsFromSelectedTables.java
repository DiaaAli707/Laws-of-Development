
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class displayTopicsFromSelectedTables extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_topics_from_selected_tables);
        Bundle bundle = getIntent().getExtras();
        String name = bundle.getString("name"),
                id = bundle.getString("ID"),
                URL = bundle.getString("URL");

        getSupportActionBar().setTitle(name);
        TextView tID = findViewById(R.id.topicArticle);
        ImageView iPic = findViewById(R.id.topicPic);
        tID.setText(id);
        Glide.with(getBaseContext()).load(URL).into(iPic);

    }
}

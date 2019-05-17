
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class customTopicListAdapter extends ArrayAdapter<topic> {

    public customTopicListAdapter(Context context, ArrayList<topic> topicList) {
        super(context, 0,topicList);
    }

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View topicList = convertView;
        if(topicList == null)
            topicList = LayoutInflater.from(getContext()).inflate(R.layout.list_items,parent,false);

        topic currentTopic = getItem(position);
        ImageView image = topicList.findViewById(R.id.image);
        Glide.with(getContext()).load(currentTopic.getImgg()).into(image);
        TextView name = topicList.findViewById(R.id.name);
        name.setText(currentTopic.getName());
        TextView id = topicList.findViewById(R.id.ID);
        id.setText("ID: "+ currentTopic.getTopicID());
        return topicList;
    }
}






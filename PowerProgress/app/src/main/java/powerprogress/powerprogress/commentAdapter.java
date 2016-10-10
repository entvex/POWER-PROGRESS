package powerprogress.powerprogress;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * Created by entvex on 06-10-2016.
 */

public class commentAdapter extends BaseAdapter {

    Context context;
    List<CommentDTO> commentDTOs;
    CommentDTO commentDTO;

    public commentAdapter(Context context, List<CommentDTO> commentDTOs)
    {
        this.context   = context;
        this.commentDTOs = commentDTOs;
    }

    @Override
    public int getCount() {
        return commentDTOs.size();
    }

    @Override
    public Object getItem(int position) {
        if (commentDTOs !=null)
        {
            return commentDTOs.get(position);
        } else {
            return 0;
        }
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null )
        {
            LayoutInflater InflaterReminder = (LayoutInflater) this.context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = InflaterReminder.inflate(R.layout.comment_list_item,null);
        }
        commentDTO = commentDTOs.get(position);

        //Filling a item
        if(commentDTO != null)
        {
            TextView ttv_comment_commentListItem = (TextView) convertView.findViewById(R.id.ttv_comment_commentListItem);
            ttv_comment_commentListItem.setText(commentDTO.getComment());

            TextView ttv_author_commentListItem = (TextView) convertView.findViewById(R.id.ttv_author_commentListItem);
            ttv_author_commentListItem.setText(commentDTO.getAuthor());


            String rightNow = new SimpleDateFormat("dd-MM-yy").format(Calendar.getInstance().getTime());
            String commentTime = commentDTO.getTimestamp().substring(0,8);

            TextView ttv_timestamp_commentListItem = (TextView) convertView.findViewById(R.id.ttv_timestamp_commentListItem);

            if(rightNow.equals(commentTime))
            {
                ttv_timestamp_commentListItem.setText("Today " + commentDTO.getTimestamp().substring(9));
            }
            else {
                ttv_timestamp_commentListItem.setText(commentDTO.getTimestamp());
            }

        }
        return convertView;
    }
}
package powerprogress.powerprogress;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

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
            //convertView = InflaterReminder.inflate(R.layout.LAYOUTTOUSE,null);
        }
        commentDTO = commentDTOs.get(position);

        //Filling a item
        if(commentDTO != null)
        {

        }
        return convertView;
    }
}
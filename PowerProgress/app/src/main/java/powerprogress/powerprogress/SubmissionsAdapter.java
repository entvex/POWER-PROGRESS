package powerprogress.powerprogress;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by entvex on 06-10-2016.
 */

public class SubmissionsAdapter extends BaseAdapter {

    Context context;
    List<UploadDTO> uploadDTOs;
    UploadDTO uploadDTO;

    public SubmissionsAdapter(Context context, List<UploadDTO> uploadDTOs)
    {
        this.context   = context;
        this.uploadDTOs = uploadDTOs;
    }

    @Override
    public int getCount() {
        return uploadDTOs.size();
    }

    @Override
    public Object getItem(int position) {
        if (uploadDTOs !=null)
        {
            return uploadDTOs.get(position);
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
            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.video_list_item,null);
        }
        uploadDTO = uploadDTOs.get(position);

        //Filling a item
        if(uploadDTO != null)
        {
            TextView ttv_videoTitle_videoListItem = (TextView) convertView.findViewById(R.id.ttv_videoTitle_videoListItem);
            ttv_videoTitle_videoListItem.setText(uploadDTO.getTitel());

            TextView ttv_description_videoListItem = (TextView) convertView.findViewById(R.id.ttv_description_videoListItem);
            ttv_description_videoListItem.setText(uploadDTO.getDescription());
        }
        return convertView;
    }

}

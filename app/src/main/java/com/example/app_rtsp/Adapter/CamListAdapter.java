package com.example.app_rtsp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.app_rtsp.Models.Camera_Link;
import com.example.app_rtsp.R;

import java.util.List;

public class CamListAdapter extends ArrayAdapter<Camera_Link> {
    private Context mContext;
    private int mResource;
    private Animation translate_anim;

    public CamListAdapter(Context context, int resource, List<Camera_Link> objects){
        super(context, resource, objects);
        this.mContext = context;
        this.mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String camName = getItem(position).getCaName();
        String camLink = getItem(position).getCaLink();

        Camera_Link cam = new Camera_Link(camName, camLink);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        TextView CamName = convertView.findViewById(R.id.camera_name);
        TextView CamLink = convertView.findViewById(R.id.camera_link);
        LinearLayout mainRow = convertView.findViewById(R.id.mainLayout);
        translate_anim = AnimationUtils.loadAnimation(mContext, R.anim.translate_anim);

        mainRow.setAnimation(translate_anim);
        CamName.setText(camName);
        CamLink.setText(camLink);

        return convertView;
    }
}

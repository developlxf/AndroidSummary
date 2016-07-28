package com.example.androidsummary.utils;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.android.volley.toolbox.NetworkImageView;
import com.example.androidsummary.R;

/**用于RecyclerView的性能优化
 * Created by 伦小丹 on 2015/12/22 0022.
 */
public class MyViewHolder extends RecyclerView.ViewHolder{
    private NetworkImageView networkImageView;

    public MyViewHolder(View itemView) {
        super(itemView);
        networkImageView = (NetworkImageView) itemView.findViewById(R.id.networkimageview);
    }

    public NetworkImageView getNetworkImageView() {
        return networkImageView;
    }
}

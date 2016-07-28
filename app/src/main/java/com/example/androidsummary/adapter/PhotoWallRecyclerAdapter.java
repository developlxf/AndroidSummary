package com.example.androidsummary.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.example.androidsummary.R;
import com.example.androidsummary.bean.Images;
import com.example.androidsummary.utils.BitmapCache;
import com.example.androidsummary.utils.MyViewHolder;

/**
 * Created by 伦小丹 on 2015/12/22 0022.
 */
public class PhotoWallRecyclerAdapter extends RecyclerView.Adapter<MyViewHolder> {
    private String[] imagePaths;
    private Context context;
    /**
     * 记录每个item高度的数组
     */
    private int[] sizeArray;

    /**
     * 请求队列
     */
    private RequestQueue requestQueue;

    public PhotoWallRecyclerAdapter(Context context, String[] imagePaths) {
        this.context = context;
        this.imagePaths = imagePaths;
        /**
         * 设置每张图片的高度
         */
        sizeArray = new int[Images.imagePaths.length];
        for (int i = 0; i < sizeArray.length; i++) {
            sizeArray[i] = (int) (150 + Math.random() * 300);
        }

        requestQueue = Volley.newRequestQueue(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.recyclerview_item, null);
        MyViewHolder viewHolder = new MyViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        /**
         * 设置每个item的高度
         */
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) holder.getNetworkImageView().getLayoutParams();
        layoutParams.height = sizeArray[position];

        /**
         * 给每个NetworkImageView绑定数据
         * 自定义一个ImageCache，实现加载图片时先访问内存缓存，没有再访问磁盘缓存，都没有的话再访问网络
         */
        ImageLoader imageLoader = new ImageLoader(requestQueue, new BitmapCache(context));

        holder.getNetworkImageView().setImageUrl(imagePaths[position], imageLoader);

        /**
         * 添加单击监听的回调
         */

        holder.getNetworkImageView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(holder.getNetworkImageView());
                }
            }
        });
    }

    public void setImagePaths(String[] imagePaths) {
        this.imagePaths = imagePaths;
    }

    @Override

    public int getItemCount() {
        return imagePaths.length;
    }

    /**
     * 添加单击事件的监听器
     */
    public interface OnItemClickListener {
        void onItemClick(NetworkImageView networkImageView);
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
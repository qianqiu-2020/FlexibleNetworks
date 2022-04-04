package com.example.flexiblenetworks.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


import com.example.flexiblenetworks.R;
import com.example.flexiblenetworks.define.InfoItem;

import java.util.List;

/*好友列表填充所用的适配器*/
public class InfoAdapter extends RecyclerView.Adapter<InfoAdapter.ViewHolder> {
    private Context mContext;
    private List<InfoItem> mInfoList;

    /*MsgAdapter的内部类*/
    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView infoTitle;
        TextView infoReply;

        public ViewHolder(@NonNull View itemView) {//该参数为子项的最外层布局文件
            super(itemView);
            cardView=(CardView)itemView;
            infoTitle=(TextView)itemView.findViewById(R.id.info_title);
            infoReply=(TextView)itemView.findViewById(R.id.info_reply);
        }
    }

    /*适配器的构造函数，消息列表传入适配器*/
    public InfoAdapter(List<InfoItem> InfoList) {
        mInfoList = InfoList;
    }

    /*创建ViewHolder实例*/
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(mContext==null)
        {
            mContext=parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.info_item, parent, false);//将子项的布局加载进来

        final ViewHolder holder=new ViewHolder(view);

        holder.infoTitle.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                int position=holder.getAdapterPosition();
                InfoItem infoitem=mInfoList.get(position);
                holder.infoTitle.setText(infoitem.getTitle());
                //Glide.with(mContext).load(image.getUrl()).signature(new ObjectKey(System.currentTimeMillis())).into(holder.imageImage);

            }
        });

        return holder;//并用此布局构建一个Viewholder实例
    }

    /*对子项数据进行赋值，会在每个子项被滚动到屏幕内的时候执行*/
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {//此处的holder即通过onCreateViewHolder构建出来的
        InfoItem infoitem = mInfoList.get(position);
        holder.infoTitle.setText(infoitem.getTitle());
        holder.infoReply.setText(infoitem.getReply());

        //Glide.with(mContext).load(image.getUrl()).signature(new ObjectKey(System.currentTimeMillis())).into(holder.imageImage);
        Log.d("mark1", "加载图片");
    }

    /*RecyclerView的项数*/
    @Override
    public int getItemCount() {
        return mInfoList.size();
    }

}

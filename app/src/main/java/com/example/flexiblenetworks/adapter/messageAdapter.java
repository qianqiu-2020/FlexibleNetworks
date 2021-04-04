package com.example.flexiblenetworks.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.flexiblenetworks.R;
import com.example.flexiblenetworks.define.messageItem;

import java.util.List;

public class messageAdapter extends ArrayAdapter<messageItem> {
    private int resourceId;//每一项所用的布局，在实例化适配器时传入
    public messageAdapter(Context context, int textViewRourceId, List<messageItem> objects){
        super(context,textViewRourceId,objects);
        resourceId=textViewRourceId;
    }
    /*listview中每一项将要可见时，调用此方法加载内容，此方法调用一次加载一项的内容*/
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        messageItem Onmessage=getItem(position);//获取适配器对应的List(即构造函数中传入的objects)中的一项数据
        View view;//listview中一项的模板，注释掉的内容为采用固定数量的模板，而当前则是每一项都新造一个模板
//        if(convertView==null){
        view= LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
        Log.d("mark1", String.valueOf(view.hashCode()));
  /*      }
        else {
            view=convertView;
            Log.d("mark2", String.valueOf(view.hashCode()));
        }*/

        /*获取每一项中的控件*/
        ImageView messageImage=(ImageView)view.findViewById(R.id.message_image);
        TextView messageName=(TextView)view.findViewById(R.id.message_name);
        TextView messageContent=(TextView)view.findViewById(R.id.message_content);
        //EditText editText=(EditText)view.findViewById(R.id.edit_text);
        /*把从适配器对应的List中取到的数据设置到控件中*/
        messageImage.setImageResource(Onmessage.getImageId());
        messageName.setText(Onmessage.getName());
        messageContent.setText(Onmessage.getPartMessage());
        //editText.setText(friend.getName());

        return view;
    }

}

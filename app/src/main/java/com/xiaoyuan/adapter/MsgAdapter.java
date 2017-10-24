package com.xiaoyuan.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xiaoyuan.Class.Chat;
import com.xiaoyuan.longer.R;

import java.util.List;

/**
 * Created by Axu on 2016/8/6.
 */
public class MsgAdapter extends ArrayAdapter<Chat> {
    private int resourceId;
    public MsgAdapter(Context context, int textViewResourceId, List<Chat>objects){
        super(context,textViewResourceId,objects);
        resourceId=textViewResourceId;
    }
    public View getView(int position, View convertView, ViewGroup parent){
        Chat chat=getItem(position);
        View view;
       ViewHolder viewHolder;
        if(convertView==null) {
            view= LayoutInflater.from(getContext()).inflate(resourceId,null);
            viewHolder=new ViewHolder();
            viewHolder.leftLayout=(LinearLayout)view.findViewById(R.id.left_layout);
            viewHolder.rightLayout=(LinearLayout)view.findViewById(R.id.right_layout);
            viewHolder.tv_leftMsg=(TextView)view.findViewById(R.id.tv_msg_receve);
            viewHolder.tv_rightMsg=(TextView)view.findViewById(R.id.tv_msg_send);
            view.setTag(viewHolder);
        }else{
            view=convertView;
            viewHolder=(ViewHolder)view.getTag();
        }
        if(chat.getType()==Chat.TYPE_RECEVED){
            viewHolder.leftLayout.setVisibility(View.VISIBLE);
            viewHolder.rightLayout.setVisibility(View.GONE);
            viewHolder.tv_leftMsg.setText(chat.getContent());
        }else if(chat.getType()==Chat.TYPE_SEND){
            viewHolder.rightLayout.setVisibility(View.VISIBLE);
            viewHolder.leftLayout.setVisibility(View.GONE);
            viewHolder.tv_rightMsg.setText(chat.getContent());
        }
        return view;
    }
    class ViewHolder{
        LinearLayout leftLayout;
        LinearLayout rightLayout;
        TextView tv_leftMsg;
        TextView tv_rightMsg;
    }
}

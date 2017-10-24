package com.xiaoyuan.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.xiaoyuan.Class.Chat;
import com.xiaoyuan.Class.Pyq;
import com.xiaoyuan.longer.R;

import java.util.List;

/**
 * Created by Axu on 2016/8/6.
 */
public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {
    public static Context context;
    public List<Chat> chats;
    public ViewGroup viewGroup;
    public static ViewHolder viewHolder;

    private int chatLayout;

    public ChatAdapter(List<Chat> chats, Context chatLayout) {
        this.chats = chats;
        this.context = chatLayout;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.msg_item, parent, false);
        return new ViewHolder(v);
    }

    public void onBindViewHolder(ViewHolder holder, int position) {
        Chat chat = chats.get(position);
//        holder.text.setText(chat.getText());
//        holder.image.setImageBitmap(null);
//        Picasso.with(holder.image.getContext()).cancelRequest(holder.image);
//        Picasso.with(holder.image.getContext()).load(chat.getImage()).into(holder.image);
//        holder.chatView.setTag(chat);

        if (chat.getType() == Chat.TYPE_RECEVED) {
            holder.leftLayout.setVisibility(View.VISIBLE);
            holder.rightLayout.setVisibility(View.GONE);
            holder.tv_leftMsg.setText(chat.getContent());
            holder.tv_tmrece.setText("2016-8-6");

        } else if (chat.getType() == Chat.TYPE_SEND) {
            holder.rightLayout.setVisibility(View.VISIBLE);
            holder.leftLayout.setVisibility(View.GONE);
            holder.tv_rightMsg.setText(chat.getContent());
            holder.tv_tmsend.setText("2016-8-6");
        }
        holder.itemView.setTag(chat);


    }

    public int getItemCount() {
        return chats.size();
    }




    public static class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout leftLayout;
        LinearLayout rightLayout;
        TextView tv_leftMsg;
        TextView tv_rightMsg;
        TextView tv_tmrece;
        TextView tv_tmsend;

        public ViewHolder(View chatView) {
            super(chatView);

            leftLayout = (LinearLayout) chatView.findViewById(R.id.left_layout);
            rightLayout = (LinearLayout) chatView.findViewById(R.id.right_layout);
            tv_leftMsg = (TextView) chatView.findViewById(R.id.tv_msg_receve);
            tv_rightMsg = (TextView) chatView.findViewById(R.id.tv_msg_send);
            tv_tmrece=(TextView)chatView.findViewById(R.id.tv_msg_tmre);
            tv_tmsend=(TextView)chatView.findViewById(R.id.tv_msg_tmsend);

        }
    }

}

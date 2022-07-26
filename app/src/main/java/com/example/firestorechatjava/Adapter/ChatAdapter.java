package com.example.firestorechatjava.Adapter;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firestorechatjava.Models.ChatMessage;
import com.example.firestorechatjava.databinding.ItemContainerRecieveMessageBinding;
import com.example.firestorechatjava.databinding.ItemContainerSentMessageBinding;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private final List<ChatMessage>chatMessages;
    private  Bitmap receiverprofileImage;
private final String senderId;

public static final int VIEW_TYPE_SENT =1;
public static final int VIEW_TYPE_RECIEVE =2;

public void setReceiverprofileImage(Bitmap bitmap){
    receiverprofileImage =bitmap;
}

    public ChatAdapter(List<ChatMessage> chatMessages, Bitmap receiverprofileImage, String senderId) {
        this.chatMessages = chatMessages;
        this.receiverprofileImage = receiverprofileImage;
        this.senderId = senderId;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType==VIEW_TYPE_SENT){
            return new SendMessageViewholder(ItemContainerSentMessageBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
        }else{
            return new RecievedMessageViewholder(ItemContainerRecieveMessageBinding.inflate(LayoutInflater.from( parent.getContext()),parent,false));
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
if(getItemViewType(position) ==VIEW_TYPE_SENT){
    ((SendMessageViewholder)holder).setdata(chatMessages.get(position));
}else{
    ((RecievedMessageViewholder)holder).setdata(chatMessages.get(position),receiverprofileImage);
}
    }

    @Override
    public int getItemCount() {
        return chatMessages.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (chatMessages.get(position).senderId.equals(senderId)) {
return VIEW_TYPE_SENT;
        } else {
return VIEW_TYPE_RECIEVE;
        }
    }
    static class SendMessageViewholder extends RecyclerView.ViewHolder{

        private final ItemContainerSentMessageBinding binding;

        SendMessageViewholder(ItemContainerSentMessageBinding itemContainerSentMessageBinding){
            super(itemContainerSentMessageBinding.getRoot());
            binding=itemContainerSentMessageBinding;
        }
        void setdata(ChatMessage chatMessage){
      binding.textdatetime.setText(chatMessage.datetime);
      binding.textmessage.setText(chatMessage.message);

        }

    }
    static class RecievedMessageViewholder extends RecyclerView.ViewHolder{
        private final ItemContainerRecieveMessageBinding binding1;

        RecievedMessageViewholder( ItemContainerRecieveMessageBinding itemContainerRecieveMessageBinding){
            super(itemContainerRecieveMessageBinding.getRoot());
            binding1=itemContainerRecieveMessageBinding;
        }
        void setdata(ChatMessage chatMessage,Bitmap receiverProfileImage){
            binding1.textRecieveMessage.setText(chatMessage.message);
            binding1.textdateTime.setText(chatMessage.datetime);
            if (receiverProfileImage != null) {
                binding1.imageprofile.setImageBitmap(receiverProfileImage);
            }
        }
    }

}

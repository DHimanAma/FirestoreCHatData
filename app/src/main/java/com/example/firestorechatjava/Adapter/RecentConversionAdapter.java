package com.example.firestorechatjava.Adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firestorechatjava.Models.ChatMessage;
import com.example.firestorechatjava.Models.User;
import com.example.firestorechatjava.databinding.ItemContainerRecentuserBinding;
import com.example.firestorechatjava.databinding.ItemContainerUserBinding;
import com.example.firestorechatjava.listner.ConversionListner;

import java.util.List;

public class RecentConversionAdapter extends RecyclerView.Adapter<RecentConversionAdapter.ConversionViewholder> {
    private final List<ChatMessage>chatMessages;
private final ConversionListner conversionListner;
    public RecentConversionAdapter(List<ChatMessage> chatMessages,ConversionListner conversionListner) {
        this.chatMessages = chatMessages;
        this.conversionListner=conversionListner;
    }

    @NonNull
    @Override
    public ConversionViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ConversionViewholder(ItemContainerRecentuserBinding.inflate(   LayoutInflater.from(parent.getContext()),
                parent,false));

    }

    @Override
    public void onBindViewHolder(@NonNull ConversionViewholder holder, int position) {
holder.savedata(chatMessages.get(position));
    }

    @Override
    public int getItemCount() {
        return chatMessages.size();
    }


    class ConversionViewholder extends RecyclerView.ViewHolder{

      ItemContainerRecentuserBinding binding;

      ConversionViewholder(ItemContainerRecentuserBinding itemContainerRecentuserBinding){
          super(itemContainerRecentuserBinding.getRoot());
          binding=itemContainerRecentuserBinding;
  }
  void savedata(ChatMessage chatMessage){
      binding.imageProfile.setImageBitmap(getUSerimage(chatMessage.conversionImage));
      binding.textName.setText(chatMessage.conversionName);
      binding.textRecentMessage.setText(chatMessage.message);
      binding.getRoot().setOnClickListener(view -> {
          User user=new User();
          user.id=chatMessage.conversionId;
          user.name=chatMessage.conversionName;
          user.image=chatMessage.conversionImage;
          conversionListner.ConversionOnClicked(user);
      });
      }
  }
    public Bitmap getUSerimage(String encodedImage){
        byte[]bytes = Base64.decode(encodedImage,Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes,0,bytes.length);

    }
}

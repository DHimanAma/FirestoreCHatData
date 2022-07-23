package com.example.firestorechatjava.Adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firestorechatjava.Models.User;
import com.example.firestorechatjava.databinding.ItemContainerUserBinding;
import com.example.firestorechatjava.listner.UserListner;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder>{
     private final List<User>users;
     ItemContainerUserBinding binding;
private UserListner userListner;
     public UserAdapter(List<User> users,UserListner userListner) {
          this.users = users;
          this.userListner=userListner;
     }

     @NonNull
     @Override
     public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
ItemContainerUserBinding itemContainerUserBinding =ItemContainerUserBinding.inflate(
        LayoutInflater.from(parent.getContext()),
        parent,false
);
return  new UserViewHolder(itemContainerUserBinding);
     }

     @Override
     public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
         holder.setUserData(users.get(position));


     }

     @Override
     public int getItemCount() {
          return users.size();
     }

     class UserViewHolder extends RecyclerView.ViewHolder{
        UserViewHolder(ItemContainerUserBinding itemContainerUserBinding){
             super(itemContainerUserBinding.getRoot());
             binding=itemContainerUserBinding;

          }
          void setUserData(User user){
              Log.e("sdks","dsajdkjadk"+user.name);
             binding.textName.setText(user.name);
             binding.textEmail.setText(user.email);
             binding.imageProfile.setImageBitmap(getUSerimage(user.image));
             binding.getRoot().setOnClickListener(view -> userListner.onUserCliked(user));
          }
     }



     private Bitmap getUSerimage(String encodedImage){
          byte[]bytes = Base64.decode(encodedImage,Base64.DEFAULT);
          return BitmapFactory.decodeByteArray(bytes,0,bytes.length);

     }
}

package com.example.firestorechatjava.Fragment;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.VideoView;

import com.example.firestorechatjava.R;
import com.example.firestorechatjava.activities.OnBoarding;


public class SecondFragment extends Fragment {
    View view;
    VideoView videoView1;
    Context context;
    MediaController mediaController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_second, container, false);
        videoView1 = view.findViewById(R.id.videoView);


        if (videoView1 != null) {

            // videoView1.setVisibility(View.VISIBLE);

//    public void onResume() {
//        super.onResume();
            mediaController = new MediaController(getActivity());
            mediaController.setAnchorView(videoView1);
            videoView1.setMediaController(mediaController);


            String video_url = "android.resource://" + getActivity().getPackageName() + "/" + R.raw.anim2;

            Uri videoUri = Uri.parse(video_url);

            mediaController.setAnchorView(videoView1);
            videoView1.setMediaController(mediaController);
            videoView1.setVideoURI(videoUri);
            videoView1.requestFocus();
            videoView1.start();
//            videoView1.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//                @Override
//                public void onCompletion(MediaPlayer mp) {
//                    OnBoarding.pager.setCurrentItem(2);
//                }
//            });
        } else {

            if (videoView1 != null) {
                videoView1.stopPlayback();
                videoView1.setVisibility(View.GONE);
            }
        }
        return view;
    }
    public void onResume() {
        super.onResume();
        if(videoView1 != null)
            videoView1.start();
    }
    @Override
    public void onPause() {
        super.onPause();
        if (videoView1 != null)
            videoView1.stopPlayback();
    }
}
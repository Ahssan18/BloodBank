
package com.ommi.bloodbank.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;

import androidx.fragment.app.Fragment;

import com.ommi.bloodbank.Activities.PublicMessegesActivity;
import com.ommi.bloodbank.Activities.RequestAnnouncementActivity;
import com.ommi.bloodbank.Activities.RequsetBloodActivity;
import com.ommi.bloodbank.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class  RequestFragment extends Fragment {

    private View view;
    private CircleImageView cardChat, cardAnnouncement;
    FrameLayout framePublicmessege,frameRequests;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_request, container, false);
        init();
        getActivity().findViewById(R.id.spinner_land).setVisibility(view.GONE);
        clickListener();
        return view;
    }

    private void clickListener() {
        cardChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), RequsetBloodActivity.class);
                startActivity(intent);
            }
        });
        cardAnnouncement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PublicMessegesActivity.class);
                startActivity(intent);
            }
        });


    }


    private void init() {
        framePublicmessege=view.findViewById(R.id.frame_publicmessege);
        frameRequests=view.findViewById(R.id.frame_requests);
        cardChat = view.findViewById(R.id.card_chats);
        cardAnnouncement = view.findViewById(R.id.card_announcement);
        ScaleAnimation scal=new ScaleAnimation(0, 1f, 0, 1f, Animation.RELATIVE_TO_SELF, (float)0.5,Animation.RELATIVE_TO_SELF, (float)0.5);
        scal.setDuration(1500);
        scal.setFillAfter(true);
        framePublicmessege.setAnimation(scal);
        frameRequests.setAnimation(scal);

    }
}

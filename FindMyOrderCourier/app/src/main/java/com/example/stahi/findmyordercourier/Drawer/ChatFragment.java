package com.example.stahi.findmyordercourier.Drawer;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

import com.example.stahi.findmyordercourier.API.RestService;
import com.example.stahi.findmyordercourier.Adapters.ChatRecycleAdaptor;
import com.example.stahi.findmyordercourier.ChatCore.ChatContract;
import com.example.stahi.findmyordercourier.ChatCore.ChatPresenter;
import com.example.stahi.findmyordercourier.Models.Chat;
import com.example.stahi.findmyordercourier.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatFragment extends Fragment implements ChatContract.View, TextView.OnEditorActionListener
{
    private static final String TAG = "ChatInteractor";

    RestService restService;

    private RecyclerView mRecyclerViewChat;
    private TextView EditTextMessage;

    private ChatRecycleAdaptor mChatRecyclerAdapter;

    private ChatPresenter mChatPresenter;

    @Override
    public void onResume() {
        NavigationView navigationView = ((MainActivity) getActivity()).navigationView;
        navigationView.getMenu().getItem(4).setChecked(true);
        super.onResume();
    }

    public ChatFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        NavigationView navigationView = ((MainActivity) getActivity()).navigationView;
        navigationView.getMenu().getItem(4).setChecked(true);
        ((MainActivity) getActivity()).setActionBarTitle("Chat");
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chat, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        restService = new RestService();
        EditTextMessage = getActivity().findViewById(R.id.edit_text_message);
        mRecyclerViewChat = getActivity().findViewById(R.id.recycler_view_chat);

        EditTextMessage.setOnEditorActionListener(this);
        mChatPresenter = new ChatPresenter(this);

        //pt scroll cand apare/dispare tastatura in chat
        mRecyclerViewChat.addOnLayoutChangeListener((v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom) -> {
            if (bottom < oldBottom) {
                mRecyclerViewChat.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(mRecyclerViewChat.getAdapter() != null){
                            if (mRecyclerViewChat.getAdapter().getItemCount() > 1) {
                                mRecyclerViewChat.smoothScrollToPosition(mRecyclerViewChat.getAdapter().getItemCount() - 1);
                            }
                        }
                    }
                }, 100);
            }
        });


        Bundle arguments = getArguments();
        String otherUserToken = arguments.getString("otherUserToken");



        mChatPresenter.getMessage(FirebaseAuth.getInstance().getCurrentUser().getUid(), otherUserToken);

    }



    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEND) {
            EditTextMessage = getActivity().findViewById(R.id.edit_text_message);
            sendMessage();
            return true;
        }
        return false;
    }

    private void sendMessage() {

        String message = EditTextMessage.getText().toString();
        if(!message.equals("")){

            SharedPreferences sharedPref = getActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
            Bundle arguments = getArguments();

            //sharedPrefs
            String senderUid = sharedPref.getString("token", "0");
            String sender = sharedPref.getString("email", "email");
            String senderStringId = sharedPref.getString("id", "0");
            String senderName = sharedPref.getString("name", "0");

            //arguments
            String receiver = arguments.getString("otherUserEmail");
            String receiverUid = arguments.getString("otherUserToken");
            String receiverFirebaseToken = arguments.getString("otheruserFirebaseToken");
            String receiverName = arguments.getString("userName");
            String receiverId = arguments.getString("otherUserId");


            final Chat chat = new Chat(sender, receiver, senderUid, receiverUid, message, System.currentTimeMillis(), FirebaseInstanceId.getInstance().getToken(), receiverFirebaseToken, senderName, receiverName);

            if (mChatRecyclerAdapter == null) {
                chat.IsChatEmpty = true;
            }

            restService = new RestService();

            restService.getService().SaveMessageToDatabase(Integer.parseInt(senderStringId), message, Integer.parseInt(receiverId)).enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response.isSuccessful()) {
                    }
                }
                ///aici intra mereu pe Fail dar merge introducerea in baza
                @Override
                public void onFailure(Call<String> call, Throwable t) {
                }
            });

            mChatPresenter.sendMessage(getActivity().getApplicationContext(), chat, receiverFirebaseToken);
        }
    }


    @Override
    public void onSendMessageSuccess() {
        EditTextMessage.setText("");
        //Toast.makeText(getActivity(), "Message sent", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSendMessageFailure(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onGetMessagesSuccess(Chat chat) {

        if (mChatRecyclerAdapter == null) {
            mChatRecyclerAdapter = new ChatRecycleAdaptor(new ArrayList<Chat>());
            mRecyclerViewChat.setAdapter(mChatRecyclerAdapter);
        }

        mChatRecyclerAdapter.add(chat);
        mRecyclerViewChat.smoothScrollToPosition(mChatRecyclerAdapter.getItemCount() - 1);

    }

    @Override
    public void onGetMessagesFailure(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }
}

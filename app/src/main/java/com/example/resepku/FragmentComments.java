package com.example.resepku;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.Collections;


public class FragmentComments extends Fragment {
    ArrayList<Comment> listComment;
    RecyclerView rvComments;
    MaterialButton btnCommentSend;
    EditText txtCommentsInput;
    CommentsAdapter commentsAdapter;

    public FragmentComments() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listComment = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_comments, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvComments = view.findViewById(R.id.rvComments);
        btnCommentSend = view.findViewById(R.id.btnCommentSend);
        txtCommentsInput = view.findViewById(R.id.txtCommentsInput);

        loadCommentsData();
        commentsAdapter = new CommentsAdapter(getContext(), listComment);
        rvComments.setLayoutManager(new LinearLayoutManager(getContext()));
        rvComments.setAdapter(commentsAdapter);

        btnCommentSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendComment();
            }
        });
    }

    private void sendComment() {
        String commentText = txtCommentsInput.getText().toString();
        Comment comment = new Comment("User", commentText);
        listComment.add(0, comment);
        refreshComments();
    }

    private void refreshComments() {
        txtCommentsInput.setText("");
        commentsAdapter.notifyDataSetChanged();
    }

    private void loadCommentsData() {
        listComment.add(new Comment("Naruto", "lorem ipsum"));
        listComment.add(new Comment("Hinata", "dolor sit amet"));
    }
}
package com.example.resepku;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Comment> comments;
    Fragment fragment;

    public CommentsAdapter(Context context, ArrayList<Comment> comments) {
        this.context = context;
        this.comments = comments;
    }

    public CommentsAdapter(Context context, Fragment tempFragment, ArrayList<Comment> comments) {
        this.context = context;
        this.comments = comments;
        this.fragment = tempFragment;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Comment comment = comments.get(position);
        String idxComment = "" + position;
        holder.txtItemCommentName.setText(comment.getName());
        holder.txtItemCommentText.setText(comment.getText());
        holder.txtItemCommentDate.setText(comment.getDate());

        if (comment.getUser_id() == ((FragmentComments) fragment).getUserLogin().getId()) {
            holder.btnItemCommentDelete.setVisibility(View.VISIBLE);
            holder.btnItemCommentDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (fragment != null) {
                        ((FragmentComments) fragment).deleteComment(Integer.parseInt(idxComment), comment.getId());
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgItemComment;
        TextView txtItemCommentName, txtItemCommentText, txtItemCommentDate;
        MaterialButton btnItemCommentDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgItemComment = itemView.findViewById(R.id.imgItemComment);
            txtItemCommentName = itemView.findViewById(R.id.txtItemCommentName);
            txtItemCommentText = itemView.findViewById(R.id.txtItemCommentText);
            txtItemCommentDate = itemView.findViewById(R.id.txtItemCommentDate);
            btnItemCommentDelete = itemView.findViewById(R.id.btnItemCommentDelete);
        }
    }
}

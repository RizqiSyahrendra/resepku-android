package com.example.resepku;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Comment> comments;

    public CommentsAdapter(Context context, ArrayList<Comment> comments) {
        this.context = context;
        this.comments = comments;
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
        holder.txtItemCommentName.setText(comment.getName());
        holder.txtItemCommentText.setText(comment.getText());
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgItemComment;
        TextView txtItemCommentName, txtItemCommentText, txtItemCommentDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgItemComment = itemView.findViewById(R.id.imgItemComment);
            txtItemCommentName = itemView.findViewById(R.id.txtItemCommentName);
            txtItemCommentText = itemView.findViewById(R.id.txtItemCommentText);
            txtItemCommentDate = itemView.findViewById(R.id.txtItemCommentDate);
        }
    }
}

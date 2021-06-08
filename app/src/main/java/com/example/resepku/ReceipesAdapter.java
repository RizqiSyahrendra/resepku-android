package com.example.resepku;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ReceipesAdapter extends RecyclerView.Adapter<ReceipesAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Meal> listMeal;

    public ReceipesAdapter(Context context, ArrayList<Meal> listMeal) {
        this.context = context;
        this.listMeal = listMeal;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_receipe, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Meal meal = listMeal.get(position);
        holder.txtItemReceipeName.setText(meal.getName());
        holder.imgItemReceipe.setVisibility(View.VISIBLE);
        Picasso.get().load(meal.getImage()).placeholder(R.drawable.food_placeholder).into(holder.imgItemReceipe);

        holder.cardItemReceipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.viewDetail("1");
            }
        });
    }

    @Override
    public int getItemCount() {
        return listMeal.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgItemReceipe;
        TextView txtItemReceipeName;
        CardView cardItemReceipe;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgItemReceipe = itemView.findViewById(R.id.imgItemReceipe);
            txtItemReceipeName = itemView.findViewById(R.id.txtItemReceipeName);
            cardItemReceipe = itemView.findViewById(R.id.cardItemReceipe);
        }

        public void viewDetail(String idMeal) {
            Intent intentDetail = new Intent(itemView.getContext(), DetailActivity.class);
            itemView.getContext().startActivity(intentDetail);
        }
    }
}

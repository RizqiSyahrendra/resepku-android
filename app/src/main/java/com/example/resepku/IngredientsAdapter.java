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

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Ingredient> listIngredient;

    public IngredientsAdapter(Context context, ArrayList<Ingredient> listIngredient) {
        this.context = context;
        this.listIngredient = listIngredient;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ingredient, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Ingredient ingredient = listIngredient.get(position);
        String ingredientName = ingredient.getMeasure() + " " + ingredient.getName();
        holder.txtItemIngredientName.setText(ingredientName);
    }

    @Override
    public int getItemCount() {
        return listIngredient.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgItemIngredient;
        TextView txtItemIngredientName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgItemIngredient = itemView.findViewById(R.id.imgItemIngredient);
            txtItemIngredientName = itemView.findViewById(R.id.txtItemIngredientName);
        }
    }
}

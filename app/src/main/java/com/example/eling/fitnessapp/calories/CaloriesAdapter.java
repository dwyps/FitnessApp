package com.example.eling.fitnessapp.calories;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.eling.fitnessapp.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;

public class CaloriesAdapter extends RecyclerView.Adapter<CaloriesAdapter.CaloriesViewHolder> {

    private CalorieClickListener calorieClickListener;
    private final List<CalorieDetails> calorieDetailsList;

    public CaloriesAdapter() {
        calorieDetailsList = new ArrayList<>();
    }

    public CaloriesAdapter(CalorieClickListener calorieClickListener, List<CalorieDetails> calorieDetailsList) {
        this.calorieClickListener = calorieClickListener;
        this.calorieDetailsList = calorieDetailsList;
    }

    public void setCalorieDetailsList(List<CalorieDetails> calorieDetails){
        if(calorieDetails!=null){
            this.calorieDetailsList.clear();
            this.calorieDetailsList.addAll(calorieDetails);
            notifyDataSetChanged();
        }
    }

    @NonNull
    @Override
    public CaloriesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.calorie_item, parent,false);
        return new CaloriesViewHolder(view, calorieClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull CaloriesViewHolder holder, int position) {

        CalorieDetails calorieDetails = calorieDetailsList.get(position);
        holder.bind(calorieDetails);

    }

    @Override
    public int getItemCount() {
        return calorieDetailsList.size();
    }

    public class CaloriesViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.textView_date)
        TextView textView_date;
        @BindView(R.id.totalCalories)
        TextView textView_totalCalories;


        public CaloriesViewHolder(@NonNull View itemView, CalorieClickListener calorieClickListener) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        public void bind(CalorieDetails calorieDetails){
            textView_date.setText(calorieDetails.getDate());
            textView_totalCalories.setText("Total calories: "+calorieDetails.getTotalCalories()+"kcal");
        }

        @OnClick
        public void onItemClick(){
            calorieClickListener.onClick(calorieDetailsList.get(getAdapterPosition()));
        }

        @OnLongClick
        public boolean onItemLongClick(){
            calorieClickListener.onLongClick(calorieDetailsList.get(getAdapterPosition()));
            return true;
        }
    }
}

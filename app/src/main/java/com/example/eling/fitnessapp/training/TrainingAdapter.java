package com.example.eling.fitnessapp.training;

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

public class TrainingAdapter extends RecyclerView.Adapter<TrainingAdapter.TrainingViewHolder> {

    private TrainingClickListener trainingClickListener;
    private final List<TrainingDetails> trainingDetailsList;

    public TrainingAdapter() {
        trainingDetailsList = new ArrayList<>();
    }

    public TrainingAdapter(TrainingClickListener trainingClickListener, List<TrainingDetails> trainingDetails) {
        this.trainingClickListener = trainingClickListener;
        this.trainingDetailsList = trainingDetails;
    }

    public void setTrainingDetailsList(List<TrainingDetails> details) {
        if (details != null) {
            this.trainingDetailsList.clear();
            this.trainingDetailsList.addAll(details);
            notifyDataSetChanged();
        }
    }


    @NonNull
    @Override
    public TrainingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.training_item, parent, false);
        return new TrainingViewHolder(view, trainingClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull TrainingViewHolder holder, int position) {

        TrainingDetails trainingDetails = trainingDetailsList.get(position);
        holder.bind(trainingDetails);

    }

    @Override
    public int getItemCount() {
        return trainingDetailsList.size();
    }

    public class TrainingViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.textView_dateTrainning)
        TextView textView_dateTraining;

        public TrainingViewHolder(@NonNull View itemView, TrainingClickListener clickListener) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(TrainingDetails trainingDetails) {
            textView_dateTraining.setText(trainingDetails.getDate());
        }

        @OnClick
        public void onItemClick() {
            trainingClickListener.onClick(trainingDetailsList.get(getAdapterPosition()));
        }

        @OnLongClick
        public boolean onItemLongClick() {
            trainingClickListener.onLongClick(trainingDetailsList.get(getAdapterPosition()));
            return true;
        }
    }
}

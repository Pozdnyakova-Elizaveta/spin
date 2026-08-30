package com.example.spin.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spin.R;
import com.example.spin.entities.Exercise;

import java.util.List;

/**
 * Адаптер для отображения списка упражнений
 */
public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.ViewHolder> {
    //Список упражнений
    private final List<Exercise> exercises;
    //Слушатель удаления
    private final OnExerciseDeleteListener deleteListener;

    /**
     * callback-интерфейс для передачи событий из адаптера в Activity
     */
    public interface OnExerciseDeleteListener {
        void onDelete(int position);
    }

    public ExerciseAdapter(List<Exercise> exercises, OnExerciseDeleteListener deleteListener) {
        this.exercises = exercises;
        this.deleteListener = deleteListener;
    }

    /**
     * Создание view holder
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_exercise, parent, false);
        return new ViewHolder(view);
    }

    /**
     * Привязка данных
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Exercise exercise = exercises.get(position);
        //устанвока значений
        holder.tvPosition.setText(getPositionName(exercise.getPosition()));
        holder.tvDuration.setText("Продолжительность: " + formatDuration(exercise.getDuration()));
        holder.tvResistance.setText("Сопротивление: " + exercise.getResistance());
        //обработка клика на кнопку удаления
        holder.btnDelete.setOnClickListener(v -> {
            if (deleteListener != null) {
                deleteListener.onDelete(position);
            }
        });
    }

    /**
     * Приведение вывода времени к мин и сек
     */
    private String formatDuration(int seconds) {
        int minutes = seconds / 60;
        int remainingSeconds = seconds % 60;

        if (minutes == 0) {
            return remainingSeconds + " сек";
        } else if (remainingSeconds == 0) {
            return minutes + " мин";
        } else {
            return minutes + " мин " + remainingSeconds + " сек";
        }
    }

    @Override
    public int getItemCount() {
        return exercises.size();
    }

    /**
     * Локализация названий позиций
     */
    private String getPositionName(String position) {
        switch (position) {
            case "sitting": return "Сидя";
            case "sitting_tilt": return "Сидя в наклоне";
            case "standing": return "Стоя";
            case "standing_tilt": return "Стоя в наклоне";
            case "sitting_standing": return "Переход сидя-стоя";
            default: return position;
        }
    }

    /**
     * Кэшированный view-компонент
     */
    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvPosition, tvDuration, tvResistance;
        ImageView btnDelete;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPosition = itemView.findViewById(R.id.tvPosition);
            tvDuration = itemView.findViewById(R.id.tvDuration);
            tvResistance = itemView.findViewById(R.id.tvResistance);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}

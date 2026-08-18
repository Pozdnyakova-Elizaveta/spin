package com.example.spin.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spin.R;
import com.example.spin.entities.Workout;

import java.util.List;

/**
 * Адаптер для отображения списка тренировок
 */
public class WorkoutAdapter extends RecyclerView.Adapter<WorkoutAdapter.ViewHolder> {
    //Данные для отображения
    private final List<Workout> workoutList;
    //интерфейс обработки кликов
    private final OnItemClickListener listener;
    //выбранный id тренировки
    private int selectedId = -1;

    /**
     * Передача данных о клике в Activity
     */
    public interface OnItemClickListener {
        void onItemClick(Workout workout);
    }

    public WorkoutAdapter(List<Workout> workoutList, OnItemClickListener listener) {
        this.workoutList = workoutList;
        this.listener = listener;
    }

    /**
     * Создание view holder
     *
     * @return
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_workout, parent, false);
        return new ViewHolder(view);
    }

    /**
     * Привязка данных
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Workout workout = workoutList.get(position);        //получаем данные по позиции

        holder.tvWorkoutName.setText(workout.getName());
        holder.itemView.setBackgroundColor(selectedId == workout.getId() ?  //устанавливаем фон в зависимости от выбора
                holder.itemView.getContext().getColor(R.color.light_blue) :
                holder.itemView.getContext().getColor(R.color.transparent));

        holder.itemView.setOnClickListener(v -> {   //обработка клика на элемент
            selectedId = workout.getId();
            listener.onItemClick(workout);
            notifyDataSetChanged();
        });
    }

    /**
     * Получение количества элементов
     * @return количество элеиентов
     */
    @Override
    public int getItemCount() {
        return workoutList.size();
    }

    /**
     * Внешнее обновление выбора
     * @param id выбрранная тренировка
     */
    public void setSelectedId(int id) {
        this.selectedId = id;
        notifyDataSetChanged();
    }

    /**
     * Кэшированный view-компонент
     */
    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvWorkoutName;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvWorkoutName = itemView.findViewById(R.id.tvWorkoutName);
        }
    }
}
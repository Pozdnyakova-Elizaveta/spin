package com.example.spin.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spin.R;
import com.example.spin.entities.History;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Адаптер для отображения элементов истории тренировок
 */
public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {
    //Данные для отображения
    private List<History> historyList;
    //Формат даты
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault());

    public HistoryAdapter(List<History> historyList) {
        this.historyList = historyList;
    }

    /**
     * Создание view holder
     *
     * @return
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_history, //файл разметки
                        parent, //родительский контейнер, куда добавится view
                        false); //не присоединять сразу
        return new ViewHolder(view);
    }

    /**
     * Привязка данных
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        History history = historyList.get(position);    //получаем данные по позиции
        //установка данных
        String date = dateFormat.format(new Date(history.getDate()));
        holder.tvDate.setText(date);
        holder.tvWorkoutName.setText(history.getWorkoutName());

        // добавление деталей
        String details = "Упражнений: " + history.getCompletedExercises() + "/" + history.getTotalExercises();
        holder.tvDetails.setText(details);
    }

    /**
     * Получение количества элементов
     * @return количество элеиентов
     */
    @Override
    public int getItemCount() {
        return historyList.size();
    }

    /**
     * Кэшированный view-компонент
     */
    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvDate, tvWorkoutName, tvDetails;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvWorkoutName = itemView.findViewById(R.id.tvWorkoutName);
            tvDetails = itemView.findViewById(R.id.tvDetails);
        }
    }
}
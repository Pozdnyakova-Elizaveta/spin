package com.example.spin.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spin.AppDatabase;
import com.example.spin.R;
import com.example.spin.adapters.HistoryAdapter;
import com.example.spin.entities.History;

import java.util.List;
import java.util.concurrent.Executors;

/**
 * Экран истории тренировок
 */
public class HistoryActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private HistoryAdapter adapter;
    private TextView textViewEmptyHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);  // Установка UI-макета

        recyclerView = findViewById(R.id.recyclerViewHistory);
        textViewEmptyHistory = findViewById(R.id.tvEmptyHistory);
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        recyclerView.setLayoutManager(new LinearLayoutManager(this));   //элементы в виде вертикального списка

        loadHistory();
    }

    private void loadHistory() {
        Executors.newSingleThreadExecutor().execute(() -> { //запрос в фоновом потоке
            List<History> historyList = AppDatabase.getInstance(getApplicationContext())
                    .historyDao().getAllHistory();

            runOnUiThread(() -> {   //обновления ui
                if (historyList.isEmpty()) {    //нет истории тренировок
                    textViewEmptyHistory.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                } else {    //есть история тренировок
                    textViewEmptyHistory.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    adapter = new HistoryAdapter(historyList);
                    recyclerView.setAdapter(adapter);
                }
            });
        });
    }
}

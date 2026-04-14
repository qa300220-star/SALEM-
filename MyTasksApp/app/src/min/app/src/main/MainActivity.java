package com.example.mytasks;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private TaskAdapter adapter;
    private ArrayList<Task> taskList;
    private EditText editTextTask;
    private Button buttonAdd;
    private SharedPreferences sharedPreferences;
    private Gson gson;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recyclerView);
        editTextTask = findViewById(R.id.editTextTask);
        buttonAdd = findViewById(R.id.buttonAdd);
        taskList = new ArrayList<>();
        gson = new Gson();
        sharedPreferences = getSharedPreferences("tasks_prefs", MODE_PRIVATE);
        loadTasks();
        adapter = new TaskAdapter(taskList, position -> {
            taskList.remove(position);
            saveTasks();
            adapter.notifyItemRemoved(position);
        }, (position, isChecked) -> {
            taskList.get(position).setCompleted(isChecked);
            saveTasks();
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        buttonAdd.setOnClickListener(v -> {
            String taskText = editTextTask.getText().toString().trim();
            if (!taskText.isEmpty()) {
                taskList.add(0, new Task(taskText, false));
                editTextTask.setText("");
                saveTasks();
                adapter.notifyItemInserted(0);
                recyclerView.scrollToPosition(0);
            } else {
                Toast.makeText(MainActivity.this, "الرجاء كتابة مهمة", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void saveTasks() {
        String json = gson.toJson(taskList);
        sharedPreferences.edit().putString("tasks", json).apply();
    }
    private void loadTasks() {
        String json = sharedPreferences.getString("tasks", "");
        if (!json.isEmpty()) {
            Type type = new TypeToken<ArrayList<Task>>(){}.getType();
            taskList = gson.fromJson(json, type);
            if (taskList == null) taskList = new ArrayList<>();
        }
    }
}

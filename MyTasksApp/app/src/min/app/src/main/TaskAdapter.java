package com.example.mytasks;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {
    private ArrayList<Task> taskList;
    private OnDeleteClickListener deleteListener;
    private OnCheckChangeListener checkListener;
    public interface OnDeleteClickListener { void onDeleteClick(int position); }
    public interface OnCheckChangeListener { void onCheckChanged(int position, boolean isChecked); }
    public TaskAdapter(ArrayList<Task> taskList, OnDeleteClickListener deleteListener, OnCheckChangeListener checkListener) {
        this.taskList = taskList;
        this.deleteListener = deleteListener;
        this.checkListener = checkListener;
    }
    @NonNull @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Task task = taskList.get(position);
        holder.checkBox.setText(task.getText());
        holder.checkBox.setChecked(task.isCompleted());
        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            task.setCompleted(isChecked);
            checkListener.onCheckChanged(position, isChecked);
        });
        holder.buttonDelete.setOnClickListener(v -> deleteListener.onDeleteClick(position));
    }
    @Override
    public int getItemCount() { return taskList.size(); }
    static class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkBox;
        ImageButton buttonDelete;
        ViewHolder(View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.checkBoxTask);
            buttonDelete = itemView.findViewById(R.id.buttonDelete);
        }
    }
}

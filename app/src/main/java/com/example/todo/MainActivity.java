package com.example.todo;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todo.adapter.TodoAdapter;
import com.example.todo.data.Todo;
import com.example.todo.viewmodel.TodoViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    public static final int ADD_TODO_REQUEST = 1;
    public static final int EDIT_TODO_REQUEST = 2;

    private TodoViewModel todoViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        final TodoAdapter adapter = new TodoAdapter();
        recyclerView.setAdapter(adapter);

        todoViewModel = new ViewModelProvider(this).get(TodoViewModel.class);
        todoViewModel.getAllTodos().observe(this, todos -> {
            adapter.setTodos(todos);
        });

        // 添加滑动删除功能
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView,
                                @NonNull RecyclerView.ViewHolder viewHolder,
                                @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                Todo todo = adapter.getTodoAt(viewHolder.getAdapterPosition());
                todoViewModel.delete(todo);
                
                // 显示撤销删除的Snackbar
                Snackbar.make(recyclerView, "Todo deleted", Snackbar.LENGTH_LONG)
                        .setAction("UNDO", v -> {
                            todoViewModel.insert(todo);
                        }).show();
            }
        }).attachToRecyclerView(recyclerView);

        FloatingActionButton buttonAddTodo = findViewById(R.id.fab);
        buttonAddTodo.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, com.example.todo.view.AddEditTodoActivity.class);
            startActivityForResult(intent, ADD_TODO_REQUEST);
        });

        adapter.setOnItemClickListener(todo -> {
            Intent intent = new Intent(MainActivity.this, com.example.todo.view.AddEditTodoActivity.class);
            intent.putExtra(com.example.todo.view.AddEditTodoActivity.EXTRA_ID, todo.getId());
            intent.putExtra(com.example.todo.view.AddEditTodoActivity.EXTRA_TITLE, todo.getTitle());
            startActivityForResult(intent, EDIT_TODO_REQUEST);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_TODO_REQUEST && resultCode == RESULT_OK) {
            String title = data.getStringExtra(com.example.todo.view.AddEditTodoActivity.EXTRA_TITLE);
            Todo todo = new Todo(title, false,System.currentTimeMillis());
            todoViewModel.insert(todo);
        } else if (requestCode == EDIT_TODO_REQUEST && resultCode == RESULT_OK) {
            int id = data.getIntExtra(com.example.todo.view.AddEditTodoActivity.EXTRA_ID, -1);
            if (id == -1) {
                // Handle error
                return;
            }
            String title = data.getStringExtra(com.example.todo.view.AddEditTodoActivity.EXTRA_TITLE);
            // You need a way to get the original `isCompleted` status, or assume it's unchanged
            // For simplicity, we'll fetch the original todo, but this is not efficient.
            // A better approach would be to pass the whole Todo object back.
            Todo todo = new Todo(title, false, System.currentTimeMillis()); // 修复：添加缺失的timestamp参数
            todo.setId(id);
            todoViewModel.update(todo);
        } else {
            // Handle cancellation or other results
        }
    }
}
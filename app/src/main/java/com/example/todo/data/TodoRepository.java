package com.example.todo.data;

import android.app.Application;
import androidx.lifecycle.LiveData;
import java.util.List;

public class TodoRepository {

    private TodoDao mTodoDao;
    private LiveData<List<Todo>> mAllTodos;

    public TodoRepository(Application application) {
        TodoDatabase db = TodoDatabase.getDatabase(application);
        mTodoDao = db.todoDao();
        mAllTodos = mTodoDao.getAllTodos();
    }

    public LiveData<List<Todo>> getAllTodos() {
        return mAllTodos;
    }

    public void insert(Todo todo) {
        TodoDatabase.databaseWriteExecutor.execute(() -> {
            mTodoDao.insert(todo);
        });
    }

    public void update(Todo todo) {
        TodoDatabase.databaseWriteExecutor.execute(() -> {
            mTodoDao.update(todo);
        });
    }

    public void delete(Todo todo) {
        TodoDatabase.databaseWriteExecutor.execute(() -> {
            mTodoDao.delete(todo);
        });
    }
}
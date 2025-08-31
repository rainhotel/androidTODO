package com.example.todo.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.todo.data.Todo;
import com.example.todo.data.TodoRepository;

import java.util.List;

public class TodoViewModel extends AndroidViewModel {

    private TodoRepository mRepository;
    private LiveData<List<Todo>> mAllTodos;

    public TodoViewModel(@NonNull Application application) {
        super(application);
        mRepository = new TodoRepository(application);
        mAllTodos = mRepository.getAllTodos();
    }

    public LiveData<List<Todo>> getAllTodos() {
        return mAllTodos;
    }

    public void insert(Todo todo) {
        mRepository.insert(todo);
    }

    public void update(Todo todo) {
        mRepository.update(todo);
    }

    public void delete(Todo todo) {
        mRepository.delete(todo);
    }
}
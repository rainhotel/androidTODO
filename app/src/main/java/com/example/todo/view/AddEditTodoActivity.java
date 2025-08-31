package com.example.todo.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.todo.R;

public class AddEditTodoActivity extends AppCompatActivity {

    public static final String EXTRA_ID = "com.example.todo.EXTRA_ID";
    public static final String EXTRA_TITLE = "com.example.todo.EXTRA_TITLE";

    private EditText editTextTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_todo);

        editTextTitle = findViewById(R.id.edittext_todo_title);
        Button buttonSave = findViewById(R.id.button_save);

        // 检查ActionBar是否存在，避免空指针异常
        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeAsUpIndicator(android.R.drawable.ic_delete);
        }

        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_ID)) {
            setTitle("Edit Todo");
            editTextTitle.setText(intent.getStringExtra(EXTRA_TITLE));
        } else {
            setTitle("Add Todo");
        }

        buttonSave.setOnClickListener(v -> saveTodo());
    }

    private void saveTodo() {
        String title = editTextTitle.getText().toString();
        if (title.trim().isEmpty()) {
            // Show an error message
            return;
        }

        Intent data = new Intent();
        data.putExtra(EXTRA_TITLE, title);

        int id = getIntent().getIntExtra(EXTRA_ID, -1);
        if (id != -1) {
            data.putExtra(EXTRA_ID, id);
        }

        setResult(RESULT_OK, data);
        finish();
    }
}
package com.example.myapplication;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

public class FragmentCheckbox extends Fragment {

    private EditText etStudentName, etStudentID;
    private CheckBox cbHasEnrolled;
    private Button btnAdd, btnEdit, btnDelete, btnView;
    private DatabaseHelper dbHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_checkbox, container, false);

        // Bind UI Elements
        etStudentName = view.findViewById(R.id.etStudentName);
        etStudentID = view.findViewById(R.id.etStudentID);
        cbHasEnrolled = view.findViewById(R.id.cbHasEnrolled);
        btnAdd = view.findViewById(R.id.btnAdd);
        btnEdit = view.findViewById(R.id.btnEdit);
        btnDelete = view.findViewById(R.id.btnDelete);
        btnView = view.findViewById(R.id.btnView);

        dbHelper = new DatabaseHelper(requireContext());

        // ADD Listener
        btnAdd.setOnClickListener(v -> {
            String id = etStudentID.getText().toString().trim();
            String name = etStudentName.getText().toString().trim();
            boolean isEnrolled = cbHasEnrolled.isChecked();

            if (id.isEmpty() || name.isEmpty()) {
                Toast.makeText(getContext(), "Please enter both ID and Name", Toast.LENGTH_SHORT).show();
                return;
            }

            boolean success = dbHelper.addStudent(id, name, isEnrolled);
            if (success) {
                Toast.makeText(getContext(), "Student added!", Toast.LENGTH_SHORT).show();
                clearInputs();
            } else {
                Toast.makeText(getContext(), "Error: ID already exists", Toast.LENGTH_SHORT).show();
            }
        });

        // EDIT Listener
        btnEdit.setOnClickListener(v -> {
            String id = etStudentID.getText().toString().trim();
            String name = etStudentName.getText().toString().trim();
            boolean isEnrolled = cbHasEnrolled.isChecked();

            if (id.isEmpty()) {
                Toast.makeText(getContext(), "Enter Student ID to edit", Toast.LENGTH_SHORT).show();
                return;
            }

            boolean updated = dbHelper.updateStudent(id, name, isEnrolled);
            if (updated) {
                Toast.makeText(getContext(), "Student updated!", Toast.LENGTH_SHORT).show();
                clearInputs();
            } else {
                Toast.makeText(getContext(), "Student ID not found", Toast.LENGTH_SHORT).show();
            }
        });

        // DELETE Listener
        btnDelete.setOnClickListener(v -> {
            String id = etStudentID.getText().toString().trim();

            if (id.isEmpty()) {
                Toast.makeText(getContext(), "Enter Student ID to delete", Toast.LENGTH_SHORT).show();
                return;
            }

            boolean deleted = dbHelper.deleteStudent(id);
            if (deleted) {
                Toast.makeText(getContext(), "Student deleted!", Toast.LENGTH_SHORT).show();
                clearInputs();
            } else {
                Toast.makeText(getContext(), "Student ID not found", Toast.LENGTH_SHORT).show();
            }
        });

        // VIEW Listener
        btnView.setOnClickListener(v -> {
            Cursor cursor = dbHelper.getAllStudents();
            if (cursor.getCount() == 0) {
                Toast.makeText(getContext(), "No data found", Toast.LENGTH_SHORT).show();
                return;
            }

            StringBuilder buffer = new StringBuilder();
            while (cursor.moveToNext()) {
                buffer.append("ID: ").append(cursor.getString(0)).append("\n");
                buffer.append("Name: ").append(cursor.getString(1)).append("\n");
                buffer.append("Enrolled: ").append(cursor.getInt(2) == 1 ? "Yes" : "No").append("\n\n");
            }
            cursor.close();

            // Display records in a simple dialog box
            new AlertDialog.Builder(requireContext())
                    .setTitle("Student Records")
                    .setMessage(buffer.toString())
                    .setPositiveButton("OK", null)
                    .show();
        });

        return view;
    }

    private void clearInputs() {
        etStudentName.setText("");
        etStudentID.setText("");
        cbHasEnrolled.setChecked(false);
    }
}
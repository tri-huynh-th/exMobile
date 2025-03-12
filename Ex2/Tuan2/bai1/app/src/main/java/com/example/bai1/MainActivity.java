package com.example.bai1;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private EditText editName;
    private EditText editAge;
    private TextView resultTextView;
    private Button checkButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Khởi tạo các thành phần giao diện
        editName = findViewById(R.id.editName);
        editAge = findViewById(R.id.editAge);
        resultTextView = findViewById(R.id.resultTextView);
        checkButton = findViewById(R.id.check);

        // Xử lý sự kiện khi nhấn Button
        checkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAge();
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void checkAge() {
        // Lấy giá trị từ EditText
        String name = editName.getText().toString().trim();
        String ageStr = editAge.getText().toString().trim();

        // Kiểm tra xem các trường có trống không
        if (name.isEmpty() || ageStr.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập họ và tên và tuổi!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Chuyển đổi tuổi thành số nguyên
        int age;
        try {
            age = Integer.parseInt(ageStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Tuổi phải là số hợp lệ!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Kiểm tra tuổi và hiển thị kết quả
        if (age > 65) {
            resultTextView.setText(name + " là người già.");
        } else if (6 <= age && age <= 65) {
            resultTextView.setText(name + " là người lớn.");
        }  else if (2 <= age && age < 6) {
            resultTextView.setText(name + " là trẻ em.");
        } else if (2 > age ) {
            resultTextView.setText(name + " là người lớn.");
        }else {
            Toast.makeText(this, "Tuổi phải lớn hơn 0!", Toast.LENGTH_SHORT).show();
        }
    }
}
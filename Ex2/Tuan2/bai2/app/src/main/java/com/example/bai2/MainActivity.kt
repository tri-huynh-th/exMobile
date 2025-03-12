package com.example.bai2

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

open class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Gọi hàm để gắn sự kiện cho footer
        setupFooterButtons()
    }

    // Hàm để gắn sự kiện click cho các nút footer
    protected fun setupFooterButtons() {
        val homeButton = findViewById<ImageButton>(R.id.btnHome)
        homeButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish() // Đóng Activity hiện tại
        }

        val bookButton = findViewById<ImageButton>(R.id.btn_book)
        bookButton.setOnClickListener {
            val intent = Intent(this, BookActivity::class.java)
            startActivity(intent)
            finish()
        }

        val employeeButton = findViewById<ImageButton>(R.id.btn_employee)
        employeeButton.setOnClickListener {
            showPasswordDialog()
        }
    }
    // Hàm hiển thị hộp thoại yêu cầu mật khẩu
    private fun showPasswordDialog() {
        // Tạo AlertDialog Builder
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Nhập mật khẩu")

        // Tạo EditText để nhập mật khẩu
        val input = EditText(this)
        input.hint = "Mật khẩu"
        input.inputType = android.text.InputType.TYPE_CLASS_TEXT or android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD

        builder.setView(input)

        // Thiết lập các nút của hộp thoại
        builder.setPositiveButton("OK") { dialog, which ->
            val enteredPassword = input.text.toString()
            val correctPassword = "12345" // Mật khẩu chính xác bạn muốn kiểm tra

            if (enteredPassword == correctPassword) {
                // Nếu mật khẩu đúng, chuyển sang EmployeeActivity
                val intent = Intent(this, EmployeeActivity::class.java)
                startActivity(intent)
            } else {
                // Nếu mật khẩu sai, hiện thông báo
                showErrorDialog()
            }
        }
        builder.setNegativeButton("Hủy") { dialog, which ->
            dialog.cancel()
        }

        // Hiển thị hộp thoại
        builder.show()
    }

    // Hàm hiển thị hộp thoại thông báo sai mật khẩu
    private fun showErrorDialog() {
        AlertDialog.Builder(this)
            .setTitle("Sai mật khẩu")
            .setMessage("Mật khẩu bạn nhập không đúng.")
            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
            .show()
    }
}

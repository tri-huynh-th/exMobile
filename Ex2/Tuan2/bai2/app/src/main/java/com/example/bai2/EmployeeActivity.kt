package com.example.bai2

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import org.json.JSONArray
import java.io.File

class EmployeeActivity : MainActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_employee_list)

        setupFooterButtons()

        val listViewEmployees = findViewById<ListView>(R.id.list_view_employee)

        // Đọc dữ liệu JSON từ file lưu thông tin người mượn
        val borrowersList = loadBorrowerInfoFromJSON()

        // Tạo ArrayAdapter để hiển thị danh sách người mượn

        val adapter = ArrayAdapter(this, R.layout.list_item_book, R.id.tv_book_item, borrowersList)
        listViewEmployees.adapter = adapter
    }
    // Tạo một ArrayAdapter để liên kết dữ liệu với ListView


    // Hàm đọc thông tin người mượn từ file JSON
    private fun loadBorrowerInfoFromJSON(): List<String> {
        val borrowersList = mutableListOf<String>()
        try {
            val fileName = "infor.json"
            val file = File(filesDir, fileName)
            if (file.exists()) {
                val fileContent = file.readText()
                val borrowersArray = JSONArray(fileContent)
                for (i in 0 until borrowersArray.length()) {
                    val borrower = borrowersArray.getJSONObject(i)
                    val name = borrower.getString("name")
                    val id = borrower.getString("id")
                    val bookTitle = borrower.getString("bookTitle")
                    borrowersList.add("$name (ID: $id) đã mượn sách: $bookTitle")
                }
            } else {
                borrowersList.add("Chưa có thông tin người mượn")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Lỗi khi đọc thông tin người mượn", Toast.LENGTH_SHORT).show()
        }
        return borrowersList
    }
}

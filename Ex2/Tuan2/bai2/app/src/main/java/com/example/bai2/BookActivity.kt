package com.example.bai2

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.io.InputStream

class BookActivity : MainActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_list)

        setupFooterButtons()
        val fileName = "infor.json"
        val file = File(filesDir, fileName)
        val filePath = file.absolutePath

        // Hiển thị đường dẫn file trong TextView
//        val tvFilePath = findViewById<TextView>(R.id.tv_file_path)
//        tvFilePath.text = "Đường dẫn file: $filePath "


        val listViewBooks = findViewById<ListView>(R.id.list_view_books)

        // Đọc dữ liệu JSON từ file assets
        val jsonString = loadJSONFromAsset("listBook.json")
        val bookList = mutableListOf<String>()
        val availableQuantities = mutableListOf<Int>()

        if (jsonString != null) {
            try {
                val jsonArray = JSONArray(jsonString)
                for (i in 0 until jsonArray.length()) {
                    val book = jsonArray.getJSONObject(i)
                    val title = book.getString("title")
                    val author = book.getString("author")
                    val availableQuantity = book.getInt("availableQuantity")
                    bookList.add("$title - $author (Còn lại: $availableQuantity quyển)")
                    availableQuantities.add(availableQuantity)
                }
            } catch (e: Exception) {
                println("Error parsing JSON: ${e.message}")
            }
        }

        // Tạo một ArrayAdapter để liên kết dữ liệu với ListView
        val adapter = ArrayAdapter(this, R.layout.list_item_book, R.id.tv_book_item, bookList)
        listViewBooks.adapter = adapter

        // Xử lý sự kiện click vào từng cuốn sách
        listViewBooks.setOnItemClickListener { parent, view, position, id ->
            val availableQuantity = availableQuantities[position]
            if (availableQuantity > 0) {
                // Nếu còn sách để mượn, hiển thị dialog yêu cầu nhập tên và ID
                showBorrowDialog(bookList[position], availableQuantity, position, availableQuantities)
            } else {
                Toast.makeText(this, "Sách này đã hết, không thể mượn", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Hàm hiển thị dialog để yêu cầu người dùng nhập tên và ID
    private fun showBorrowDialog(bookTitle: String, availableQuantity: Int, position: Int, availableQuantities: MutableList<Int>) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Mượn sách: $bookTitle")

        // Layout cho dialog để nhập tên và ID
        val layout = layoutInflater.inflate(R.layout.dialog_borrow_book, null)
        builder.setView(layout)

        val inputName = layout.findViewById<EditText>(R.id.input_name)
        val inputId = layout.findViewById<EditText>(R.id.input_id)

        // Xử lý khi người dùng nhấn nút "Mượn"
        builder.setPositiveButton("Mượn") { dialog, which ->
            val name = inputName.text.toString()
            val id = inputId.text.toString()
            if (name.isNotEmpty() && id.isNotEmpty()) {
                Toast.makeText(this, "Bạn đã mượn sách: $bookTitle", Toast.LENGTH_SHORT).show()

                // Giảm số lượng sách còn lại
                availableQuantities[position] -= 1
                updateBookList(availableQuantities)

                // Lưu thông tin người mượn vào file JSON
                saveBorrowerInfoToJSON(name, id, bookTitle)

            } else {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show()
            }
        }

        // Xử lý khi người dùng nhấn nút "Hủy"
        builder.setNegativeButton("Hủy", null)

        // Hiển thị dialog
        builder.show()
    }

    private fun saveBorrowerInfoToJSON(name: String, id: String, bookTitle: String) {
        // Lấy đường dẫn file
        val fileName = "infor.json"
        val file = File(filesDir, fileName)
        val filePath = file.absolutePath

        if (file.exists()) {
            println("File does not exist. Creating a new file...")
        }

        // Kiểm tra xem file có trống không
        val jsonContent = if (file.exists() && file.length() > 0) {
            file.readText()
        } else {
            "[]" // Tạo một mảng JSON rỗng nếu file trống
        }

        // Đọc nội dung JSON
        val borrowersArray = try {
            JSONArray(jsonContent)
        } catch (e: Exception) {
            JSONArray() // Tạo mảng JSON mới nếu file không hợp lệ
        }

        // Tạo đối tượng JSON cho người mượn
        val borrowerInfo = JSONObject().apply {
            put("name", name)
            put("id", id)
            put("bookTitle", bookTitle)
        }

        // Thêm thông tin người mượn vào danh sách
        borrowersArray.put(borrowerInfo)

        // Ghi lại danh sách vào file
        try {
            file.writeText(borrowersArray.toString())
            println("File content after writing: ${file.readText()}") // Kiểm tra nội dung file
            Toast.makeText(this, "Lưu thông tin thành công", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Lỗi khi lưu thông tin", Toast.LENGTH_SHORT).show()
        }
    }




    // Hàm cập nhật lại danh sách sách sau khi mượn
    private fun updateBookList(availableQuantities: MutableList<Int>) {
        val listViewBooks = findViewById<ListView>(R.id.list_view_books)

        // Lấy adapter hiện có từ ListView
        val adapter = listViewBooks.adapter as ArrayAdapter<String>

        // Tạo danh sách mới với số lượng sách đã cập nhật
        val bookList = mutableListOf<String>()
        val jsonString = loadJSONFromAsset("listBook.json")

        if (jsonString != null) {
            try {
                val jsonArray = JSONArray(jsonString)
                for (i in 0 until jsonArray.length()) {
                    val book = jsonArray.getJSONObject(i)
                    val title = book.getString("title")
                    val author = book.getString("author")
                    val availableQuantity = availableQuantities[i]
                    bookList.add("$title - $author (Còn lại: $availableQuantity quyển)")
                }
            } catch (e: Exception) {
                println("Error parsing JSON: ${e.message}")
            }
        }

        // Xóa dữ liệu cũ và thêm dữ liệu mới vào adapter hiện có
        adapter.clear()
        adapter.addAll(bookList)

        // Thông báo cho adapter rằng dữ liệu đã thay đổi
        adapter.notifyDataSetChanged()
    }


    // Hàm đọc file JSON từ thư mục assets (không thay đổi)
    private fun loadJSONFromAsset(fileName: String): String? {
        return try {
            val inputStream: InputStream = assets.open(fileName)
            val size: Int = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            String(buffer, Charsets.UTF_8)
        } catch (ex: Exception) {
            ex.printStackTrace()
            null
        }
    }
}

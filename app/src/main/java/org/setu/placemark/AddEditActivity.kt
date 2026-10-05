package org.setu.placemark

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import org.setu.placemark.models.PlacemarkModel

class AddEditActivity : AppCompatActivity() {
    private lateinit var titleInput: EditText
    private lateinit var descriptionInput: EditText
    private lateinit var xInput: EditText
    private lateinit var yInput: EditText
    private var editingId: Long? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit)

        titleInput = findViewById(R.id.titleInput)
        descriptionInput = findViewById(R.id.descriptionInput)
        xInput = findViewById(R.id.xInput)
        yInput = findViewById(R.id.yInput)

        editingId = intent.getLongExtra("id", -1L).takeIf { it != -1L }
        if (editingId != null) {
            findViewById<TextView>(R.id.formTitle).text = "Edit Mark"
            loadExistingMark(editingId!!)
        }

        findViewById<Button>(R.id.saveButton).setOnClickListener { saveMark() }
        findViewById<Button>(R.id.cancelButton).setOnClickListener { finish() }
    }

    private fun loadExistingMark(id: Long) {
        val mark = AppData.placedMarks.findOne(id)
        if (mark == null) {
            Toast.makeText(this, "Mark not found", Toast.LENGTH_SHORT).show()
            finish()
            return
        }
        titleInput.setText(mark.title)
        descriptionInput.setText(mark.description)
        xInput.setText(mark.x.toString())
        yInput.setText(mark.y.toString())
    }

    private fun saveMark() {
        val title = titleInput.text.toString().trim()
        val description = descriptionInput.text.toString().trim()
        if (title.isEmpty()) {
            titleInput.error = "Title is required"
            return
        }

        val x = xInput.text.toString().toDoubleOrNull()
        if (x == null) {
            xInput.error = "Enter a valid number"
            return
        }

        val y = yInput.text.toString().toDoubleOrNull()
        if (y == null) {
            yInput.error = "Enter a valid number"
            return
        }

        val mark = PlacemarkModel(
            id = editingId ?: 0L,
            title = title,
            description = description,
            x = x,
            y = y
        )

        if (editingId == null) {
            AppData.placedMarks.create(mark)
            Toast.makeText(this, "Mark created", Toast.LENGTH_SHORT).show()
        } else {
            AppData.placedMarks.update(mark)
            Toast.makeText(this, "Mark updated", Toast.LENGTH_SHORT).show()
        }
        finish()
    }
}
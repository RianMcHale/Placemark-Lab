package org.setu.placemark

import android.app.Activity
import android.os.Bundle
import android.text.InputType
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.Toast
import org.setu.placemark.models.PlacemarkModel

class AddEditActivity : Activity() {
    private lateinit var titleInput: EditText
    private lateinit var descriptionInput: EditText
    private lateinit var xInput: EditText
    private lateinit var yInput: EditText
    private var editingId: Long? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createUserInterface()

        if (intent.hasExtra(EXTRA_ID)) {
            editingId = intent.getLongExtra(EXTRA_ID, -1L)
            editingId?.let { loadExistingMark(it) }
        }
    }

    private fun createUserInterface() {
        val form = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(32, 32, 32, 32)
        }

        titleInput = EditText(this).apply { hint = "Title" }
        descriptionInput = EditText(this).apply { hint = "Description" }
        xInput = coordinateInput("X coordinate")
        yInput = coordinateInput("Y coordinate")

        form.addView(titleInput)
        form.addView(descriptionInput)
        form.addView(xInput)
        form.addView(yInput)
        form.addView(Button(this).apply {
            text = "Save"
            setOnClickListener { saveMark() }
        })
        form.addView(Button(this).apply {
            text = "Cancel"
            setOnClickListener { finish() }
        })

        setContentView(ScrollView(this).apply { addView(form) })
    }

    private fun coordinateInput(label: String) = EditText(this).apply {
        hint = label
        inputType = InputType.TYPE_CLASS_NUMBER or
                InputType.TYPE_NUMBER_FLAG_DECIMAL or
                InputType.TYPE_NUMBER_FLAG_SIGNED
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

    companion object {
        const val EXTRA_ID = "org.setu.placemark.ID"
    }
}
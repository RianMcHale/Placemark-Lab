package org.setu.placemark

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView

class MainActivity : Activity() {
    private lateinit var listLayout: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(32, 32, 32, 32)
        }

        val heading = TextView(this).apply {
            text = "Placed Marks"
            textSize = 28f
            gravity = Gravity.CENTER
        }
        root.addView(heading)

        val addButton = Button(this).apply {
            text = "Add Mark"
            setOnClickListener {
                startActivity(Intent(this@MainActivity, AddEditActivity::class.java))
            }
        }
        root.addView(addButton)

        listLayout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
        }

        val scrollView = ScrollView(this).apply {
            addView(listLayout)
        }
        root.addView(
            scrollView,
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                0,
                1f
            )
        )

        setContentView(root)
        displayMarks()
    }

    override fun onResume() {
        super.onResume()
        if (::listLayout.isInitialized) displayMarks()
    }

    private fun displayMarks() {
        listLayout.removeAllViews()
        val marks = AppData.placedMarks.findAll()

        if (marks.isEmpty()) {
            listLayout.addView(TextView(this).apply {
                text = "No placed marks yet."
                textSize = 18f
                setPadding(0, 40, 0, 40)
            })
            return
        }

        for (mark in marks) {
            val item = LinearLayout(this).apply {
                orientation = LinearLayout.VERTICAL
                setPadding(0, 20, 0, 20)
            }

            item.addView(TextView(this).apply {
                text = "${mark.id}: ${mark.title}"
                textSize = 20f
            })
            item.addView(TextView(this).apply {
                text = mark.description
                textSize = 16f
            })
            item.addView(TextView(this).apply {
                text = "X: ${mark.x}, Y: ${mark.y}"
                textSize = 14f
            })

            item.addView(Button(this).apply {
                text = "Edit"
                setOnClickListener {
                    val intent = Intent(this@MainActivity, AddEditActivity::class.java)
                    intent.putExtra(AddEditActivity.EXTRA_ID, mark.id)
                    startActivity(intent)
                }
            })

            item.addView(Button(this).apply {
                text = "Delete"
                setOnClickListener {
                    AppData.placedMarks.delete(mark.id)
                    displayMarks()
                }
            })

            listLayout.addView(item)
        }
    }
}
package iu9.bmstu.ru.lab1

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.NumberPicker
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    private var textView: TextView? = null
    private var editText: EditText? = null
    private var imageView: ImageView? = null
    private var numberPicker: NumberPicker? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textView = findViewById(R.id.textView) as TextView
        editText = findViewById(R.id.editText) as EditText
        imageView = findViewById(R.id.imageView) as ImageView
        numberPicker = findViewById(R.id.numberPicker) as NumberPicker
        numberPicker?.maxValue = 9
        numberPicker?.minValue = 0

        textView?.text = "new text"

        textView?.setOnClickListener { Toast.makeText(baseContext, "Toast!", Toast.LENGTH_LONG).show() }
    }
}

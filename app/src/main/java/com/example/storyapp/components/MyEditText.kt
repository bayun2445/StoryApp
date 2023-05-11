package com.example.storyapp.components

import android.content.Context
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText

class MyEditText: AppCompatEditText {
    constructor(context: Context): super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet): super(context, attrs) {
        init()
    }

    constructor(
        context: Context,
        attrs: AttributeSet,
        defStyleAttr: Int
    ): super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                validateInputText(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })
    }

    private fun validateInputText(text: String) {
        if (inputType and InputType.TYPE_TEXT_VARIATION_PASSWORD > 0) {
            if (text.length < 8) {
                error = "Password requires 8 or more characters"
            }
        } else if (inputType and InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS > 0) {
            val pattern = "[a-zA-Z0-9._-]+@[a-z]+.+[a-z]+"

            if (!text.matches(pattern.toRegex())) {
                error = "Incorrect email format. (user@example.com)"
            }
        }
    }
}
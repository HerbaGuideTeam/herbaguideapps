package com.capstone.herbaguideapps.widget

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import com.capstone.herbaguideapps.R
import com.google.android.material.textfield.TextInputEditText
import java.util.regex.Pattern

class EmailEditText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : TextInputEditText(context, attrs) {
    init {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                validateEmail(s.toString())
            }

        })
    }

    private fun validateEmail(email: String) {
        val pattern = Pattern.compile("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")
        val matcher = pattern.matcher(email)
        if (email.isEmpty()) {
            this.error = null
        } else if (!matcher.matches()) {
            this.error = context.getString(R.string.invalid_email_address)
        } else {
            this.error = null
        }
    }
}
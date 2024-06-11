package com.capstone.herbaguideapps.widget

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import com.capstone.herbaguideapps.R
import com.google.android.material.textfield.TextInputEditText

class PasswordEditText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : TextInputEditText(context, attrs) {

    init {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                validatePassword(s.toString())
            }

        })
    }

    private fun validatePassword(password: String) {
        if (password.isEmpty()) {
            this.error = null
        } else if (password.length < 8) {
            this.error = context.getString(R.string.password_must_be_at_least_8_characters)
        } else {
            this.error = null
        }
    }

}
package com.example.engieersthesis.utility;

import android.text.method.PasswordTransformationMethod;
import android.view.View;

public class NumericKeyboardTransformationMethod extends PasswordTransformationMethod {
    @Override
    public CharSequence getTransformation(CharSequence source, View view) {
        return source;
    }
}

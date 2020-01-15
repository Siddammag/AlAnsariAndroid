package app.alansari.Utils;

import com.google.android.material.textfield.TextInputLayout;
import android.text.TextUtils;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;

import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Parveen Dala on 12 October, 2016
 * Fugenx Technologies, Bengaluru
 * AlAnsari
 */

public class Validation {
    private static String SPECIAL_CHARACTER_PATTERN;
    private static String USER_PATTERN;
    private static String EMAIL_PATTERN;
    private static String MOBILE_PATTERN;

    public static boolean isValidEditTextValue(EditText editText) {
        return editText.getText() != null && editText.getText().toString().trim().length() > 0;
    }
    public static boolean isValidEditTextValueZero(EditText editText) {
        return editText.getText() != null && editText.getText().toString().trim().length() > 0 && Integer.parseInt(editText.getText().toString().trim().replaceAll(",", "")) > 0;
    }

    public static boolean isValidAutoCompleteTextValue(AutoCompleteTextView editText) {
        return editText.getText() != null && editText.getText().toString().trim().length() > 0;
    }

    public static boolean isValidTextViewValue(TextView textView) {
        return textView.getText() != null && textView.getText().toString().trim().length() > 0;
    }

    public static boolean isValidString(String string) {
        return string != null && string.trim().length() > 0;
    }

    public static boolean isValidRate(String rate) {
        return (rate != null && !rate.equalsIgnoreCase("null") && rate.length() > 0);
    }

    public static boolean isValidList(List list) {
        return list != null && list.size() > 0;
    }

    public static boolean isValidJsonArray(JSONArray jsonArray) {
        return (jsonArray != null && jsonArray.length() > 0);
    }

    public static boolean validateSpecialCharacters(EditText editText, TextInputLayout inputLayout) {
        if (!Validation.isValidEditTextValue(editText)) {
            inputLayout.setError(null);
            inputLayout.setErrorEnabled(false);
            return true;
        }
        SPECIAL_CHARACTER_PATTERN = "^[\u0621-\u064A\u0660-\u0669a-zA-Z0-9\\s]*$";
        Pattern pattern = Pattern.compile(SPECIAL_CHARACTER_PATTERN);
        Matcher matcher = pattern.matcher(editText.getText().toString());
        if (matcher.matches()) {
            inputLayout.setError(null);
            inputLayout.setErrorEnabled(false);
            return true;
        } else {
            inputLayout.setError("Special characters are not allowed");
            inputLayout.setErrorEnabled(true);
            return false;
        }
    }

    public static boolean isValidUser(String user) {
        USER_PATTERN = "^[a-zA-Z ]{3,20}$";
        Pattern pattern = Pattern.compile(USER_PATTERN);
        Matcher matcher = pattern.matcher(user);
        return matcher.matches();
    }

    public static boolean isValidEmail(String email) {
        EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static boolean isValidMobile(String phone) {
        MOBILE_PATTERN = "[0-9]{12}";
        Pattern pattern = Pattern.compile(MOBILE_PATTERN);
        Matcher matcher = pattern.matcher(phone);
        return matcher.matches();
    }

    public static boolean isValidPassword(String pass) {
        return pass != null && pass.length() >= 6;
    }

    public static String getMaskedString(String string) {
        try {
            if (string != null && string.trim().length() > 3) {
                int length = string.length() <= 10 ? 2 : 4;
                String textToReplace = TextUtils.join("", Collections.nCopies(string.length() - (length * 2), "*"));
                return new StringBuilder(string).replace(length, string.length() - length, textToReplace).toString();
            } else
                return string;
        } catch (Exception ex) {
            ex.printStackTrace();
            return "****";
        }
    }
}

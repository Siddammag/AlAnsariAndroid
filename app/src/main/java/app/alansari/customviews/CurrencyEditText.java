package app.alansari.customviews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.EditText;

import androidx.appcompat.widget.AppCompatEditText;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

import app.alansari.Utils.CommonUtils;
import app.alansari.Utils.Validation;

public class CurrencyEditText extends AppCompatEditText {

    public CurrencyEditText(Context context) {
        super(context);
        initEditText();
    }

    public CurrencyEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        initEditText();
    }

    public CurrencyEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initEditText();
    }

    private void initEditText() {
        this.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                CurrencyEditText et = CurrencyEditText.this;
                et.removeTextChangedListener(this);
                String originalString= s.toString();
                if (s.length() > 5) {
                    et.setTextSize(TypedValue.COMPLEX_UNIT_PX, getContext().getResources().getDimension(app.alansari.R.dimen.dimens_22sp));
                } else {
                    et.setTextSize(TypedValue.COMPLEX_UNIT_PX, getContext().getResources().getDimension(app.alansari.R.dimen.dimens_28sp));
                }

                if (Validation.isValidString(s.toString())) {
                    //et.setText(CommonUtils.addCommaAfterThousand(s.toString()));
                    //et.setText(CommonUtils.addCommaAfterThousand());

                    try {
                        int currentSelection = et.getSelectionStart();
                        int length = et.getText().length();
                        if (originalString.contains(",")) {
                            originalString = originalString.replaceAll(",", "");
                        }

                        String afterDecimal = "";
                        if (originalString.contains(".")) {
                            afterDecimal = originalString.substring(originalString.indexOf("."));
                            originalString = originalString.substring(0, originalString.indexOf("."));
                        }

                        String formattedString = addCommaToStringForet(originalString) + afterDecimal;

                        et.setText(formattedString);
                        if (et.getText().length() > length)
                            currentSelection++;
                        else if (et.getText().length() < length && currentSelection != 0)
                            currentSelection--;

                        et.setSelection(currentSelection);
                    } catch (NumberFormatException nfe) {
                        nfe.printStackTrace();
                    }
                }
                et.addTextChangedListener(this);
            }
        });
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
    public static String addCommaToStringForet(String value) {
        try {
            if (!Validation.isValidString(value))
                return value;

            DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
            formatter.applyPattern("#,###,###,###");
            return formatter.format(Double.parseDouble(value));
        } catch (Exception ex) {
            ex.printStackTrace();
            return String.valueOf(value);
        }
    }
}

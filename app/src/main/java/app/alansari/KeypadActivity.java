package app.alansari;

import app.alansari.Utils.CommonUtils;
import app.alansari.Utils.Constants;
import app.alansari.Utils.LogOutTimerUtil;
import app.alansari.Utils.LogUtils;
import app.alansari.keypadview.BigButtonView;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Stack;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static app.alansari.Utils.LogOutTimerUtil.stopLogoutTimer;

/**
 * Created by Parveen Dala on 7 October, 2016
 * Fugenx Technologies, Bengaluru
 * AlAnsari
 */
public class KeypadActivity extends AppCompatActivity implements BigButtonView.OnPressListener, LogOutTimerUtil.LogOutListener  {


    private Context context;
    private LinearLayout keyPadPinLayout;
    private int screenLayout;
    private TextView pin1, pin2, pin3, pin4, pin5, pin6, mCurrentlyFocusedEditText;

    /**
     * Key Pad
     */
    private boolean goBack = false;
    private int passwordLength = 4;
    private int incorrectInputTimes = 0;
    private String correctPassword = null;

    private BigButtonView[] bigButtonViews;
    private TextView leftButton, rightButton;
    private Stack<String> passwordStack = new Stack<>();


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.keypad_activity);
        context = this;
        screenLayout = getIntent().getExtras().getInt(Constants.SCREEN_LAYOUT, Constants.SCREEN_LAYOUT_PIN);
        init();
        setFocusListener();
        setOnTextChangeListener();
    }


    private void init() {
        pin1 = (TextView) findViewById(R.id.pin1);
        pin2 = (TextView) findViewById(R.id.pin2);
        pin3 = (TextView) findViewById(R.id.pin3);
        pin4 = (TextView) findViewById(R.id.pin4);
        pin5 = (TextView) findViewById(R.id.pin5);
        pin6 = (TextView) findViewById(R.id.pin6);
        pin1.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
        pin2.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
        pin3.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
        pin4.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
        pin5.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
        pin6.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
        mCurrentlyFocusedEditText = pin1;
        keyPadPinLayout = (LinearLayout) findViewById(R.id.keypad_pin_layout);
        if (screenLayout == Constants.SCREEN_LAYOUT_PIN) {
            keyPadPinLayout.setWeightSum(4f);
            findViewById(R.id.pin5_layout).setVisibility(View.GONE);
            findViewById(R.id.pin6_layout).setVisibility(View.GONE);
            pin4.setNextFocusDownId(R.id.pin4);
            passwordLength = 4;
            setCorrectPassword("1234");
        } else {
            keyPadPinLayout.setWeightSum(6f);
            findViewById(R.id.pin5_layout).setVisibility(View.VISIBLE);
            findViewById(R.id.pin6_layout).setVisibility(View.VISIBLE);
            pin4.setNextFocusDownId(R.id.pin5);
            passwordLength = 6;
            setCorrectPassword("123456");
        }


        // Number password
        bigButtonViews = new BigButtonView[10];
        bigButtonViews[0] = (BigButtonView) findViewById(R.id.button_0);
        bigButtonViews[1] = (BigButtonView) findViewById(R.id.button_1);
        bigButtonViews[2] = (BigButtonView) findViewById(R.id.button_2);
        bigButtonViews[3] = (BigButtonView) findViewById(R.id.button_3);
        bigButtonViews[4] = (BigButtonView) findViewById(R.id.button_4);
        bigButtonViews[5] = (BigButtonView) findViewById(R.id.button_5);
        bigButtonViews[6] = (BigButtonView) findViewById(R.id.button_6);
        bigButtonViews[7] = (BigButtonView) findViewById(R.id.button_7);
        bigButtonViews[8] = (BigButtonView) findViewById(R.id.button_8);
        bigButtonViews[9] = (BigButtonView) findViewById(R.id.button_9);
        String[] texts = getResources().getStringArray(R.array.default_big_button_text);
        for (int i = 0; i < bigButtonViews.length; i++) {
            bigButtonViews[i].setOnPressListener(this);
            bigButtonViews[i].setText(texts[i]);
        }

        leftButton = (TextView) findViewById(R.id.left_button);
        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        rightButton = (TextView) findViewById(R.id.right_button);
        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                simulateDeletePress();
            }
        });
    }

    private void setFocusListener() {
        View.OnFocusChangeListener onFocusChangeListener = new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                mCurrentlyFocusedEditText = (TextView) v;
            }
        };
        pin1.setOnFocusChangeListener(onFocusChangeListener);
        pin2.setOnFocusChangeListener(onFocusChangeListener);
        pin3.setOnFocusChangeListener(onFocusChangeListener);
        pin4.setOnFocusChangeListener(onFocusChangeListener);
        pin5.setOnFocusChangeListener(onFocusChangeListener);
        pin6.setOnFocusChangeListener(onFocusChangeListener);
    }

    private void setOnTextChangeListener() {
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (mCurrentlyFocusedEditText.getText().length() >= 1 && (screenLayout == Constants.SCREEN_LAYOUT_PIN ? mCurrentlyFocusedEditText != pin4 : mCurrentlyFocusedEditText != pin6)) {
                    LogUtils.d("ok", "Next Focus Length " + mCurrentlyFocusedEditText.getText().length() + "   View " + (mCurrentlyFocusedEditText == pin4));
                    mCurrentlyFocusedEditText.focusSearch(View.FOCUS_DOWN).requestFocus();
                } else if (mCurrentlyFocusedEditText.getText().length() >= 1 && (screenLayout == Constants.SCREEN_LAYOUT_PIN ? mCurrentlyFocusedEditText == pin4 : mCurrentlyFocusedEditText == pin6)) {
                    LogUtils.d("ok", "Check Pass " + mCurrentlyFocusedEditText.getText().length() + "   View " + (mCurrentlyFocusedEditText == pin4));
                    LogUtils.d("ok", "Check Password " + pin1.getText() + pin2.getText() + pin3.getText() + pin4.getText());
                } else {
                    String currentValue = mCurrentlyFocusedEditText.getText().toString();
                    if (currentValue.length() <= 0 && goBack) {
                        mCurrentlyFocusedEditText.focusSearch(View.FOCUS_UP).requestFocus();
                        goBack = false;
                        mCurrentlyFocusedEditText.setText("");
                    }
                }
            }
        };
        pin1.addTextChangedListener(textWatcher);
        pin2.addTextChangedListener(textWatcher);
        pin3.addTextChangedListener(textWatcher);
        pin4.addTextChangedListener(textWatcher);
        pin5.addTextChangedListener(textWatcher);
        pin6.addTextChangedListener(textWatcher);
    }

    public void simulateDeletePress() {
        if (passwordStack.size() > 0) {
            LogUtils.d("ok", "pop " + passwordStack.size());
            passwordStack.pop();
            LogUtils.d("ok", "After pop " + passwordStack.size());
        }
        if (mCurrentlyFocusedEditText != null && mCurrentlyFocusedEditText.getText().toString().equals("")) {
            goBack = true;
            mCurrentlyFocusedEditText.setText("");
        } else {
            goBack = false;
            mCurrentlyFocusedEditText.setText("");
        }
    }

    /**
     * From the button views.
     *
     * @param string The string from button views.
     */
    @Override
    public void onPress(String string) {
        if (correctPassword == null) {
            throw new RuntimeException("The correct password has NOT been set!");
        }
        LogUtils.d("ok", "Stack " + passwordStack.size());
        LogUtils.d("ok", "PassLength " + passwordLength);
        if (passwordStack.size() >= passwordLength) return;
        passwordStack.push(string);
        mCurrentlyFocusedEditText.setText(string);
        StringBuilder nowPassword = new StringBuilder("");
        for (String s : passwordStack) {
            nowPassword.append(s);
        }
        String nowPasswordString = nowPassword.toString();
        if (correctPassword.equals(nowPasswordString)) {
            LogUtils.d("ok", "Correct Pass");
            Toast.makeText(context, "Correct Password !! ", Toast.LENGTH_SHORT).show();
        } else {
            if (correctPassword.length() > nowPasswordString.length()) {
                LogUtils.d("ok", "Enter.. ");
            } else {
                LogUtils.d("ok", "In Correct Pass");
                incorrectInputTimes++;
                showError();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < passwordLength; i++) {
                            simulateDeletePress();
                            passwordStack.clear();
                        }
                    }
                }, 300);
            }
        }
    }

    private void showError() {
        Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake_error);
        findViewById(R.id.keypad_pin_layout).startAnimation(shake);
    }

    /**
     * Prevent click 2 or above buttons at the same time.
     *
     * @param event
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getPointerCount() > 1) {
            for (int i = 0; i < bigButtonViews.length; i++) bigButtonViews[i].clearAnimation();
            return true;
        }
        return super.dispatchTouchEvent(event);
    }


    /**
     * Set the length of the password.
     * Default length is 4.
     *
     * @param passwordLength
     */
    public void setPasswordLength(int passwordLength) {
        this.passwordLength = passwordLength;
        passwordStack.clear();
        correctPassword = null;
    }


    /**
     * Set the target password.
     *
     * @param correctPassword The target password.
     */
    public void setCorrectPassword(String correctPassword) {
        setPasswordLength(correctPassword.length());
        this.correctPassword = correctPassword;
    }

    /**
     * You can use this to reset the incorrect input times.
     *
     * @param incorrectInputTimes The incorrect input times.
     */
    public void setIncorrectInputTimes(int incorrectInputTimes) {
        this.incorrectInputTimes = incorrectInputTimes;
    }

    /**
     * Return the incorrect input times.
     *
     * @return Incorrect input times.
     */
    public int getIncorrectInputTimes() {
        return incorrectInputTimes;
    }

    @Override
    protected void onStart() {
        super.onStart();
        LogOutTimerUtil.startLogoutTimer(this, this);
    }

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        LogOutTimerUtil.startLogoutTimer(this, this);

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if(CommonUtils.getLogoutStatus()) {
            CommonUtils.registerAgainOpen(getApplicationContext());
        }
    }

    @Override
    public void doLogout() {
        boolean mlogout=CommonUtils.isAppOnForeground(context);
        if(mlogout) {
            CommonUtils.registerAgainOpen(getApplicationContext());
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopLogoutTimer();
    }

}

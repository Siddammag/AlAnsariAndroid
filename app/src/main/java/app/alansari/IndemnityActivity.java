package app.alansari;

import app.alansari.Utils.CommonUtils;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.xml.sax.XMLReader;

/**
 * Created by Parveen Dala on 05 November, 2016
 * Fugenx Technologies, Bengaluru
 * AlAnsari
 */
public class IndemnityActivity extends Dialog implements View.OnClickListener {
    boolean firstTag = true;
    private Context context;
    private Toolbar toolbar;
    private TextView tvText;
    private ImageView ivChecked;
    private boolean isAgreed;


    public IndemnityActivity(Context context) {
        super(context);
        this.context = context;
    }

    public IndemnityActivity(Context context, int themeResId) {
        super(context, themeResId);
        this.context = context;
    }

    private void init() {
        tvText = (TextView) findViewById(R.id.text);
        ivChecked = (ImageView) findViewById(R.id.accept_image);
        ivChecked.setOnClickListener(this);
        findViewById(R.id.accept_text).setOnClickListener(this);
        findViewById(R.id.submit_btn).setOnClickListener(this);
        findViewById(R.id.back_btn).setOnClickListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.indemnity_activity);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        ((TextView) findViewById(R.id.toolbar_title)).setText("TERMS AND CONDITIONS");
        toolbar.setBackground(ContextCompat.getDrawable(context, R.drawable.login_register_header_bg));
        init();

        CommonUtils.setLayoutFont(context, "HelveticaNeue-Medium.ttf", findViewById(R.id.toolbar_title));
        CommonUtils.setLayoutFont(context, "Roboto-Regular.ttf", tvText, findViewById(R.id.accept_text));
        CommonUtils.setLayoutFont(context, "Roboto-Medium.ttf", findViewById(R.id.submit_btn), findViewById(R.id.back_btn));

        tvText.setText(Html.fromHtml(context.getString(R.string.indemnity_form_text), null, new UlTagHandler()));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.accept_text:
            case R.id.accept_image:
                if (isAgreed) {
                    ivChecked.setImageResource(0);
                    isAgreed = false;
                } else {
                    ivChecked.setImageResource(R.drawable.svg_success);
                    isAgreed = true;
                }
                break;

            case R.id.submit_btn:
                if (isAgreed) {
                    this.dismiss();
                } else
                    Toast.makeText(context, context.getResources().getString(R.string.error_indemnity_form_text), Toast.LENGTH_SHORT).show();
                break;
            case R.id.back_btn:
                onCancel();
                break;
        }
    }

    private void onCancel() {
        CommonUtils.registerAgain(context);
        this.dismiss();
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }

    public class UlTagHandler implements Html.TagHandler {

        private CharSequence apply(CharSequence content, Object... tags) {
            SpannableStringBuilder text = new SpannableStringBuilder();
            openTags(text, tags);
            text.append(content);
            closeTags(text, tags);
            return text;
        }

        /**
         * Iterates over an array of tags and applies them to the beginning of the specified
         * Spannable object so that future text appended to the text will have the styling
         * applied to it. Do not call this method directly.
         */
        private void openTags(Spannable text, Object[] tags) {
            for (Object tag : tags) {
                text.setSpan(tag, 0, 0, Spannable.SPAN_MARK_MARK);
            }
        }

        /**
         * "Closes" the specified tags on a Spannable by updating the spans to be
         * endpoint-exclusive so that future text appended to the end will not take
         * on the same styling. Do not call this method directly.
         */
        private void closeTags(Spannable text, Object[] tags) {
            int len = text.length();
            for (Object tag : tags) {
                if (len > 0) {
                    text.setSpan(tag, 0, len, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                } else {
                    text.removeSpan(tag);
                }
            }
        }

        @Override
        public void handleTag(boolean opening, String tag, Editable output, XMLReader xmlReader) {
            if (firstTag) {
                if (tag.equals("li") && opening) {
                    output.append(apply((CharSequence) "•", new StyleSpan(Typeface.BOLD)));
                    firstTag = false;
                }
            } else {
                if (tag.equals("ul") && !opening) output.append("\n");
                if (tag.equals("li") && opening)
                    output.append("\n" + apply((CharSequence) "•", new StyleSpan(Typeface.BOLD)));
            }
        }
    }

}
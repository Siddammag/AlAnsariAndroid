package app.alansari.Utils;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.core.app.DialogFragment;

import java.util.Calendar;

/**
 * Created by Parveen Dala on 03 August, 2017
 * Fugenx Technologies, Bengaluru
 * Al Ansari
 */

public class DatePickerFragment extends DialogFragment {
    String dateRestriction;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        if (getArguments() != null) {
            String date = getArguments().getString(Constants.DATE, null);
            dateRestriction = getArguments().getString(Constants.DATE_RESTRICTION, null);
            if (date != null) {
                c.setTime(CommonUtils.getDateFromString(date));
            }
        }
        DatePickerDialog dialog = new DatePickerDialog(getActivity(), android.R.style.Theme_Holo_Light_Dialog, (DatePickerDialog.OnDateSetListener) getActivity(), c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
        Calendar currentTime = Calendar.getInstance();
        if (dateRestriction != null) {
            if (dateRestriction.equalsIgnoreCase(Constants.DATE_RESTRICTION_BOTH)) {
                dialog.getDatePicker().setMinDate(currentTime.getTimeInMillis() - 10000);
                dialog.getDatePicker().setMaxDate(currentTime.getTimeInMillis());
            } else if (dateRestriction.equalsIgnoreCase(Constants.DATE_RESTRICTION_MIN)) {
                dialog.getDatePicker().setMinDate(currentTime.getTimeInMillis() - 10000);
            } else if (dateRestriction.equalsIgnoreCase(Constants.DATE_RESTRICTION_MAX)) {
                dialog.getDatePicker().setMaxDate(currentTime.getTimeInMillis());
            }
        }
        dialog.setTitle(getTag());
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return dialog;
    }
}
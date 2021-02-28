package id.co.datascrip.app_collector_systems;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;

import java.text.DecimalFormat;
import java.util.Locale;

public class Utils {

    public static void hideKeyboard(Activity activity, View view) {
        try {
            view.requestFocus();
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        } catch (Exception e) {
            Log.e("hideKeyboard", e.toString());
        }
    }

    public static boolean isEmpty(String teks) {
        return teks == null || teks.trim().isEmpty();
    }

    public static void FormatNumberDecimal(Editable s, EditText e) {
        if (!s.toString().equals("")) {

            int start, end, cp, sel;
            String price;

            if (s.toString().contains("0."))
                price = s.toString();
            else
                price = formatNumber(Double.valueOf(s.toString().replaceAll(",", "")));

            start = e.getText().length();
            cp = e.getSelectionStart();
            if (!s.toString().trim().equals(price))
                e.setText(price);
            end = e.getText().length();
            sel = cp + (end - start);
            if (sel > 0 && sel <= e.getText().length()) {
                e.setSelection(sel);
            } else {
                // place cursor at the end?
                e.setSelection(e.getText().length());
            }

        }
    }

    public static String formatNumber(double d) {
        DecimalFormat numberFormat = new DecimalFormat("###,###,###.##");
        Locale.setDefault(Locale.US);
        return numberFormat.format(d);
    }

    public static String FloatToStr(float number, String formatNumber) {
        DecimalFormat numberFormat = new DecimalFormat(formatNumber);
        Locale.setDefault(Locale.US);
        return numberFormat.format(number);
    }

    public static double getDouble(EditText e) {
        double value;
        String txt = e.getText().toString().replaceAll(",", "").trim();
        if (txt.equals(""))
            value = 0.0;
        else
            value = Double.parseDouble(txt);

        return value;
    }

    public static void showErrorMessage(final Context context, String message, final boolean finish) {
        if (message != null)
            new AlertDialog.Builder(context)
                    .setMessage(Html.fromHtml(message))
                    .setPositiveButton("OK", (dialog, which) -> {
                        if (finish)
                            ((Activity) context).finish();
                    })
                    .setOnDismissListener(dialog -> {
                        if (finish)
                            ((Activity) context).finish();
                    })
                    .create()
                    .show();
    }
}

package com.omegaspocktari.inventoryapp.data;

import android.widget.EditText;
import android.widget.ImageView;

/**
 * Created by ${Michael} on 9/21/2016.
 */
public class ProductValidation {
    private static final String LOG_TAG = ProductValidation.class.getSimpleName();

    public static boolean checkBlank(EditText editTextField) {
        return editTextField.getText().toString().trim().length() <= 0;
    }

    public static boolean checkIsFloat(EditText editTextField) {
        try {
            Float.parseFloat(editTextField.getText().toString());

            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean checkIsInteger(EditText editTextField) {
        try {
            Integer.parseInt(editTextField.getText().toString());
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean checkIsPositiveInteger(int integer) {
        return integer >= 0;
    }

    public static boolean checkBlank(ImageView imageViewField) {
        return imageViewField.getDrawable() == null;
    }

    public static String formatFloat(Float f) {
        float epsilon = 0.004f;
        float storage = f;
        if (Math.abs(Math.round(f) - f) < epsilon) {
            /** 10 characters in with with 0 places after decimal */
            return String.format("$%-10.2f", storage);
        } else {
            return String.format("$%-10.2f", storage);
        }


    }
}

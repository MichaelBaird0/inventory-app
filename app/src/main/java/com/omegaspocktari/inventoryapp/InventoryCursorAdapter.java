package com.omegaspocktari.inventoryapp;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.omegaspocktari.inventoryapp.data.InventoryContract.ProductEntry;
import com.omegaspocktari.inventoryapp.data.ProductValidation;

/**
 * Created by ${Michael} on 9/16/2016.
 */
public class InventoryCursorAdapter extends CursorAdapter {
//
    //
    //
    private Context mContext;
    private ContentValues values;
    private static final String LOG_TAG = InventoryCursorAdapter.class.getSimpleName();
    private ContentResolver contentResolver;

    public InventoryCursorAdapter(Activity context, Cursor cursor, ContentResolver resolver) {
        super(context, cursor, 0);
        contentResolver = resolver;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.product_list_item, viewGroup, false);

    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView productName = (TextView) view.findViewById(R.id.product_name);
        TextView productPrice = (TextView) view.findViewById(R.id.product_price);
        TextView productQuantity = (TextView) view.findViewById(R.id.product_quantity);
        Button productSell = (Button) view.findViewById(R.id.product_sell);



        String name = cursor.getString(cursor.getColumnIndexOrThrow(ProductEntry.COLUMN_PRODUCT_NAME));
//TODO: Format this to an actual decimal price number
        String price = "Price: " + ProductValidation.formatFloat(cursor.getFloat(cursor.getColumnIndexOrThrow(ProductEntry.COLUMN_PRODUCT_PRICE)));
        final String quantity = "Quantity: " + String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow(ProductEntry.COLUMN_PRODUCT_CURRENT_QUANTITY)));

        productName.setText(name);
        productPrice.setText(price);
        productQuantity.setText(quantity);

        final int currentQuantity = cursor.getInt(cursor.getColumnIndexOrThrow(ProductEntry.COLUMN_PRODUCT_CURRENT_QUANTITY));
        final int currentId = cursor.getInt(cursor.getColumnIndexOrThrow(ProductEntry._ID));

        productSell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int quantity = currentQuantity;

                if (quantity > 0) {

                    Uri uri = Uri.parse(ProductEntry.CONTENT_URI + "/" + currentId);

                    //TODO: What am I doing.
                    int quantityChange = quantity--;
                    String where = ProductEntry._ID + "=?";
                    String[] whereArgs = {String.valueOf(currentId)};

                    ContentValues values = new ContentValues();
                    values.put(ProductEntry.COLUMN_PRODUCT_CURRENT_QUANTITY, quantityChange);

                    contentResolver.update(uri, values, where, whereArgs);

                } else {
                    Toast.makeText(mContext, "Cannot Reduce Quantity Below 0...", Toast.LENGTH_SHORT).show();
                }
            }
        });
            //TODO: Implement this method.

    }
}

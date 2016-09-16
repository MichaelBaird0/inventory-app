package com.omegaspocktari.inventoryapp;

import android.app.Activity;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.omegaspocktari.inventoryapp.data.InventoryContract.ProductEntry;

/**
 * Created by ${Michael} on 9/16/2016.
 */
public class InventoryAdapter extends CursorAdapter {

    private Context mContext;
    private ContentValues values;

    public InventoryAdapter(Activity context, Cursor cursor) {
        super(context, cursor, 0);

    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.product_list_item, viewGroup, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView productName = (TextView) view.findViewById(R.id.product_name);
        TextView productPrice = (TextView) view.findViewById(R.id.product_price);
        final TextView productQuantity = (TextView) view.findViewById(R.id.product_quantity);

        final long id = cursor.getLong(cursor.getColumnIndexOrThrow(ProductEntry._ID));
        productName.setText(cursor.getString(cursor.getColumnIndexOrThrow(ProductEntry.COLUMN_PRODUCT_NAME)));
        //TODO: Format this to an actual decimal price number
        productPrice.setText("$" + cursor.getColumnIndexOrThrow(ProductEntry.COLUMN_PRODUCT_PRICE));
        productQuantity.setText(cursor.getColumnIndexOrThrow(ProductEntry.COLUMN_PRODUCT_CURRENT_QUANTITY));

        Button productSell = (Button) view.findViewById(R.id.product_sell);
        productSell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("Adapter", "here is the id value: " + id);
                String selection = "_ID=?";
                String[] selectionArgs = { Long.toString(id) };
                Cursor query;
                query = mContext.getContentResolver().query(
                        ProductEntry.CONTENT_URI,
                        null,
                        selection, /* Looking for a certain item */
                        selectionArgs, /* The argument for the certain item is the id */
                        null
                );

                // TODO: review this. God this is complicated.
                query.moveToFirst();
                int quantity = Integer.parseInt(query.getString(query.getColumnIndexOrThrow(ProductEntry.COLUMN_PRODUCT_CURRENT_QUANTITY)));
                query.close();

                if (quantity > 0) {
                    quantity--;
                }

                /* This updates the visual text view of the list view item */
                productQuantity.setText(String.valueOf(quantity));

                /**
                 * This code creates a ContentValues object that then uses the content resolver's
                 * update method.
                 */
                values = new ContentValues();
                values.put(ProductEntry.COLUMN_PRODUCT_CURRENT_QUANTITY, String.valueOf(quantity));
                Uri uri = ContentUris.withAppendedId(ProductEntry.CONTENT_URI, id);
                mContext.getContentResolver().update(uri, values, null, null);
            }
        });
    }
}

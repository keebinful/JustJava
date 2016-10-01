package com.example.android.justjava;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.net.URI;

/**
 * This app displays an order form to order coffee.
 */
public class MainActivity extends AppCompatActivity {

    int quantity = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * This method is called when the plus button is clicked.
     */
    public void increment(View view) {
        if (quantity == 100) {
            Toast.makeText(this, "You cannot order more than 100 cups of coffee.", Toast.LENGTH_SHORT).show();
            return;
        }
        quantity = quantity + 1;
        displayQuantity(quantity);
    }

    /**
     * This method is called when the minus button is clicked.
     */
    public void decrement(View view) {
        if (quantity == 1) {
            Toast.makeText(this, "You cannot order less than 1 cup of coffee.", Toast.LENGTH_SHORT).show();
            return;
        }
        quantity = quantity - 1;
        displayQuantity(quantity);
    }

    /**
     * This method is called when the order button is clicked.
     */
    public void submitOrder(View view) {
        // Finds and stores user's namein nameText variable as String
        EditText nameEditText = (EditText) findViewById(R.id.name_text_field);
        String nameText = nameEditText.getText().toString();

        // Checks boolean status of Checkboxes for toppings and stores them in variables
        boolean addWhippedCream = ((CheckBox) findViewById(R.id.whipped_cream_checkbox)).isChecked();
        boolean addChocolate = ((CheckBox) findViewById(R.id.chocolate_checkbox)).isChecked();

        //Calculates total price of the order including toppings
        int price = calculatePrice(addWhippedCream, addChocolate);
        String priceMessage = createOrderSummary(nameText, addWhippedCream, addChocolate, price);

        // Opens email app with subject filled, body filled with priceMessage
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_SUBJECT, "JustJava order for " + nameText);
        intent.putExtra(Intent.EXTRA_TEXT, priceMessage);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
    }
    }

    /**
     * This method displays the given quantity value on the screen.
     */
    private void displayQuantity(int numberOfCoffees) {
        TextView quantityTextView = (TextView) findViewById(R.id.quantity_text_view);
        quantityTextView.setText("" + numberOfCoffees);
    }

    /**
     * @param addWhippedCream boolean whether user wants whipped cream topping
     * @param addChocolate    boolean whether user wants chocolate topping
     * @return returns total price
     */
    private int calculatePrice(boolean addWhippedCream, boolean addChocolate) {
        int basePrice = 5; // Price of 1 cup of coffee
        // Add $1 if whipped cream requested
        if (addWhippedCream) {
            basePrice += 1;
        }
        // Add $2 if chocolate is requested
        if (addChocolate) {
            basePrice += 2;
        }
        // Returns total price of full order (currently assumes each cup has the same toppings)
        return quantity * basePrice;
    }

    /**
     * Creates summary of the order.
     *
     * @param addWhippedCream is whether user wants whipped cream topping
     * @return text summary
     */
    private String createOrderSummary(String nameText, boolean addWhippedCream, boolean addChocolate, int price) {
        String priceMessage = getString(R.string.order_summary_name) + nameText;
        priceMessage += "\n" + getString(R.string.order_summary_whipped_cream) + addWhippedCream;
        priceMessage += "\n" + getString(R.string.order_summary_chocolate) + addChocolate;
        priceMessage += "\n" + getString(R.string.order_summary_quantity) + quantity;
        priceMessage += "\n" + getString(R.string.order_summary_price) + price;
        priceMessage += "\n" + getString(R.string.thank_you);
        return priceMessage;
    }

}
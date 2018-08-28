package com.udacity.sandwichclub;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.udacity.sandwichclub.model.Sandwich;
import com.udacity.sandwichclub.utils.JsonUtils;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_POSITION = "extra_position";
    private static final int DEFAULT_POSITION = -1;

    //Declaring variables
    TextView descriptionTextView;
    TextView alsoKnownAsTextView;
    TextView placeOfOriginTextView;
    TextView ingredientsTextView;


    String sandwichDescriptionText;
    String sandwichOriginText;
    //        AKA data type is list. Need to do a for loop here to append each value in the list
//        Ingredients data type is list. Use the same for loop as above for appending values
    List<String> sandwichAKAText = new ArrayList<>();
    List<String> ingredientsListText = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        final ImageView ingredientsIv = findViewById(R.id.image_iv);


        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
            Log.i("INTENT ERROR", "ERROR IN INTENT CHECKER");
        }

        int position = intent.getIntExtra(EXTRA_POSITION, DEFAULT_POSITION);
        if (position == DEFAULT_POSITION) {
            // EXTRA_POSITION not found in intent
            closeOnError();
            Log.i("POSITION ERROR", "ERROR IN POSITION CHECKER");

            return;
        }

        String[] sandwiches = getResources().getStringArray(R.array.sandwich_details);
        String json = sandwiches[position];
        Sandwich sandwich = null;
        try {
            sandwich = JsonUtils.parseSandwichJson(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (sandwich == null) {
            // Sandwich data unavailable
            Log.i("SANDWICH ERROR", "ERROR IN JSOSNUTILS CHECKER");
            Log.i("SANDWICH DETAILS", json);
            closeOnError();
            return;
        }
        sandwichDescriptionText = sandwich.getDescription();
        sandwichOriginText = sandwich.getPlaceOfOrigin();
        ingredientsListText = sandwich.getIngredients();
        sandwichAKAText = sandwich.getAlsoKnownAs();

        populateUI();
            Picasso.with(this)
                    .load(sandwich.getImage())
//  Use of callback for image not loaded found here on StackOverflow: https://stackoverflow.com/questions/17052538/picasso-library-android-using-error-listener
                    .into(ingredientsIv, new Callback() {

            @Override
            public void onSuccess() {

            }

            @Override
            public void onError() {
//                noimg.jpg source - https://www.dia.org/sites/default/files/No_Img_Avail.jpg
                ingredientsIv.setImageDrawable(getResources().getDrawable(R.drawable.noimg));
            }
        });

        setTitle(sandwich.getMainName());
    }

    private void closeOnError() {
        finish();
        // TODO change toast to show sandwich name* data not available
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

    private void populateUI() {
        // Get empty textviews from the activity_detail page
        descriptionTextView = findViewById(R.id.description_tv);
        alsoKnownAsTextView = findViewById(R.id.also_known_tv);
        placeOfOriginTextView = findViewById(R.id.origin_tv);
        ingredientsTextView = findViewById(R.id.ingredients_tv);
        alsoKnownAsTextView = findViewById(R.id.also_known_tv);

        //Add the text from parsedJSON to the empty textviews
        descriptionTextView.append(sandwichDescriptionText);

//        adding "unknown" to TextView if no value for Origin in sandwich model
        if (sandwichOriginText.equals("")) {
            placeOfOriginTextView.setText("Unknown");
        }
        else{
            placeOfOriginTextView.append(sandwichOriginText);
        }

        for (int i = 0; i < ingredientsListText.size(); i++) {
            ingredientsTextView.append(ingredientsListText.get(i) + "\n");

        }

//        adding default text to akaTextView if there are no values from sandwich model
        if (sandwichAKAText.size() == 0) {
            alsoKnownAsTextView.setText("No alternative names");

        } else {
            for (int i = 0; i < sandwichAKAText.size(); i++) {
                alsoKnownAsTextView.append(sandwichAKAText.get(i) + "\n");

            }
        }
    }
}

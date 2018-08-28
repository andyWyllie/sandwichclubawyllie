package com.udacity.sandwichclub.utils;

import android.util.Log;

import com.udacity.sandwichclub.model.Sandwich;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonUtils {

    public static Sandwich parseSandwichJson(String json) throws JSONException {
//    TODO get json data from strings.xml
//    TODO add json data into custom Sandwich object
        JSONObject sandwichData = new JSONObject(json);
        JSONObject sandwichName = sandwichData.getJSONObject("name");
        JSONArray sandwichIngredients = sandwichData.getJSONArray("ingredients");
        JSONArray sandwichAKA = sandwichName.getJSONArray("alsoKnownAs");


        Sandwich parsedSandwich = new Sandwich();
        parsedSandwich.setMainName(sandwichName.getString("mainName"));
        parsedSandwich.setDescription(sandwichData.getString("description"));
        parsedSandwich.setImage(sandwichData.getString("image"));
        parsedSandwich.setPlaceOfOrigin(sandwichData.getString("placeOfOrigin"));

//       for loop that will add all ingredients to a list called ingredientsList
        List<String> ingredientsList = new ArrayList<>();
        for(int i = 0; i < sandwichIngredients.length(); i++){
            ingredientsList.add(sandwichIngredients.get(i).toString());
            Log.i("TEST DATA", ingredientsList.get(i));
        }
//       for loop that will add all known values from AKA to a list
        List<String> akaList = new ArrayList<>();
        for(int i = 0; i < sandwichAKA.length(); i++){
            akaList.add(sandwichAKA.get(i).toString());
            Log.i("TEST DATA", akaList.get(i));
        }
        parsedSandwich.setIngredients(ingredientsList);
        parsedSandwich.setAlsoKnownAs(akaList);


        return parsedSandwich;
    }
}

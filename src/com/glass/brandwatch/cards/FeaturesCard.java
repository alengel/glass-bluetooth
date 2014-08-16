package com.glass.brandwatch.cards;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.glass.brandwatch.R;
import com.google.gson.Gson;

public class FeaturesCard {

	public static View build(Context context, String featuresData) {

		Data data = new Gson().fromJson(featuresData, Data.class);
		Product product = data.results.get(0);

		View view = View.inflate(context, R.layout.features_card, null);

		ListView featuresList = (ListView) view.findViewById(R.id.features_list);
		List<String> features = new ArrayList<String>();
		features.add("Colour: " + product.color);

		for (Map.Entry<String, String> entry : product.features.entrySet()) {
			features.add(entry.getKey() + ": " + entry.getValue());
		}

		ArrayAdapter<String> modeAdapter = new ArrayAdapter<String>(context,
				R.layout.features_list_item, android.R.id.text1, features);
		featuresList.setAdapter(modeAdapter);

		TextView footer = (TextView) view.findViewById(R.id.features_footer);
		footer.setText(product.name + " Features");

		return view;
	}

	// Create a Data class that is a list of the retrieved products
	private class Data {
		public List<Product> results;
	}

	// Create a product class for each product
	private class Product {
		public String name;
		public String color;
		public Map<String, String> features;
	}
}

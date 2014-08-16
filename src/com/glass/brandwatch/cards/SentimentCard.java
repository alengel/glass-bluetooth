package com.glass.brandwatch.cards;

import java.util.List;

import org.apache.commons.lang3.text.WordUtils;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.glass.brandwatch.R;
import com.google.gson.Gson;

public class SentimentCard {

	// Setup the template and return the card
	public static View build(Context context, String sentimentData) {
		Data data = new Gson().fromJson(sentimentData, Data.class);
		List<Value> results = data.results.get(0).values;

		View view = View.inflate(context, R.layout.sentiment_card, null);

		buildVolumeRow(view, results);
		buildNegativeRow(view, results);
		buildPositiveRow(view, results);

		TextView footer = (TextView) view.findViewById(R.id.sentiment_footer);
		footer.setText("Brandwatch");

		return view;
	}

	// Get the TextViews that make up the volume row and set results text
	private static void buildVolumeRow(View view, List<Value> results) {
		Integer volumeCount = results.get(1).value + results.get(2).value;

		TextView volume = (TextView) view.findViewById(R.id.volume);
		TextView volumeValue = (TextView) view.findViewById(R.id.volume_value);

		volume.setText("Volume");
		volumeValue.setText(volumeCount.toString());
	}

	// Get the TextViews that make up the negative row and set results text
	private static void buildNegativeRow(View view, List<Value> results) {
		TextView negative = (TextView) view.findViewById(R.id.negative);
		TextView negativeValue = (TextView) view.findViewById(R.id.negative_value);

		negative.setText(WordUtils.capitalize(results.get(1).name));
		negativeValue.setText(results.get(1).value.toString());
	}

	// Get the TextViews that make up the positive row and set results text
	private static void buildPositiveRow(View view, List<Value> results) {
		TextView positive = (TextView) view.findViewById(R.id.positive);
		TextView positiveValue = (TextView) view.findViewById(R.id.positive_value);

		positive.setText(WordUtils.capitalize(results.get(2).name));
		positiveValue.setText(results.get(2).value.toString());
	}

	// Private classes used to build an object representation
	// of the returned sentiment JSON
	private class Data {
		public List<Result> results;
	}

	private class Result {
		public List<Value> values;
	}

	private class Value {
		public String name;
		public Integer value;
	}
}

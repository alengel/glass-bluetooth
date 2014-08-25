package com.glass.brandwatch.cards;

import java.util.List;

import org.apache.commons.lang3.text.WordUtils;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.glass.brandwatch.R;
import com.google.gson.Gson;

public class SentimentCard {

	public static View build(Context context, String sentimentData) {
		Data data = new Gson().fromJson(sentimentData, Data.class);

		List<Value> results = data.results.get(0).values;
		Integer volumeCount = results.get(1).value + results.get(2).value;

		View view = View.inflate(context, R.layout.sentiment_card, null);

		TextView volume = (TextView) view.findViewById(R.id.volume);
		TextView volumeValue = (TextView) view.findViewById(R.id.volume_value);

		TextView negative = (TextView) view.findViewById(R.id.negative);
		TextView negativeValue = (TextView) view.findViewById(R.id.negative_value);

		TextView positive = (TextView) view.findViewById(R.id.positive);
		TextView positiveValue = (TextView) view.findViewById(R.id.positive_value);

		volume.setText("Volume");
		volumeValue.setText(volumeCount.toString());
		
		Log.v("sentiment results size ", Integer.toString(results.size()));
		for (int i = 0; i < results.size(); i++) {
			Value sentiment = results.get(i);
			
			//Set positive textview with values from data
			if(sentiment.name.equals("positive")) {
				positive.setText(WordUtils.capitalize(sentiment.name));
				positiveValue.setText(sentiment.value.toString());
			}
			//Set negative textview with values from data
			if(sentiment.name.equals("negative")) {
				negative.setText(WordUtils.capitalize(sentiment.name));
				negativeValue.setText(sentiment.value.toString());
			}
		}

		TextView footer = (TextView) view.findViewById(R.id.sentiment_footer);
		footer.setText("Brandwatch");

		return view;
	}

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
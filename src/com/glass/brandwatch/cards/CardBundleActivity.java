package com.glass.brandwatch.cards;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.glass.app.Card;
import com.google.android.glass.widget.CardScrollAdapter;
import com.google.android.glass.widget.CardScrollView;

public class CardBundleActivity extends Activity {

	private List<View> cardsBundle;
	private CardScrollView cardScrollView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Create the individual cards
		createCards();

		// Setup the scroller, that allows for scrolling between multiple cards
		cardScrollView = new CardScrollView(this);
		ScrollAdapter adapter = new ScrollAdapter();
		cardScrollView.setAdapter(adapter);
		cardScrollView.activate();
		setContentView(cardScrollView);
	}

	// Get the intent data and build the cards
	private void createCards() {
		Intent intent = getIntent();
		List<String> data = intent.getStringArrayListExtra("data");

		// Extract the data from the array
		String featuresData = data.get(0);
		String sentimentData = data.get(1);
		String topicsData = data.get(2);

		// Build the individual cards from the data
		cardsBundle = new ArrayList<View>();
		cardsBundle.add(SentimentCard.build(this, sentimentData));
		cardsBundle.add(TopicsCard.build(this, topicsData));
		cardsBundle.add(FeaturesCard.build(this, featuresData));

		Log.v("CardBundleActivity", "finished building the cards");
	}

	// Generic functions that need to be overridden to setup the ScrollAdapter
	private class ScrollAdapter extends CardScrollAdapter {

		@Override
		public int getPosition(Object item) {
			return cardsBundle.indexOf(item);
		}

		@Override
		public int getCount() {
			return cardsBundle.size();
		}

		@Override
		public Object getItem(int position) {
			return cardsBundle.get(position);
		}

		@Override
		public int getViewTypeCount() {
			return Card.getViewTypeCount();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			return cardsBundle.get(position);
		}
	}
}

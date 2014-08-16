package com.glass.brandwatch.cards;

import java.util.Collections;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.glass.brandwatch.R;
import com.google.gson.Gson;

public class TopicsCard {

	private final static int startTextSize = 24;
	private final static int maxTopic = 7;

	public static View build(Context context, String topicsData) {

		Data data = new Gson().fromJson(topicsData, Data.class);
		View view = View.inflate(context, R.layout.topics_card, null);

		int count = Math.min(data.topics.size(), maxTopic);
		Collections.sort(data.topics, Collections.reverseOrder(new TopicVolumeComparer()));

		int textSize = startTextSize;
		for (int i = 1; i <= count; i++) {
			Topic topic = data.topics.get(i);
			String layoutId = "topics" + i;

			// Set the topic text in the view
			int layoutResourceId = context.getResources().getIdentifier(layoutId, "id",
					context.getPackageName());
			TextView topicView = (TextView) view.findViewById(layoutResourceId);
			topicView.setText(topic.label);

			// Set the text colour according to the most present sentiment
			setTextColour(topic.sentiment, topicView);

			// Set the text size according to volume
			topicView.setTextSize(textSize--);
		}

		TextView footer = (TextView) view.findViewById(R.id.topics_footer);
		footer.setText("Brandwatch");

		return view;
	}

	private static void setTextColour(Sentiment sentiment, TextView topics1) {

		// If negative sentiment is greater than positive and neutral, return
		// red
		if (sentiment.negative > sentiment.positive && sentiment.negative > sentiment.neutral) {
			topics1.setTextColor(Color.parseColor("#FF0000"));
		}

		// If positive sentiment is greater than negative and neutral, return
		// green
		if (sentiment.positive > sentiment.negative && sentiment.positive > sentiment.neutral) {
			topics1.setTextColor(Color.parseColor("#00FF00"));
		}
	}

	// Private classes used to build an object representation
	// of the returned topics JSON
	private class Data {
		public List<Topic> topics;
	}

	// Create a topic class for each topic
	public class Topic {
		public String label;
		public Integer volume;
		public Sentiment sentiment;
	}

	// Create a sentiment class for the sentiment within each topic
	private class Sentiment {
		public int negative = 0;
		public int neutral = 0;
		public int positive = 0;
	}
}

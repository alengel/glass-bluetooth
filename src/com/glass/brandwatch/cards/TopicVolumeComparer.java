package com.glass.brandwatch.cards;

import java.util.Comparator;

import com.glass.brandwatch.cards.TopicsCard.Topic;

public class TopicVolumeComparer implements Comparator<Topic> {
	@Override
	public int compare(Topic x, Topic y) {
		return x.volume - y.volume;
	}
}

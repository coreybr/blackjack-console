package io.github.coreybr;

import java.util.concurrent.ThreadLocalRandom;

public class Card {

	private Suits suit;
	private String face;
	private int value;

	private enum Suits {
		HEARTS("Hearts"), SPADES("Spades"), CLUBS("Clubs"), DIAMONDS("Diamonds");

		private final String text;

		Suits(final String text) {
			this.text = text;
		}

		@Override
		public String toString() {
			return text;
		}
	}

	Card() {

		// Create random number from 3 to 6 for the card suit
		suit = Suits.values()[ThreadLocalRandom.current().nextInt(0, 4)];

		// Create a random number from 2 to 14 ( 2 to Ace)
		int number = ThreadLocalRandom.current().nextInt(2, 14 + 1);

		if (number >= 2 && number <= 10) {
			value = number;
			face = "" + number;
		} else if (number == 11) {
			value = 10;
			face = "Jack";
		} else if (number == 12) {
			value = 10;
			face = "Queen";
		} else if (number == 13) {
			value = 10;
			face = "King";
		} else if (number == 14) {
			value = 11;
			face = "Ace";
		} else // error case
		{
			value = -1;
			face = "Joker";
		}

	}

	public String toString() {
		String result = "";
		result += face + " of " + suit;
		return result;
	}

	public void flipAceToOne() {
		if (value == 11) {
			value = 1;
		}
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

}

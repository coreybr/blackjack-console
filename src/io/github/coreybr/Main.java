package io.github.coreybr;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {
	
	private int cash;

	public static void main(String[] args) {
		Main game = new Main();
		Scanner reader = new Scanner(System.in);

		// Create starting cash amount and display it to the user
		game.setCash(100);
		System.out.println("Welcome to Blackjack!");
		System.out.println("Starting game balance is $" + game.getCash() + "\n");

		// Create loop variable
		int choice = 0;

		// Run loop
		do {
			// Show menu and get users choice
			System.out.println("Menu");
			System.out.println("1) Play a hand");
			System.out.println("2) Show current cash balance");
			System.out.println("3) Exit");

			System.out.print("\nEnter your choice: ");
			try {
				choice = Integer.parseInt(reader.nextLine());
			} catch (NumberFormatException e) {
				System.out.println("Please enter a number");
				continue;
			}

			// Switch from users choice
			switch (choice) {
			case 1:
				game.playHand(reader);
				break;
			case 2:
				System.out.println("\nYour cash balance is now: $" + game.getCash());
				if (game.getCash() < 1) {
					System.out.println("Restart to play again!");
				}
				break;
			case 3:
				System.out.println("\nYou are exiting BlackJack, thank you for playing!");
				System.out.println("\nFinal Cash Balance: $" + game.getCash());
				break;
			default:
				System.out.println("Error. Please select menu option 1, 2, or 3.");
				break;
			}

			game.pressAnyKeyToContinue();

		} while (choice != 3);

	}

	/**
	 * Show the cards in the ArrayList
	 * 
	 * @param cards
	 * @return
	 */
	String showCards(ArrayList<Card> cards) {
		String output = "";

		for (int i = 0; i < cards.size(); i++) {
			output += cards.get(i);
			if (i < cards.size() - 1) {
				output += ',';
			}
			output += ' ';
		}

		return output;
	}

	/**
	 * Add up the value of all cards in ArrayList
	 * 
	 * @param cards
	 * @return
	 */
	private int sumCardValues(ArrayList<Card> cards) {
		int output = 0;
		for (Card c : cards) {
			output += c.getValue();
		}

		return output;
	}

	/**
	 * Add up the value of all cards in ArrayList
	 * 
	 * @param reader
	 */
	private void playHand(Scanner reader) {
		// Initialize variables
		ArrayList<Card> dealerCards = new ArrayList<Card>();
		ArrayList<Card> playerCards = new ArrayList<Card>();
		int playerTotal = 0;
		int dealerTotal = 0;

		// Get player bet
		if (cash < 1) {
			System.out.println("\nSorry, looks like you're out of cash.");
			return;
		}
		int bet = 0;
		System.out.print("\nWhat will you bet? $");
		try {
			bet = Integer.parseInt(reader.nextLine());
		} catch (NumberFormatException e) {
			System.out.println("Please enter a number");
		}

		if (bet < 0 || bet > cash) {
			System.out.println("\nInvalid bet amount!");
			return;
		}

		// Draw dealer's cards
		Card dealerCard = new Card();
		Card dealerCard2 = new Card();
		dealerCards.add(dealerCard);
		dealerCards.add(dealerCard2);
		dealerTotal = sumCardValues(dealerCards);

		// Show dealer's first card
		System.out
				.println("\nThe dealer's first card is a: " + dealerCards.get(0).toString() + " (" + dealerTotal + ")");

		// Draw player's cards
		Card playerCard = new Card();
		Card playerCard2 = new Card();
		playerCards.add(playerCard);
		playerCards.add(playerCard2);
		playerTotal = sumCardValues(playerCards);

		// Show player's cards
		System.out.println("\nYour cards are: " + showCards(playerCards) + "(" + playerTotal + ")");

		// Prompt player to hit or stand
		char answer;

		do {
			System.out.print("\nDo you hit or stand? (H/S): ");

			answer = reader.next().charAt(0);

			if (Character.toUpperCase(answer) == 'H') {
				playerTotal = hitPlayer(playerCards);

				// Stay if player busted
				if (playerTotal > 21) {
					answer = 'S';
				}

			}
		} while (Character.toUpperCase(answer) != 'S');

		// Player busted
		if (playerTotal > 21) {
			System.out.println("\nYou went over!");
			cash = cash - bet;
		} else {
			// Draw dealer cards until 17 or more
			while (dealerTotal < 17) {
				dealerCard = new Card();
				dealerCards.add(dealerCard);
				dealerTotal = sumCardValues(dealerCards);
			}

			System.out.println("\nYour cards are: " + showCards(playerCards) + "(" + playerTotal + ")");
			System.out.println("The dealer's cards are: " + showCards(dealerCards) + "(" + dealerTotal + ")");

			if (dealerTotal > 21) {
				System.out.println("\nThe dealer busted!");
				cash = cash + bet;
			} else if (dealerTotal > playerTotal) {
				System.out.println("\nThe dealer wins!");
				cash = cash - bet;
			} else if (dealerTotal < playerTotal) {
				System.out.println("\nYou win!");
				cash = cash + bet;
			} else {
				System.out.println("\nYou pushed the dealer (tied).");
			}

		}

		// Show cash balance
		System.out.println("\nYour cash balance is now: $" + cash);
	}

	/**
	 * Draw card for player
	 * 
	 * @param playerCards
	 * @return
	 */
	private int hitPlayer(ArrayList<Card> playerCards) {
		int playerTotal;
		// Draw a card for the player
		Card card = new Card();
		System.out.println("\nYou are dealt a: " + card.toString());
		playerCards.add(card);
		playerTotal = sumCardValues(playerCards);

		// See if need to flip Ace to one
		if (playerTotal > 21) {
			playerTotal = checkForAceFlips(playerCards, playerTotal);
		}
		System.out.println("\nYour cards are: " + showCards(playerCards) + "(" + playerTotal + ")");
		return playerTotal;
	}

	/**
	 * Check player cards for Ace to flip from 11 to 1
	 * 
	 * @param playerCards
	 * @param playerTotal
	 * @return
	 */
	private int checkForAceFlips(ArrayList<Card> playerCards, int playerTotal) {
		for (Card c : playerCards) {
			// Flip found Ace
			if (c.getValue() == 11) {
				System.out.println("\nYour total is " + playerTotal);
				System.out.println("However, you have an Ace that was flipped to a 1");
				c.flipAceToOne();
				playerTotal = sumCardValues(playerCards);

				// Break out once player total below 21
				if (playerTotal < 21) {
					break;
				}
			}
		}
		return playerTotal;
	}

	/**
	 * Pause waiting for input
	 */
	private void pressAnyKeyToContinue() {
		System.out.println("Press Enter to continue...");
		try {
			System.in.read();
		} catch (Exception e) {
		}
	}

	public int getCash() {
		return cash;
	}

	public void setCash(int cash) {
		this.cash = cash;
	}

}

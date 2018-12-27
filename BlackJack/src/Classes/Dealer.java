/*
 * Class for dealer-objects in the BlackJack game
 * */

package Classes;

import java.util.ArrayList;

public class Dealer {
	// variable fields
	private ArrayList<Card> cards; // composition

	// constructor
	public Dealer() {
		cards = new ArrayList<>();
	}

	// function for adding a card into the dealer object
	public void addCard(Card card) {
		cards.add(card);
	}

	// function that returns the cards the dealer has picked up
	public Card[] getCards() {
		return cards.toArray(new Card[cards.size()]);
	}

	// function for printing out the cards picked up by the dealer
	public void printCards() {
		System.out.print("Dealer's cards: ");

		if (cards.size() == 0)
			System.out.println("(no cards).");
		else {
			System.out.println();
			for (int n = 0; n < cards.size(); n++)
				System.out.printf("%d. %s%n", n + 1, cards.get(n).toString());
		}
	}
}

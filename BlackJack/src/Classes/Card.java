/*
 * Class for card-objects in the BlackJack game
 * */

package Classes;

public class Card {
	// constant fields (available ranks and suits for cards)
	public static final String[] RANKS = new String[] { "Ace", "2", "3", "4", "5", "6", "7", "8", "9", "10", "Jack", "Queen", "King" };
	public static final String[] SUITS = new String[] { "Diamond", "Club", "Heart", "Spade" };

	// variable fields
	private int rank;
	private int suit;

	// constructor
	public Card(int rank, int suit) {
		this.rank = rank;
		this.suit = suit;
	}

	// convert the card object to string
	public String toString() {
		return String.format("%s of %ss", RANKS[rank], SUITS[suit]);
	}

	// get integer value of the card object
	public int getValue() {
		return rank == 0 ? 11 : (rank > 9 ? 10 : rank + 1);
	}
}

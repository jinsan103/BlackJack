/*
 * Class for player-objects in the BlackJack game
 * */

package Classes;

import java.util.ArrayList;

public class Player {
	// constant fields (default balance for new player objects)
	public static int DEF_BALANCE = 10;

	// variable fields
	private String name;
	private int balance;
	private ArrayList<Card> cards; // composition

	// constructor
	public Player(String name, int balance) {
		this.name = name;
		this.balance = balance;
		cards = new ArrayList<>();
	}

	// function for adding a card into the player object
	public void addCard(Card card) {
		cards.add(card);
	}

	// function for checking if the player has required amount in balance
	public boolean hasAmountInBalance(int amount) {
		return amount <= balance;
	}

	// function for withdrawing the bet amount from player's balance
	public void withdrawBetAmount(int bet) {
		balance -= bet;
	}

	// function for depositing money into player's balance (logically after winning the game)
	public void deposit(int amount) {
		balance += amount;
	}

	// function for checking if the balance is equal to 0 (meaning that the player is betting all they have)
	public boolean isGoingVaBanque() {
		return balance == 0;
	}

	// function for printing the player's balance to output log screen
	public void printBalance() {
		System.out.printf("Balance: $%d%n", balance);
	}

	// function for printing out the cards picked up by the player
	public void printCards() {
		System.out.printf("%s's cards: ", name);

		if (cards.size() == 0)
			System.out.println("(no cards).");
		else {
			System.out.println();
			for (int n = 0; n < cards.size(); n++)
				System.out.printf("%d. %s%n", n + 1, cards.get(n).toString());
		}
	}

	// function that returns the cards the player has picked up
	public Card[] getCards() {
		return cards.toArray(new Card[cards.size()]);
	}

	// function that returns the name of the player
	public String getName() {
		return name;
	}
}

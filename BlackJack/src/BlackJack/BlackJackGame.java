/*
 * Class for BlackJackGame-objects.
 * This class manages the whole process of the game, containing all the rules and the sequences of the game.
 * */

package BlackJack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Random;
import java.util.Scanner;

import javax.swing.plaf.synth.SynthSpinnerUI;

import Classes.Card;
import Classes.Dealer;
import Classes.Player;

public class BlackJackGame {
	// constant fields
	public static final int SCORE_BORDER = 21; // the critical score (maximum score)
	public static final int SOFT_17 = 17; // the comfortable score for a dealer

	// variable fields
	private ArrayList<Card> cards; // the deck of cards
	private ArrayList<Card> shuffled;// shuffled deck
	private Player player; // the player object (composition)
	private Dealer dealer; // the dealer object (composition)
	private Scanner in;
	private boolean initialized;
	private String name;//player's name
	private int deckIndex = 0;//index of shuffled deck
	private int playerIndex = 0;//player's card index
	private int dealerIndex = 0;//dealer's card index
	private boolean insuranceAccept;//check if player get insurance

	// constructor
	public BlackJackGame() {
		//initialize the 'deck of cards'
		cards = new ArrayList<>();
		initialized = false;
		insuranceAccept = false;
		deckIndex = 0;
		playerIndex = 0;
		dealerIndex = 0;
		name="";
	}

	// function for initializing the BlackJack game
	public void init() {
		
		// accept the player's name as string input from the user and
		// create a new player object, then assign it to the global player field.
		in = new Scanner(System.in);
		System.out.print("Enter player's name: ");
		name = in.nextLine();
		player = new Player(name, 10);
		System.out.println("* Player "+name+" has joined. Welcome.");
		// create a dealer object and assign to the global dealer field.
		dealer = new Dealer();
		// clear out the deck of cards
		cards.clear();
		// generate all 52 cards and populate the deck with them using the constant
		// ranks: Card.RANKS(13); and suits: Card.SUITS(4);
		for (int i = 0; i < 4; i++) {// suit
			for (int j = 0; j < 13; j++) {// rank
				Card card = new Card(j, i);
				cards.add(card);
			}
		}
		// shuffle the generated cards randomly.
		shuffled = new ArrayList<>();
		Random r = new Random();
		int size = cards.size();
		for (int i = 0; i < size; i++) {
			int rv = r.nextInt(cards.size());
			shuffled.add(cards.get(rv));
			cards.remove(rv);
		}
		System.out.println("* Cards shuffled by the dealer.");
		initialized = true;
	}

	// *function for running the game (whole logic of the game is here)
	public void run() {
		// check the initialization for failure tolerance
		if (!initialized) {
			System.out.println("[class BlackJackGame] Please call init() before calling run()!");
			return;
		}
		char choice = 0; // player's choice throughout the game
		int bet = 0; // player's bet
		
		// get the player's bet amount
		bet_loop: do {
			System.out.println("* Accepting bet.");
			System.out.println("1. Enter 'p' to print your current balance.");
			System.out.println("2. Enter 'b' [amount]' to bet some amount of money.");
			System.out.println("3. Enter 'q' to quit the game.");
			choice = in.next().charAt(0);

			switch (choice) {
			case 'p':
				player.printBalance();
				break;
			case 'b':
				bet = in.nextInt();
				if (player.hasAmountInBalance(bet))
					break bet_loop; // break the outer do-while loop
				else {
					System.out.println("Not enough money in balance.");
					bet = 0;
					break;
				}

			case 'q':
				System.out.println("Game aborted.");
				return;
			default:
				System.out.println("Press right buttons!");//exception case
			}
		} while (bet < 1); // loop while the amount is less than $1
		player.withdrawBetAmount(bet);
		System.out.println("* Bet accepted. Game started.");

		// game process
		game_loop: do {
			System.out.println("* Playing the game (" + name + "'s bet is $" + bet + ").");
			System.out.println("1. Enter /'p/' to print your cards/n");
			System.out.println("2. Enter /'d/' to double your bet amount/n");
			System.out.println("3. Enter /'h/' to hit (get another card from dealer)/n");
			System.out.println("4. Enter /'s/' to stand (do not take any cards and see the result)/n");
			System.out.println("5. Enter /'q/' to quit the game/n");
			// print out the available options for the user after
			// accepting the bet 
			choice = in.next().charAt(0);
			// get the user choice input
			switch (choice) {
			case 'p':
				player.printCards();
				break;
			case 'd':
				if (player.hasAmountInBalance(bet)) {
					System.out.println("* Bet doubled.");
					player.withdrawBetAmount(bet);
					bet = bet * 2;
				} else {
					System.out.println("Not enough money in balance. Do you want to ALLIN? [Y/N]");
					choice = in.next().charAt(0);
					if (choice == 'y' | choice == 'Y') {
						System.out.println("You Are SangNamJa! Ok, ALLIN. Good luck");
						player.withdrawBetAmount(10 - bet);// withdraw all balance
						bet = 10;
					} else {
						System.out.println("No Balls lol");
					}
				}
				break;
			case 'h':
				player.addCard(shuffled.get(deckIndex));
				System.out.println("* GOT: " + player.getCards()[playerIndex] + ".");
				deckIndex += 1;
				playerIndex += 1;
				while (calculateCardsSum(dealer.getCards()) < SOFT_17) {// if dealer's sum is less than comfortable
																		// score
					dealer.addCard(shuffled.get(deckIndex));
					deckIndex += 1;
					dealerIndex += 1;
					if (dealerIndex == 1 && calculateCardsSum(dealer.getCards()) == 11) {// if dealer's first card is
																							// ace
						System.out.println("Dealer's first card is ACE, Do you want to get insurance?[Y/N]");
						choice = in.next().charAt(0);
						if (choice == 'y' | choice == 'Y') {
							System.out.println("Insurance Accepted");
							if (player.hasAmountInBalance(bet / 2)) {
								player.withdrawBetAmount(bet / 2);// withdraw balance
								bet = bet + (bet / 2);
							} else {
								System.out.println(
										"Not enough money in balance. Automatically get into ALLIN state. Good luck");
								player.withdrawBetAmount(10 - bet);// withdraw all balance
								bet = 10;
							}
							insuranceAccept = true;
						} else {
							System.out.println("You must be SANGNAMJA!");
						}
					}
					if (dealerIndex == 2 && calculateCardsSum(dealer.getCards()) == 21 && insuranceAccept == true) {// if dealer's first card is ace
																													// and get blackjack
						System.out.println("Dealer got BlackJack! We return your bet to your balance. How clever!");
						player.deposit(bet);// return player's bet
						player.printBalance();
						return;
					}
				}
				System.out.println("Current sum of your cards is: " + calculateCardsSum(player.getCards()));
				break;
			case 's':
				break game_loop;
			case 'q':
				System.out.println("Player wants to quit game");
				destroy();
				return;
			}
			
		} while (choice != 'q');

		// calculate the game results
		int playerScore = calculateCardsSum(player.getCards());
		int dealerScore = calculateCardsSum(dealer.getCards());

		// print out the result of the game
		System.out.println("*~*~*~*~* GAME RESULTS *~*~*~*~*");
		player.printCards();
		System.out.printf("Score: %d%n", playerScore);
		dealer.printCards();
		System.out.printf("Score: %d%n", dealerScore);

		// proceed to the final result assessment logic
		if (playerScore > SCORE_BORDER) {
			// player got higher than 21
			System.out.printf("* Game over (Lost). %s's score exceeds %d.%n", player.getName(), SCORE_BORDER);
		} else if (playerScore < dealerScore && dealerScore <= SCORE_BORDER) {
			// player got lower than dealer, and dealer's score is valid
			System.out.printf("* Game over (Lost). Dealer's score is higher than %s's.%n", player.getName());
		} else if (playerScore == dealerScore) {
			// player's and dealer's scores are equal
			player.deposit(bet);
			System.out.printf("* Draw. %s's and the dealer's scores are equal.%n", player.getName());
		} else if (playerScore == SCORE_BORDER && player.getCards().length == 2) {
			// player got exactly 21 with two cards
			System.out.printf("* BlackJack, superb! %s has won $%d.%n", player.getName(), bet * 2.5);
			player.deposit((int) Math.ceil(bet * 2.5));
		} else {
			// regular win case when user is the winner
			System.out.printf("* Game over (Won). %s has won double of his bet ($%d).%n", player.getName(), bet * 2);
			player.deposit(bet * 2);
		}
		// print out the post-game balance of the player
		player.printBalance();
	
	}

	// function for destroying open streams and dumps if needed
	public void destroy() {
		if (!initialized) {
			System.out.println("[class BlackJackGame] Please call init() before calling destroy()!");
			return;
		}
		in.close();
	}

	// function for calculating the proper "BlackJack sum" of a collection of cards
	public int calculateCardsSum(Card[] cards) {
		int sum = 0;
		//calculate the sum of the cards

		for (int i = 0; i < cards.length; i++) {
			sum += cards[i].getValue();
			if ((sum > 21) && (cards[i].getValue() == 11)) {// if sum exceed 21 and added card is ace
				sum -= 10;// add 1 instead of add 11
			}
		}
		
		return sum;
	}
}

//*
//Richard Liu
//BlackJack 
//December 11 2018
//*

import java.util.*;
import java.io.*;

public class blackjack {
	static Scanner sc = new Scanner(System.in);
	public static void main(String[] args)throws IOException, InterruptedException {
		//variables
		int playerpoints = 0;
		int dealerpoints = 0;
		String name;
		String userin;
		String playercard = "";
		int sum = 0;
		System.out.println("Enter your name: ");
		name = sc.nextLine();
		Player player1 = new Player(name);
		Dealer dealer = new Dealer("Dealer");
		Deck deck = new Deck();
		deck.printer();
		System.out.println("\n");
		System.out.println("Shuffled deck: ");
		deck.shuffle();
		deck.printer();
		System.out.println();
		System.out.println("Would you like to play? (y/n)");
		userin = sc.nextLine();
		if (userin.equals("n")){
			//exit if they don't want to play
			System.out.println(":(");
			System.exit(0);
		}
		boolean play = true;
		System.out.println("Welcome to BlackJack!! ");
		while (play){
			//while they want to keep playing
			System.out.println("--------------------------------------------\nDeck: ");
			deck.printer();
			System.out.println();
			System.out.println("Your points: "+playerpoints+"\nDealers points: "+dealerpoints);
			System.out.println();
			//each player takes 2 cards 
			String y = deck.giveacard();
			player1.takeacard(y);
			y = deck.giveacard();
			player1.takeacard(y);
			player1.showhand();
			sum = player1.calculatetotal();
			System.out.println("Cards value: "+sum);
			y = deck.giveacard();
			dealer.takeacard(y);
			y = deck.giveacard();
			dealer.takeacard(y);
			dealer.hidehand();
			System.out.println("Cards value: ?");
			//boolean for if plyaer hits or stands
			boolean HoS;
			while (deck.cardsleft >= 5){
				//deck has more than 5 cards
				if (player1.hitorstand(sc)){
					//if player hits
					HoS = true;
					System.out.println("-----------------------------------------\nYou Hit!!");
					y = deck.giveacard();
					player1.takeacard(y);
					player1.showhand();
					//display hand and calculate total for plyaer
					sum = player1.calculatetotal();
					System.out.println("Cards value: "+sum);
					if (player1.bust(sum)){
						//if player busts
						System.out.println("You BUST!\nDEALER WINS!!");
						dealer.showhand();
						sum = dealer.calculatetotal();
						System.out.println("Cards Value: "+sum);
						//dealer gets a point
						dealerpoints++;
						break;
					}
					System.out.println();
					if (dealer.dhitorstand(dealer.calculatetotal()) && deck.cardsleft >= 5){
						//if dealer hits
						System.out.println("Dealer hits!");
						y = deck.giveacard();
						dealer.takeacard(y);
						dealer.hidehand();
						System.out.println("Cards value: ?");
						sum = dealer.calculatetotal();
						if (dealer.bust(sum)){
							//if dealer busts
							System.out.println("Dealer BUSTS!!\nYOU WIN!!");
							dealer.showhand();
							System.out.println("Cards Value: "+sum);
							playerpoints++;
							break;
						}
						System.out.println();
					}else if(deck.cardsleft >= 5){
						//if dealer stands
						System.out.println("Dealer Stands! ");
						dealer.hidehand();
						System.out.println("Cards value: ?");
						System.out.println();
					}
				}else{
					//if player stands
					HoS = false;
					System.out.println("----------------------------------------------\nYou Stand!!");
					if (dealer.dhitorstand(dealer.calculatetotal()) && deck.cardsleft >= 5){
						//if dealer hits while player stands
						System.out.println("Dealer hits!");
						y = deck.giveacard();
						dealer.takeacard(y);
						dealer.hidehand();
						System.out.println("Cards value: ?");
						sum = dealer.calculatetotal();
						if (dealer.bust(sum)){
							//if dealer busts
							System.out.println("Dealer BUSTS!!\nYOU WIN!!");
							dealer.showhand();
							System.out.println("Cards Value: "+sum);
							playerpoints++;
							break;
						}
						System.out.println();
					}else if(deck.cardsleft >= 5){
						//if dealer stands
						System.out.println("Dealer Stands! ");
						dealer.hidehand();
						System.out.println("Cards value: ?");
						System.out.println();
					}
				}
				if (!HoS && !dealer.dhitorstand(dealer.calculatetotal())){
					//when both players stand
					System.out.println("Both players STAND!! ");
					dealer.showhand();
					sum = dealer.calculatetotal();
					System.out.println("Card value: "+sum);
					System.out.println();
					player1.showhand();
					sum = player1.calculatetotal();
					System.out.println("Card value: "+sum);
					System.out.println();
					//display who the winner is or if there is a tie
					if (dealer.calculatetotal() > player1.calculatetotal()){
						System.out.println("Dealer Wins!");
						dealerpoints++;
					}else if(dealer.calculatetotal()<player1.calculatetotal()){
						System.out.println("You Win!!! ");
						playerpoints++;
					}else{
						System.out.println("It is a Tie!!");
					}
					break;
				}
			}
			if (deck.cardsleft < 5){
				//when there is less than 5 cards left in the deck
				System.out.println("There are less than 5 cards in the deck!\nTime to get a new one. \n");
				deck.dreset();
				deck.shuffle();
				System.out.println("Shuffled New Deck: ");
				deck.printer();
				System.out.println();
			}
			System.out.println("Would you like to play again? (y/n)");
			userin = sc.nextLine();
			if (userin.equals("y")){
				player1.reset();
				dealer.reset();
				clear();
			}else if (userin.equals("n")){
				System.out.println("Thank you for playing! ");
				break;
			}else{
				player1.reset();
				dealer.reset();
				clear();
			}
		}
	}

	public static void clear()throws IOException, InterruptedException{
		//clear screen
		 new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
	}
}

class Player{
	String name;
	int numofcards;
	//arrayt list for player hand
	ArrayList <String> hands;
	public Player (String n){
		name = n;
		numofcards = 0;
		hands = new ArrayList<String>();
	}
	public void takeacard(String y){
		numofcards++;
		hands.add(y);
	}
	public void showhand(){
		System.out.println(name+"\'s Cards: ");
		//show hand for player, and dealer if round is over
		for (int i = 0; i < hands.size(); i++){
			System.out.print(hands.get(i)+" ");
		}
		System.out.println();
	}
	public boolean hitorstand (Scanner sc){
		//boolean which returns true if player hits
		String userin = "";
		System.out.println("Hit or Stand? ");
		userin = sc.nextLine();
		System.out.println();
		if (userin.equals("hit")){
			return true;
		}else{
			return false;
		}
	}
	public int calculatetotal (){
		//calculate total points of dealer/player
		int points = 0;
		for (int i = 0; i < hands.size(); i++){
			Deck d = new Deck();
			points += d.cardToValue(hands.get(i));
		}
		return points;
	}
	public boolean bust(int sum){
		//to check bust
		if (sum > 21){
			return true;
		}else{
			return false;
		}
	}
	public void reset (){
		int x = hands.size();
		for (int i = x-1; i >= 0; i--){
			hands.remove(i);
		}
		numofcards = 0;
	}
 }

class Dealer extends Player{
	//dealer subclass
	String name;
	public Dealer(String s){
		super(s);
		name = s;
	}
	public void hidehand(){
		//hide the dealers hand unless round is over
		System.out.println(name+"\'s Cards: ");
		System.out.print(hands.get(0)+" ");
		//show first card
		for (int i = 1; i < hands.size(); i++){
			System.out.print("?"+" ");
		}
		System.out.println();
	}
	public boolean dhitorstand(int sum){
		//if dealers card value is less than 16 then dealer hits
		boolean hit;
		if (sum <= 16){
			hit = true;
		}else{
			hit = false;
		}
		return hit;
	}
}


class Deck{
	int cards [];
	int cardsleft;
	public Deck(){
		cardsleft = 52;
		cards = new int [cardsleft];
		for (int i = 0; i < cardsleft; i++){
			cards[i] = i;
		}
	}
	public String giveacard (){
		cardsleft--;
		return cardToString(cards[cardsleft]);
	}
  
	public static String cardToString(int numInList){
		//COPIED FROM WEBSITE
		//convert card number to string
		int cardType = numInList%13;
		char cardNum;
		String cardIs = "";
		if (cardType == 0){
			cardNum = 'K';
		}else if (cardType==1){
			cardNum='A';
		}else if (cardType==11){
			cardNum = 'J';
		}else if (cardType==12){
			cardNum='Q';
		}else {
			cardNum = (char)(cardType+'0');
		}
		//get card suit
		char cardSuit = (char)(numInList/13 +3);
		if (cardType!=10){
			cardIs=""+cardNum+cardSuit;
		}else{
			cardIs="10"+cardSuit;
		}
		return cardIs;
	}
  
	public static int cardToValue(String numOfList){
		int cardValue = 0;
		//if card is 10 set value to 10 because 10 is the only number with more than 1 character
		if (numOfList.indexOf("10")!= -1){
			cardValue = 10;
		}
		//if not 10, take away the suit temporarily
		numOfList = numOfList.substring(0,1);
		//card value is 10 if J, Q, or K
		if (numOfList.equals("J") || numOfList.equals("Q") || numOfList.equals("K")){
			cardValue = 10;
		}else if(numOfList.equals("A")){
			//if card is A value is 1
			cardValue = 1;
		}else if (Integer.parseInt(numOfList)!=1){
			//else, card number is card value
			cardValue = Integer.parseInt(numOfList)%13;
		}
		return cardValue;
	}
  

	public void shuffle(){
		int p = 0;
		int o = 0;
		//generate random numbers 50 times and switch their places in the array
		for (int i = 0; i < 50; i++){
			p = (int)(Math.random()*52);
			o = (int)(Math.random()*52);
			int temp = 0;
			temp = cards[p];
			cards[p] = cards[o];
			cards[o] = temp;
		}
	}
	public void printer (){
		//print deck
		for (int i =0; i < cardsleft; i++){
			System.out.print(cardToString(cards[i])+" ");
		}
		System.out.println();
	}
	public void dreset(){
		//deck reset for when there are less than 5 cards in the deck
		cardsleft = 52;
		for (int i = 0; i < cardsleft; i++){
			cards[i] = i;
		}
	}
}

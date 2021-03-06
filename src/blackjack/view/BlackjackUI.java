package blackjack.view;

import blackjack.BalanceCalculator;
import blackjack.model.Card;
import blackjack.model.Deck;
import blackjack.HandValueCalculator;
import blackjack.model.StandardPlayer;
import blackjack.model.Value;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * @author Amanda Jose, 2021
 * @author Vigneshwar Premachandran, 2021
 * @author Dhruv Patel, 2021
 * @author Zain Qureshi, 2021 Project: Deliverable 3 2021-04-18
 */
public class BlackjackUI {

    Scanner input;
    private static boolean endRoundForDealer = false;
    private static boolean dealerNaturalWin = false;
    HandValueCalculator valueCalculator = HandValueCalculator.getInstance();

    public BlackjackUI() {
        input = new Scanner(System.in);
    }

    public void startGame(ArrayList<StandardPlayer> players, Deck playingDeck, Deck dealerCards, ArrayList<Deck> playersDeck) {
        for (StandardPlayer player : players) {

            Deck playerCards = new Deck();
            System.out.println("\nDealing to " + player.getName() + "\n");

            //Giving player two cards
            playerCards.draw(playingDeck);
            playerCards.draw(playingDeck);

            //Checking to see if player has a natural win
            if (valueCalculator.cardsValue(playerCards.getCards()) == 21) {
                player.setNaturalWin(true);
                System.out.println();

                System.out.println(playerCards.toString());

                System.out.println();
                System.out.println("You Have A Natural Win!!!!");
                continue;
            }

            //while loop for hit or stand phase
            while (true) {
                //Show Player Cards
                System.out.println(player.getName() + " Your Hand: " + playerCards.toString());
                System.out.println();
                //Check for Joker in hand
                checkForJoker(playerCards);
                //getting User hand values
                System.out.println(player.getName() + ": The Value of Your Cards is: " + valueCalculator.cardsValue(playerCards.getCards()));
                System.out.println();

                //showing one of dealers cards
                System.out.println("Dealer's Hand: " + dealerCards.getCard(0).toString() + " and [Hidden]");
                System.out.println();

                //hit or stand phase
                System.out.println();
                //check if player busted if they got a joker
                if (valueCalculator.cardsValue(playerCards.getCards()) > 21) {
                    System.out.println();
                    System.out.println(player.getName() + " You Busted. Your Value is " + valueCalculator.cardsValue(playerCards.getCards()));
                    player.setEndRound(true);
                    playersDeck.add(playerCards);
                    break;
                } else {
                    System.out.println("Would you like to Hit or Stand?.....1 for Hit.....2 for Stand");

                    int userAnswer = input.nextInt();

                    //if they choose to hit(1)
                    if (userAnswer == 1) {

                        playerCards.draw(playingDeck);

                        System.out.println(player.getName() + " You drew a: " + playerCards.getCard(playerCards.deckSize() - 1).toString());
                        System.out.println();
                        checkForJoker(playerCards);
                        //Checking to see if value is now 21 after hitting
                        //if so then Player Busts
                        if (valueCalculator.cardsValue(playerCards.getCards()) > 21) {
                            System.out.println();
                            System.out.println(player.getName() + " You Busted. Your Value is " + valueCalculator.cardsValue(playerCards.getCards()));
                            player.setEndRound(true);
                            playersDeck.add(playerCards);
//                        playersDeck.trimToSize();
                            break;
                        }
                    }
                    //If they Choose Stand(2)
                    if (userAnswer == 2) {
                        playersDeck.add(playerCards);
//                    playersDeck.trimToSize();
                        break;
                    }
                }
            }
        }
//        playersDeck.trimToSize();

    }

    public void checkForJoker(Deck playerCard) {
        //if player has joker in hand
        valueCalculator.cardsValue(playerCard.getCards());
        for (Card jokerValue : playerCard.getCards()) {
            if (jokerValue.getValue() == Value.JOKER) {
                System.out.print(jokerValue.toString() + ": You Have A Joker In Your Hand That Has A Value Of: ");
                System.out.println(valueCalculator.getValOfJoker());
            }

        }
    }

    public void showDealersCards(Deck dealerCards) {
        System.out.println("\nDealer Cards: " + dealerCards.toString());
    }

    public void DealersTurnToHit(Deck dealerCards, Deck playingDeck) {

        //Checking Dealers hand for Natural Win
        if (valueCalculator.cardsValue(dealerCards.getCards()) == 21) {
            dealerNaturalWin = true;
        } else {
            //if dealer has joker end turn
            if ((valueCalculator.cardsValue(dealerCards.getCards()) > 21)) {
                endRoundForDealer = true;
            }
            //Dealer Keeps hitting until value above 16 and stands after that
            while ((valueCalculator.cardsValue(dealerCards.getCards()) < 17) && endRoundForDealer == false) {
                dealerCards.draw(playingDeck);
                System.out.println();
                System.out.println("Dealer Hits and Gets: " + dealerCards.getCard(dealerCards.deckSize() - 1).toString());
                System.out.println();
                dealerCards.toString();
                if ((valueCalculator.cardsValue(dealerCards.getCards()) > 21)) {
                    endRoundForDealer = true;
                }
            }
        }
    }

    public void showDealersHandValue(Deck dealerCards) {

        System.out.println("Dealer's Hand Value is " + (valueCalculator.cardsValue(dealerCards.getCards())));

    }

    public void isDealerBusted() {

        //Check if dealer busted
        if (endRoundForDealer) {
            System.out.println("\nDealer Has Busted, Moving on to Checking For Winner.\n");

        } else {

            System.out.println("\nDealer Has not Busted, Moving on to Checking For Winner.\n");
            System.out.println();
        }
    }

    public void tieChecker(Deck dealerCards, ArrayList<Deck> playersDeck, ArrayList<StandardPlayer> players) {

        for (int i = 0; i < players.size() - 1; i++) {

            if ((valueCalculator.cardsValue(playersDeck.get(i).getCards()) == valueCalculator.cardsValue(dealerCards.getCards()))) {

                players.get(i).setTie(true);

            } else {

                players.get(i).setTie(false);

            }
        }

    }

    public void winChecker(Deck dealerCards, ArrayList<Deck> playersDeck, ArrayList<StandardPlayer> players) {

        //checking for tie
        for (int i = 0; i < players.size(); i++) {
            System.out.println("\n------------------------------\n");

            System.out.println("Checking Player " + (i + 1) + "'s Cards\n");
            System.out.println("This Is The Value Of Your Hand: " + (valueCalculator.cardsValue(playersDeck.get(i).getCards())));
            System.out.println("This Is The Dealer's Hand Value: " + valueCalculator.cardsValue(dealerCards.getCards()));

            //checking if player busted
            if (players.get(i).isEndRound()) {

                playerBusts(players.get(i), playersDeck.get(i));

            } else if ((players.get(i).isTie()) && !(endRoundForDealer)) {

                if ((players.get(i).isNaturalWin()) && (dealerNaturalWin)) {
                    System.out.println("TIE!!!!!!");
                    System.out.println("Natural Win Tie between the Dealer and " + players.get(i).getName());
                    System.out.println();
                } else {
                    System.out.println("TIE!!!!!!");
                    System.out.println("Tie between the Dealer and " + players.get(i).getName());
                    System.out.println();
                }
                //checking for natural win

            } else if (players.get(i).isNaturalWin()) {
                BalanceCalculator calculateNaturalWin = new BalanceCalculator();

                players.get(i).setWallet(players.get(i).getWallet() + calculateNaturalWin.calculateNaturalWinBalance(players.get(i)));

                System.out.println(players.get(i).getName() + " You Won With a Natural Hand!!");

                //Players beats the dealer
            } else if (!(endRoundForDealer) && !((valueCalculator.cardsValue(playersDeck.get(i).getCards())) < valueCalculator.cardsValue(dealerCards.getCards())) && !(players.get(i).isEndRound())) {
                System.out.println(players.get(i).getName());
                System.out.println();

                //if player wins
                playerWins(players.get(i));

//                if (i == players.size()) {
//                    endRoundForDealer = true;
//                }
//                if dealer busts
            } else if ((endRoundForDealer)) {
                System.out.println();

                //if dealer busts and player does as well
                if ((endRoundForDealer) && (players.get(i).isEndRound())) {
                    dealerAndPlayerBusts(players.get(i));
                } else {
                    System.out.println("Dealer Busts. You Win " + players.get(i).getName() + "!!!");

                    BalanceCalculator calculateWin = new BalanceCalculator();
                    players.get(i).setWallet(players.get(i).getWallet() + calculateWin.calculateBalance(players.get(i)));
                }

                //dealer wins
            } else if (!(endRoundForDealer) && ((valueCalculator.cardsValue(playersDeck.get(i).getCards())) < valueCalculator.cardsValue(dealerCards.getCards()))) {

                dealerWins(players.get(i), playersDeck.get(i), dealerCards);
            }
            System.out.println("Done Checking Player " + (i + 1) + "'s Cards. Next Player");
            System.out.println("\n------------------------------\n");
        }
    }

    public void playerBusts(StandardPlayer player, Deck playersDeck) {
        System.out.println();
        System.out.println(player.getName() + " You  Busted, Say Bye To Your Money!! :/)/ ");
        player.setWallet(player.getWallet() - player.getCurrentBet());

    }

    public void dealerAndPlayerBusts(StandardPlayer player) {
        System.out.println(player.getName() + " You Also Busted Like the Dealer ");
        player.setWallet(player.getWallet() - player.getCurrentBet());

    }

    public void playerWins(StandardPlayer player) {
        //if won by having higher value than dealer and not  busting
        System.out.println();
        System.out.println(player.getName() + " You Win this Hand.");
        BalanceCalculator calculateWin = new BalanceCalculator();
        player.setWallet(player.getWallet() + calculateWin.calculateBalance(player));
        //another way of adding money to wallet is
        //players.get(i).setWaller(players.get(i).getWaller() + players.get(i).getCurrentBet());

        //Once last players hand gets checked change value to true
    }

    public void dealerWins(StandardPlayer player, Deck playersDeck, Deck dealerCards) {
        if (dealerNaturalWin == true) {
            System.out.println();
            System.out.println("Dealer Wins with a Natural Win!!!! against " + player.getName());
            player.setWallet(player.getWallet() - player.getCurrentBet());
        } else {
            System.out.println();
            System.out.println("Dealer Wins!!!! against " + player.getName());
            player.setWallet(player.getWallet() - player.getCurrentBet());
        }
    }

    public void removePlayer(ArrayList<StandardPlayer> players) {

        for (int i = 0; i < players.size(); i++) {
            if (players.get(i).getWallet() == 0) {
                System.out.println(players.get(i).getName() + " You Ran Out Of Money!!!!!!");
                System.out.println(players.get(i).getName() + " You LOST!!!!!, You will be removed from the game.");
                players.remove(i);
            }
        }
    }

    public void moneyUpdate(ArrayList<StandardPlayer> players) {
        for (StandardPlayer player : players) {
            System.out.println("\nNew Balance for " + player.getName() + " is ");
            System.out.println("$" + player.getWallet() + "\n\n");
        }
    }

    public boolean newRound(Deck playingDeck) {
        System.out.println("End of Round");
        //Creating new Deck once the main deck is done
        if (playingDeck.deckSize() == 0) {
            playingDeck.createDeck();
            playingDeck.shuffle();
        }
        System.out.println("Play Again? Enter \"Yes\" or \"No\"");
        String userChoice = input.next();
        return userChoice.equals("yes");
    }
}

package blackjack;

/**
 *
 * @author dhruvpatel
 * @author Amanda Jose, 2021
 * @author Vigneshwar Premachandran, 2021
 */
import blackjack.view.GameManagerUI;
import blackjack.model.StandardPlayer;
import blackjack.model.Deck;
import blackjack.view.BlackjackUI;
import java.util.Scanner;
import java.util.ArrayList;

public class BlackJack {

    public static ArrayList<StandardPlayer> players = new ArrayList();

    public static void main(String[] args) {

        BlackjackUI blackjackView = new BlackjackUI();

        blackjackView.startMessage();

        //------------GameManager Controller Code---------------
        
        boolean stillJoining = true;
        GameManagerUI gm = new GameManagerUI();

        //Tutorial
        gm.tutorial();
        
        //Game Menu
        do {
            
            String command = gm.mainMenu(players);
            if (command.equalsIgnoreCase("")) {

                players.add(gm.getPlayerRegistration());

            } else if (command.equalsIgnoreCase("Q")) {

                gm.quitGame(players);

            } else if (gm.doesPlayerExist(players, command)) {

                players.remove(gm.getPlayerObject(players, command));

            } else if (command.equalsIgnoreCase("start")) {
                stillJoining = false;
            }
            
            if(players.size() >= 4){
                stillJoining = false;
            }
        } while (stillJoining == true);

        gm.showAllPlayerBalance(players);

        //Creating Main Deck
        Deck playingDeck = new Deck();
        //Adding 52 ordered cards to the deck
        playingDeck.createDeck();
        //shuffling the Deck
        playingDeck.shuffle();

        //Main Game Loop
        do {

            gm.placingBets(players);

            //dealercards will be created first
            Deck dealerCards = new Deck();
            //Giving two cards to dealer
            dealerCards.draw(playingDeck);
            dealerCards.draw(playingDeck);

            //ArrayList to hold the hands of the players
            ArrayList<Deck> playersDeck = new ArrayList();

            blackjackView.startGame(players, playingDeck, dealerCards, playersDeck);

            blackjackView.showDealersCards(dealerCards);

            blackjackView.didDealerWin(playingDeck, dealerCards, playersDeck);

            blackjackView.showDealersHandValue(dealerCards);

            blackjackView.isBusted(dealerCards, playersDeck);

            blackjackView.tieChecker(dealerCards, playersDeck, players);

            blackjackView.winChecker(dealerCards, playersDeck);

            blackjackView.moneyUpdate(players);

        } while (blackjackView.newRound(playingDeck));

    }
}

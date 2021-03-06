
package blackjack;

import blackjack.model.StandardPlayer;

/**
 * @author Amanda Jose, 2021
 * @author Vigneshwar Premachandran, 2021
 * @author Dhruv Patel, 2021
 * @author Zain Qureshi, 2021
 * Project: Deliverable 3
 * 2021-04-18
 */
public class BalanceCalculator {

    public final double MULTIPLIER = 1.5;
    public final double NATURALWIN = 2;
    public double winnings;
    public double naturalWinnings;

    public double calculateBalance(StandardPlayer player) {
        winnings += player.getCurrentBet() * MULTIPLIER;
        return winnings;
    }

    public double calculateNaturalWinBalance(StandardPlayer player) {
        naturalWinnings += player.getCurrentBet() * NATURALWIN;
        return naturalWinnings;
    }
}

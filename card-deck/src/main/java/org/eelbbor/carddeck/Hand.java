package org.eelbbor.carddeck;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Robb Lee (robbmlee@gmail.com).
 */
public class Hand {
    private List<Card> cards;

    public Hand() {
        this.cards = new ArrayList<>();
    }

    public void dealCard(Card... card) {
        cards.addAll(Arrays.asList(card));
    }

    public boolean playCard(Card card) {
        return cards.remove(card);
    }
}

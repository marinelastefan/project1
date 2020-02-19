package com.tema1.players;
import java.util.Comparator;
public final class PlayerComparator implements Comparator<Players> {
        @Override
        public int compare(final Players p1, final Players p2) {
            return p2.getCoins() - p1.getCoins();
        }
}



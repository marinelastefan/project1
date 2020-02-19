package com.tema1.players;

import com.tema1.common.Constants;
import com.tema1.goods.GoodsFactory;
import java.util.ArrayList;
import java.util.Collections;


public class BribedPlayer extends BasicPlayer {
    public BribedPlayer(final int id, final PlayerStrategy strategy, final int coins) {
        super(id, strategy, coins);

    }
   public final ArrayList<Integer> createBribeBag(final ArrayList<Integer> cards) {
       GoodsFactory allgoods = GoodsFactory.getInstance();
       ArrayList<Integer> goodsInBag = new ArrayList<Integer>();
       //daca nu are bunuri ilegale sau nu are bani joaca strategia de baza
        boolean illegal = false;
        for (int i = 0; i < cards.size(); i++) {
            if (cards.get(i) > Constants.ten) {
                illegal = true;
                break;
            }
        }
        if (!illegal || getCoins() <= Constants.five) {
            return super.createTheBag(cards);
        }  else {
            ProfitComparator comparator = new ProfitComparator();
            //sortez bunurile in functie de profit
            Collections.sort(cards, comparator);
            int illegalCount = 0;
            int cardsCount = 0;
            int penaltyCount = getCoins();
            int i = 0;
           // adaug carti doar daca penalty-ul acestora nu ma duce pe 0
            while (cardsCount < Constants.numberOfCardsInBag) {
                if ((penaltyCount - allgoods.getGoodsById(cards.get(i)).getPenalty()) > 0) {
                    goodsInBag.add(cards.get(i));
                    penaltyCount -= allgoods.getGoodsById(cards.get(i)).getPenalty();
                    if (cards.get(i) > Constants.ten) {
                        illegalCount++;
                    }
                }
               cardsCount++;
                i++;

            }
            //setez mita in functie de numarul de carti ilegale
           if (illegalCount == Constants.one || illegalCount == Constants.two) {
               setBribe(Constants.five);
           } else {
               if (illegalCount > Constants.two) {
                   setBribe(Constants.ten);
               }
           }
           setHonesty(PlayerHonesty.Liar);
           setGoodDeclaredType(0);
           return goodsInBag;
        }
   }
   public final void bribeSheriff(final Players trader, final int round,
                                  final int numberOfPlayers) {
       int coinsLiar = 0;
       int coinsHonest = 0;
       final int playerposition = trader.getId();
       final int bribeposition = this.getId();
       GoodsFactory allgoods = GoodsFactory.getInstance();
       ArrayList<Integer> goodsInHand = new ArrayList<Integer>();
       //creez sacul pentru fiecare jucator
       if (trader.getStrategy().equals(PlayerStrategy.basic)) {
           goodsInHand = createTheBag(trader.getCards());
       }
       if (trader.getStrategy().equals(PlayerStrategy.greedy)) {
           goodsInHand = ((GreedyPlayer) trader).createBag(trader.getCards(), round);
       }
       if (trader.getStrategy().equals(PlayerStrategy.bribed)) {
           goodsInHand = ((BribedPlayer) trader).createBribeBag(trader.getCards());
       }
       for (int i = 0; i < goodsInHand.size(); i++) {
           if (goodsInHand.get(i) != trader.getGoodDeclaredType()) {
               coinsLiar += allgoods.getGoodsById(goodsInHand.get(i)).getPenalty();
           }
           coinsHonest += allgoods.getGoodsById(goodsInHand.get(i)).getPenalty();
       }
       //va controla daca are minim 15 bani
       if (this.getCoins() > Constants.minimumCoins) {
           //controleaza doar stanga si dreapta
           if ((bribeposition == playerposition - 1)
                   || (bribeposition == playerposition + 1)
                   || (bribeposition == (numberOfPlayers - 1) && playerposition == 0)
                   || (bribeposition == 0 && playerposition == (numberOfPlayers - 1))) {
               //daca jucatorul a fost cinstit isi pune bunurile pe taraba
               if (trader.getHonesty().equals(PlayerHonesty.Honest)) {
                   for (int i = 0; i < goodsInHand.size(); i++) {
                       trader.getGoodsOnStand().add(goodsInHand.get(i));
                   }
                   trader.addCoins(coinsHonest);
                   this.payPenalty(coinsHonest);
               } else {
                   //isi pune doar bunurile declarate pe taraba
                   for (int i = 0; i < goodsInHand.size(); i++) {
                       if (goodsInHand.get(i).equals(trader.getGoodDeclaredType())) {
                           trader.getGoodsOnStand().add(goodsInHand.get(i));
                       }
                   }
                   trader.payPenalty(coinsLiar);
                   this.addCoins(coinsLiar);
               }
           } else {
               //de la ceilalti va lua doar mita, daca exista
               addCoins(trader.getBribe());
               trader.payPenalty(trader.getBribe());
               //ei isi pot pune bunurile pe taraba
               for (int i = 0; i < goodsInHand.size(); i++) {
                   trader.getGoodsOnStand().add(goodsInHand.get(i));
               }
           }
       } else {
           //jucatorii trec fara sa fie controlati
           for (int i = 0; i < goodsInHand.size(); i++) {
               trader.getGoodsOnStand().add(goodsInHand.get(i));
           }
       }
    }
}

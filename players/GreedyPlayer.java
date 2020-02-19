package com.tema1.players;

import com.tema1.common.Constants;
import java.util.ArrayList;
import java.util.Collections;

public class GreedyPlayer extends BasicPlayer {
    public GreedyPlayer(final int id, final PlayerStrategy strategy, final  int coins) {
        super(id, strategy, coins);
    }
    public final ArrayList<Integer> createBag(final ArrayList<Integer> cards, final int round) {
         ArrayList<Integer> goodsInHand = new ArrayList<Integer>();
         //in rundele impare joaca strategia de baza
         if (round % 2 != 0) {
             goodsInHand = createTheBag(cards);
         } else {
             goodsInHand = createTheBag(cards);
             //daca are mai putin de 8 carti in mana va  pune un bun ilegal
             if (goodsInHand.size() < Constants.eight) {
                 ProfitComparator comparator = new ProfitComparator();
                 //sortez bunurile in functie de profit
                 Collections.sort(cards, comparator);
                 //daca la strategia de baza a aduaugat deja un bun ilegal
                 //il adauga pe urmatorul
                 if (goodsInHand.get(0).equals(cards.get(0))) {
                     if (cards.get(1) > Constants.ten) {
                         goodsInHand.add(cards.get(1));
                     }
                 } else {
                     //daca are doar bunuri legale, il adauga pe cel ilegal cu profitul maxim
                     if (cards.get(0) > Constants.ten) {
                         goodsInHand.add(cards.get(0));
                         setHonesty(PlayerHonesty.Liar);
                     }
                 }
             }
             //nu adauga mita
             setBribe(Constants.noBribe);
         }
         return goodsInHand;
     }
     public final void sherrif(final Players trader, final  int round) {
        //daca nu i se ofera mita, va controla comercianrul
         if (trader.getBribe() == 0) {
             super.sheriffTime(trader, round);
         } else {
             //in caz contrar va lua mita si jucatorul isi poate adauga bunurile pe taraba
             ArrayList<Integer> goodsInHand = new ArrayList<Integer>();
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
                 trader.getGoodsOnStand().add(goodsInHand.get(i));
             }
             this.addCoins(trader.getBribe());
             trader.payPenalty(trader.getBribe());
         }
     }



}

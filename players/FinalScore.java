package com.tema1.players;

import com.tema1.goods.Goods;
import com.tema1.goods.GoodsFactory;
import  com.tema1.goods.IllegalGoods;
import  com.tema1.goods.LegalGoods;
import  com.tema1.goods.GoodsType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FinalScore {
    public final void addIllegalBonus(final ArrayList<Players> players) {
        int numberOfPlayers = players.size();
     ArrayList<Integer> cards;
     //pentru fiecare jucator verific daca are bunurile ilegale
     for (int i = 0; i < numberOfPlayers; i++) {
         cards = players.get(i).getGoodsOnStand();
         for (int j = 0; j < cards.size(); j++) {
             Goods good = GoodsFactory.getInstance().getGoodsById(cards.get(j));
             //daca are, adaug bunurile bonus pe taraba
             if (good.getType().equals(GoodsType.Illegal)) {
                 for (var bonus : ((IllegalGoods) good).getIllegalBonus().entrySet()) {
                     for (int k = 0; k < bonus.getValue(); k++) {
                         players.get(i).getGoodsOnStand().add(bonus.getKey().getId());
                     }
                 }
             }
         }
     }
    }
    public final void addKingQueen(final  ArrayList<Players> players) {
        Goods goods;
        final int ten = 10;
        Map<Integer, Goods> allgoods = GoodsFactory.getInstance().getAllGoods();
        int numberOfPlayers = players.size();
        List<Map<Integer, Integer>> arraymap = new ArrayList<Map<Integer, Integer>>();
        for (int i = 0; i < numberOfPlayers; i++) {
            arraymap.add(new HashMap<Integer, Integer>());
            for (Integer good : players.get(i).getGoodsOnStand()) {
                arraymap.get(i).put(good, arraymap.get(i).getOrDefault(good, 0) + 1);
            }
        }
        //pentru fiecare bun legal, verific care dintre jucatori are nr maxim
        //de bunuri legale de acel tip
        for (int i = 0; i < ten; i++) {
            int maxKing = 0;
            int maxQueen = 0;
            int positionK = 0;
            int positionQ = 0;
            //retin numarul maxim de bunuri si id-ul jucatorului care le detine
            for (int  k = 0; k < numberOfPlayers; k++) {
                for (Integer key : arraymap.get(k).keySet()) {
                    if (key.equals(i) && arraymap.get(k).get(key) > maxKing) {
                        maxKing = arraymap.get(k).get(key);
                        positionK = k;
                    }
                }
            }
            //in acelasi fel procedez si pentru regina
            for (int k = 0; k < numberOfPlayers; k++) {
                for (Integer key : arraymap.get(k).keySet()) {
                    if (key.equals(i)) {
                        if (arraymap.get(k).get(key) <= maxKing && k != positionK
                            && arraymap.get(k).get(key)  > maxQueen) {
                        maxQueen = arraymap.get(k).get(key);
                        positionQ = k;
                     }
                    }
                }
            }
            //adaug banutii jucatorilor care au obtinut King si Queen
            if (maxKing != 0) {
                goods = allgoods.get(i);
                LegalGoods good2 = (LegalGoods) goods;
                players.get(positionK).addCoins(good2.getKingBonus());
            }
            if (maxQueen != 0) {
                goods = allgoods.get(i);
                LegalGoods good2 = (LegalGoods) goods;
                players.get(positionQ).addCoins(good2.getQueenBonus());
            }
        }
    }
    //aduag profitul pentru fiecare bun de pe taraba
    public final Integer addProfit(final ArrayList<Integer> goodsOnStand) {
        GoodsFactory allgoods = GoodsFactory.getInstance();
        Integer finalProfit = 0;
            for (int j = 0; j < goodsOnStand.size(); j++) {
                finalProfit += allgoods.getGoodsById(goodsOnStand.get(j)).getProfit();
            }
        return  finalProfit;
    }
}

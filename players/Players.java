package com.tema1.players;
import com.tema1.goods.GoodsFactory;
import java.util.ArrayList;
import java.util.Collections;
import  java.util.Map;
import java.util.HashMap;

public  class Players {
    private int bribe;
    private PlayerType playerType;
    private int coins = 0;
    private  ArrayList<Integer> cards = new ArrayList<Integer>();
    private ArrayList<Integer> goodsOnStand = new ArrayList<>();
    private Integer goodDeclaredType;
    private PlayerHonesty honesty;
    private PlayerStrategy strategy;
    private int id;
    public Players() {

    }
    public Players(final  int id, final PlayerStrategy strategy, final int coins) {
        this.id = id;
        this.strategy = strategy;
        this.coins = coins;
    }
    public final  PlayerStrategy getStrategy() {
        return strategy;
    }

    public final ArrayList<Integer> getGoodsOnStand() {
        return goodsOnStand;
    }

    public Players(final int bribe, final PlayerType playerType, final int coins) {
        this.bribe = bribe;
        this.playerType = playerType;
        this.coins = coins;
    }
    public final void setHonesty(final PlayerHonesty honesty) {
        this.honesty = honesty;
    }

    public final int getBribe() {
        return bribe;
    }

    public final PlayerType getPlayerType() {
        return playerType;
    }

    public final ArrayList<Integer> getCards() {
        return cards;
    }

    public final int getCoins() {
        return coins;
    }
    public final void setCoins(final int coins) {
        this.coins = coins; }
    public final void addCoins(final int coinsG) {
        this.coins += coinsG; }
    public final void payPenalty(final int coinsG) {
        this.coins -= coinsG;
    }
    public final void setPlayerType(final PlayerType playerType) {
        this.playerType = playerType;
    }
    public final Integer getGoodDeclaredType() {
        return goodDeclaredType;
    }

    public final PlayerHonesty getHonesty() {
        return honesty;
    }

    public final void setBribe(final int bribe) {
        this.bribe = bribe;
    }

    public final int getId() {
        return id;
    }

    public final Integer frequencyCalculator(final  ArrayList<Integer> startCards) {
        //adaug toate bunurile legale intr un maplist
        final int nine = 9;
        ArrayList<Integer> cards1 = new ArrayList<Integer>();
        for (int i = 0; i < startCards.size(); i++) {
            if (startCards.get(i) <= nine) {
                cards1.add(startCards.get(i));
            }
        }
        //daca exista bunuri legale
        if (cards1.size() != 0) {
            Map<Integer, Integer> freqmap = new HashMap<Integer, Integer>();
            //calculez frecventa pentru fiecare bun
            for (Integer good : cards1) {
                freqmap.put(good, freqmap.getOrDefault(good, 0) + 1);
            }
            setHonesty(PlayerHonesty.Honest);
          return getTheMaxGood(freqmap);
        } else {
            ProfitComparator comparator = new ProfitComparator();
            //sortez bunurile in functie de profit
            Collections.sort(startCards, comparator);
            setHonesty(PlayerHonesty.Liar);
            //o returnez pe cea cu profitul maxim
            return  startCards.get(0);
        }
    }
   public final Integer getTheMaxGood(final Map<Integer, Integer> freqmap) {
        //caut bunurile cu frecventa cea mai mare
       Map<Integer, Integer> bestGoodsInHand = new HashMap<Integer, Integer>();
       int maxFreqInMap = (Collections.max(freqmap.values()));
       GoodsFactory allgoods = GoodsFactory.getInstance();
       for (Map.Entry<Integer, Integer> entry : freqmap.entrySet()) {
           if (entry.getValue() == maxFreqInMap) {
               bestGoodsInHand.put(entry.getKey(), entry.getValue());
           }
       }
       //caut bunurile cu profitul cel mai mare
       int maxProfitInMap = getTheMaxProfit(bestGoodsInHand);

       Map<Integer, Integer> maxProfitInHand = new HashMap<Integer, Integer>();
       for (Map.Entry<Integer, Integer> good : bestGoodsInHand.entrySet()) {
           if (allgoods.getGoodsById(good.getKey()).getProfit() == maxProfitInMap) {
               maxProfitInHand.put(good.getKey(), good.getValue());
           }

       }
       if (maxProfitInHand.size() == 1) {
           Map.Entry<Integer, Integer> good = maxProfitInHand.entrySet().iterator().next();
           Integer value  = good.getKey();
           return value;
       } else {
           //daca sunt mai multe bunuri cu acelasi profit returnez cel mai mare id
             return getTheMaxId(maxProfitInHand);
       }


    }
    public final Integer getTheMaxProfit(final Map<Integer, Integer> bestGoodsInHand) {
        int max = -1;
       GoodsFactory allgoods = GoodsFactory.getInstance();
        for (Map.Entry<Integer, Integer> good : bestGoodsInHand.entrySet()) {
            if (allgoods.getGoodsById(good.getKey()).getProfit() > max) {
                max = allgoods.getGoodsById(good.getKey()).getProfit();
            }
        }
        return  max;

    }
    public final Integer getTheMaxId(final Map<Integer, Integer> bestGoodsInHand) {
        int maxIdInMap = (Collections.max(bestGoodsInHand.keySet()));
        return maxIdInMap;

    }

    public final void setGoodDeclaredType(final Integer goodType) {
        this.goodDeclaredType = goodType;
    }
}















package com.tema1.players;
import com.tema1.common.Constants;
import com.tema1.goods.GoodsFactory;
import java.util.ArrayList;


public class BasicPlayer extends Players {
    public BasicPlayer() {

    }
    /*public BasicPlayer(int bribe,  PlayerType playerType,  int coins){
        super(bribe, playerType, coins);
    }*/

    public BasicPlayer(final int id, final PlayerStrategy strategy, final int coins) {
        super(id, strategy, coins);

    }

    //crearea sacului
    public final ArrayList<Integer> createTheBag(final ArrayList<Integer> cards) {
        Integer goodId = frequencyCalculator(cards);
        ArrayList<Integer> goodsInHand = new ArrayList<Integer>();
        final int ten = 10;
        //daca am doar cartile ilegale, adaug una
        if (goodId > ten) {
            goodsInHand.add(goodId);
            setHonesty(PlayerHonesty.Liar);
            setGoodDeclaredType(0);
        } else {
            //in caz contrar adaug doar carti legale de acelasi tip
            for (Integer card : cards) {
                if (card.equals(goodId)) {
                    goodsInHand.add(card);
                }
            }
            setHonesty(PlayerHonesty.Honest);
            setGoodDeclaredType(goodsInHand.get(0));
        }
        //pun bunurile in sac
        setBribe(0);
        setPlayerType(PlayerType.Trader);
        return goodsInHand;

    }
    public final void sheriffTime(final Players trader, final int round) {
        int coinsLiar = 0;
        int coinsHonest = 0;
        GoodsFactory allgoods = GoodsFactory.getInstance();
        ArrayList<Integer> goodsInHand = new ArrayList<Integer>();
        //creez sacul pentru fiecare player
        if (trader.getStrategy().equals(PlayerStrategy.basic)) {
            goodsInHand = createTheBag(trader.getCards());
        }
        if (trader.getStrategy().equals(PlayerStrategy.greedy)) {
            goodsInHand = ((GreedyPlayer) trader).createBag(trader.getCards(), round);
        }
        if (trader.getStrategy().equals(PlayerStrategy.bribed)) {
            goodsInHand = ((BribedPlayer) trader).createBribeBag(trader.getCards());
        }
        //in caz ca jucatorul a mintit va trebui sa plateasca penalty
        //pentru fiecare bun nedeclarat sau ilegal
        //altfel, seriful va trebui sa-i plateasca penalty pentru fiecare bun
        for (int i = 0; i < goodsInHand.size(); i++) {
            if (!(goodsInHand.get(i).equals(trader.getGoodDeclaredType()))) {
                coinsLiar += allgoods.getGoodsById(goodsInHand.get(i)).getPenalty();
            }
            coinsHonest += allgoods.getGoodsById(goodsInHand.get(i)).getPenalty();
        }
        //jucatorul basic va controla doar daca are minim 16 bani
        if (getCoins() >= Constants.minimumCoins) {
            if (trader.getHonesty().equals(PlayerHonesty.Honest)) {
                for (int i = 0; i < goodsInHand.size(); i++) {
                    trader.getGoodsOnStand().add(goodsInHand.get(i));
                }
                trader.addCoins(coinsHonest);
                this.payPenalty(coinsHonest);
            } else {
                for (int i = 0; i < goodsInHand.size(); i++) {
                    if (goodsInHand.get(i).equals(trader.getGoodDeclaredType())) {
                        trader.getGoodsOnStand().add(goodsInHand.get(i));
                    }
                }
                trader.payPenalty(coinsLiar);
                this.addCoins(coinsLiar);
            }
        } else {
            //altfel, jucatorii isi vor putea adauga bunurile pe taraba
            for (int i = 0; i < goodsInHand.size(); i++) {
                trader.getGoodsOnStand().add(goodsInHand.get(i));
            }
        }
    }

}

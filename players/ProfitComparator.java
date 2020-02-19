package com.tema1.players;
import com.tema1.goods.GoodsFactory;
import java.util.Comparator;
public final class ProfitComparator implements Comparator<Integer> {

    @Override
    public int compare(final Integer id1, final Integer id2) {
        if ((GoodsFactory.getInstance().getGoodsById(id2).getProfit()
                - GoodsFactory.getInstance().getGoodsById(id1).getProfit()) == 0) {
            return id2 - id1;
        }

        return GoodsFactory.getInstance().getGoodsById(id2).getProfit()
                - GoodsFactory.getInstance().getGoodsById(id1).getProfit();
    }


}

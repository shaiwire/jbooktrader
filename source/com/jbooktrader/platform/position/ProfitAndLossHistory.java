package com.jbooktrader.platform.position;

import java.util.*;

/**
 * Holds P&L history.
 */
public class ProfitAndLossHistory {
    private final List<ProfitAndLoss> history;

    public ProfitAndLossHistory() {
        history = new ArrayList<ProfitAndLoss>();
    }

    public void clear() {
        history.clear();
    }

    public void add(ProfitAndLoss profitAndLoss) {
        history.add(profitAndLoss);
    }

    public List<ProfitAndLoss> getHistory() {
        return history;
    }

}

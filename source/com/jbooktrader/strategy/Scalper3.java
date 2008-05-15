package com.jbooktrader.strategy;

import com.ib.client.*;
import com.jbooktrader.indicator.*;
import com.jbooktrader.platform.bar.*;
import com.jbooktrader.platform.commission.*;
import com.jbooktrader.platform.indicator.*;
import com.jbooktrader.platform.marketdepth.*;
import com.jbooktrader.platform.model.*;
import com.jbooktrader.platform.optimizer.*;
import com.jbooktrader.platform.schedule.*;
import com.jbooktrader.platform.strategy.*;
import com.jbooktrader.platform.util.*;

/**
 *
 */
public class Scalper3 extends Strategy {

    // Technical indicators
    private final Indicator lowDepthBalanceInd, highDepthBalanceInd;

    // Strategy parameters names
    private static final String ENTRY = "Entry";

    // Strategy parameters values
    private final int entry;


    public Scalper3(StrategyParams optimizationParams, MarketBook marketBook, PriceHistory priceHistory) throws JBookTraderException {
        super(optimizationParams, marketBook, priceHistory);

        // Specify the contract to trade
        Contract contract = ContractFactory.makeFutureContract("ES", "GLOBEX");
        int multiplier = 50;// contract multiplier

        // Define trading schedule
        TradingSchedule tradingSchedule = new TradingSchedule("9:20", "16:10", "America/New_York");

        Commission commission = CommissionFactory.getBundledNorthAmericaFutureCommission();
        setStrategy(contract, tradingSchedule, multiplier, commission);

        entry = getParam(ENTRY);

        // Create technical indicators
        lowDepthBalanceInd = new LowDepthBalance(marketBook);
        highDepthBalanceInd = new HighDepthBalance(marketBook);
        addIndicator("Low Depth Balance", lowDepthBalanceInd);
        addIndicator("High Depth Balance", highDepthBalanceInd);
    }

    /**
     * Adds parameters to strategy. Each parameter must have 5 values:
     * name: identifier
     * min, max, step: range for optimizer
     * value: used in backtesting and trading
     */
    @Override
    public void setParams() {
        addParam(ENTRY, 20, 50, 1, 48);
    }

    /**
     * This method is invoked by the framework when an order book changes and the technical
     * indicators are recalculated. This is where the strategy itself should be defined.
     */
    @Override
    public void onBookChange() {
        double lowDepthBalance = lowDepthBalanceInd.getValue();
        double highDepthBalance = highDepthBalanceInd.getValue();
        if (highDepthBalance >= entry && lowDepthBalance > -entry) {
            setPosition(1);
        } else if (lowDepthBalance <= -entry && highDepthBalance < entry) {
            setPosition(-1);
        }
    }
}
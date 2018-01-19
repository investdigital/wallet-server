package com.oxchains.wallet.function.BalanceFunction;

import com.oxchains.wallet.entity.Balance;
import org.web3j.protocol.Web3j;

/**
 * Created by xuqi on 2018/1/19.
 */
public class BalanceContext {
    private BalanceStrategy balanceStrategy;

    public BalanceContext(BalanceStrategy balanceStrategy) {
        this.balanceStrategy = balanceStrategy;
    }
    public Balance getBalance(Balance balance, Web3j web3j){
        return balanceStrategy.getBalance(balance,web3j);
    };
}

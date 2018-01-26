package com.oxchains.wallet.function.BalanceFunction;

import com.oxchains.wallet.entity.Balance;
import org.web3j.protocol.Web3j;

import java.math.BigInteger;

/**
 * Created by xuqi on 2018/1/19.
 */
public class BalanceContext {
    private BalanceStrategy balanceStrategy;

    public BalanceContext(BalanceStrategy balanceStrategy) {
        this.balanceStrategy = balanceStrategy;
    }
    public BigInteger getBalance(String address, Web3j web3j){
        return balanceStrategy.getBalance(address,web3j);
    };
}

package com.oxchains.wallet.function.BalanceFunction;

import com.oxchains.wallet.entity.Balance;
import org.web3j.protocol.Web3j;

import java.util.concurrent.ExecutionException;

/**
 * Created by xuqi on 2018/1/19.
 */
public interface BalanceStrategy {
    public Balance getBalance(Balance balance, Web3j web3j);
}

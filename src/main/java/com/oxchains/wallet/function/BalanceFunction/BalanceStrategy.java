package com.oxchains.wallet.function.BalanceFunction;

import com.oxchains.wallet.entity.Balance;
import org.web3j.protocol.Web3j;

import java.math.BigInteger;
import java.util.concurrent.ExecutionException;

/**
 * Created by huohuo on 2018/1/19.
 */
public interface BalanceStrategy {
    public String getBalance(String address, Web3j web3j);
}

package com.oxchains.wallet.function.BalanceFunction.function;

import com.oxchains.wallet.entity.Balance;
import com.oxchains.wallet.function.BalanceFunction.BalanceStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetBalance;

import java.io.IOException;
import java.math.BigInteger;

/**
 * Created by xuqi on 2018/1/19.
 */
public class BalanceETH implements BalanceStrategy {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Override
    public BigInteger getBalance(String address, Web3j web3j) {
        try {
            EthGetBalance send = web3j.ethGetBalance(address, DefaultBlockParameterName.LATEST).send();
            BigInteger bigInteger = send.getBalance();
            return bigInteger;
        } catch (IOException e) {
            logger.error("get blanace faild:",e.getMessage(),e);
        }
        return null;
    }
}

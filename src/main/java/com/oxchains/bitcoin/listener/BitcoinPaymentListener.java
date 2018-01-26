package com.oxchains.bitcoin.listener;

import com.oxchains.bitcoin.rpcclient.BitcoindRpcClient;

/**
 * @author oxchains
 * @time 2017-11-07 11:33
 * @name BitcoinPaymentListener
 * @desc:
 */
public interface BitcoinPaymentListener {
    void block(String blockHash);
    void transaction(BitcoindRpcClient.Transaction transaction);
}

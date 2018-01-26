package com.oxchains.bitcoin.listener;

import com.oxchains.bitcoin.rpcclient.BitcoindRpcClient;

/**
 * @author oxchains
 * @time 2017-11-07 11:36
 * @name SimpleBitcoinPaymentListener
 * @desc:
 */
public class SimpleBitcoinPaymentListener implements BitcoinPaymentListener {
    @Override
    public void block(String blockHash) {
    }

    @Override
    public void transaction(BitcoindRpcClient.Transaction transaction) {
    }
}

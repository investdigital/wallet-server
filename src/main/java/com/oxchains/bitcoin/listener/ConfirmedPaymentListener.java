package com.oxchains.bitcoin.listener;

import com.oxchains.bitcoin.rpcclient.BitcoindRpcClient;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @author oxchains
 * @time 2017-11-07 11:37
 * @name ConfirmedPaymentListener
 * @desc:
 */
public abstract class ConfirmedPaymentListener extends SimpleBitcoinPaymentListener {
    public int minConf;

    public ConfirmedPaymentListener(int minConf) {
        this.minConf = minConf;
    }

    public ConfirmedPaymentListener() {
        this(6);
    }

    protected Set<String> processed = Collections.synchronizedSet(new HashSet<String>());

    protected boolean markProcess(String txId) {
        return processed.add(txId);
    }

    @Override
    public void transaction(BitcoindRpcClient.Transaction transaction) {
        if (transaction.confirmations() < minConf) {
            return;
        }
        if (!markProcess(transaction.txId())) {
            return;
        }
        confirmed(transaction);
    }

    public abstract void confirmed(BitcoindRpcClient.Transaction transaction);
}


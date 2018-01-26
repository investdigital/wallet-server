package com.oxchains.wallet.entity;

import com.oxchains.bitcoin.rpcclient.BitcoindRpcClient;
import lombok.Data;

/**
 * Created by xuqi on 2018/1/24.
 */
@Data
public class Unspent {
    private String txId;

    private int outputIndex;

    private String address;

    //private String account;

    private String script;

    private long satoshis;

    //private int confirmations;

    public void setUnspent(BitcoindRpcClient.Unspent unspent){
        this.txId = unspent.txid();
        this.outputIndex = unspent.vout();
        this.address = unspent.address();
        //this.account = unspent.account();
        this.script = unspent.scriptPubKey();
        this.satoshis = (long) (unspent.amount()*100000000);
        //this.confirmations = unspent.confirmations();

    }
}

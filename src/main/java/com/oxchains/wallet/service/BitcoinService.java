package com.oxchains.wallet.service;

import com.oxchains.bitcoin.rpcclient.BitcoinJSONRPCClient;
import com.oxchains.bitcoin.rpcclient.BitcoinRpcException;
import com.oxchains.bitcoin.rpcclient.BitcoindRpcClient;
import com.oxchains.wallet.common.BitcoinConfig;
import com.oxchains.wallet.common.RestResp;
import com.oxchains.wallet.entity.BTCTransaction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xuqi on 2018/1/22.
 */
@Service
@Slf4j
public class BitcoinService {
    private static BitcoinJSONRPCClient client = null;
    private BitcoinJSONRPCClient getClient(){
        try {
            if(null == client){
                synchronized (this){
                    if(null == client){
                        String urlStr= BitcoinConfig.getUrlString();
                        URL url = new URL(urlStr);
                        client = new BitcoinJSONRPCClient(url);
                    }
                }
            }
            return client;
        } catch (MalformedURLException e) {
            log.error("get client faild:{}",e.getMessage(),e);
            return client;
        }
    }
    /**
     * get account's balance
     *
     * @param accountName
     * @return
     */
    public double getBalance(String accountName) {
        double balance = 0.0d;
        try {
            balance = this.getClient().getBalance(accountName);
            return balance;
        } catch (Exception e) {
            log.error("获取余额失败", e);
            return balance;
        }
    }
    public RestResp getStatus(String txHash){
        try {
            BitcoindRpcClient.RawTransaction rawTransaction = this.getClient().getRawTransaction(txHash);
            if(null == rawTransaction){
                return RestResp.fail("此交易不存在");
            }
            int confirmations = rawTransaction.confirmations();
            return RestResp.success(1);
        } catch (Exception e) {
            return RestResp.success(0);
        }
    }
    public RestResp sendFrom(BTCTransaction btcTransaction){
        try {
            String s = this.getClient().sendFrom(btcTransaction.getAccountName(), btcTransaction.getAddress(), btcTransaction.getAmount());
            return RestResp.success(s);
        } catch (BitcoinRpcException e) {
            log.error("send tx faild :",e.getCause(),e);
            return RestResp.fail();
        }
    }
    public RestResp sendToAddress(BTCTransaction btcTransaction){
        try {
            BitcoinJSONRPCClient client = this.getClient();
            String s = this.getClient().sendToAddress(btcTransaction.getAddress(),btcTransaction.getAmount());
            return RestResp.success(s);
        } catch (BitcoinRpcException e) {
            log.error("send tx faild :",e.getCause(),e);
            return RestResp.fail();
        }
    }
    /*public RestResp sendTx(BTCTransaction btcTransaction){
        try {
            List<BitcoindRpcClient.TxInput> txInputs = new ArrayList<>();
            List<BitcoindRpcClient.TxOutput> txOutputs = new ArrayList<>();

            BitcoindRpcClient.TxInput txInput = new BitcoindRpcClient.BasicTxInput(utxoTxid, 0);
            txInputs.add(txInput);

            BitcoindRpcClient.TxOutput txOutput = new BitcoindRpcClient.BasicTxOutput(btcTransaction.getAddress(), btcTransaction.getAmount());
            txOutputs.add(txOutput);

            client.importPrivKey(btcTransaction.getPriKey(),btcTransaction.getFromAddress(),true);

            String rawTx = client.createRawTransaction(txInputs, txOutputs);
            String signedTx = client.signRawTransaction(rawTx);
            String txId = client.sendRawTransaction(signedTx);
            return RestResp.success(txId);
        } catch (BitcoinRpcException e) {
            log.error("send tx faild :",e.getCause(),e);
            return RestResp.fail();
        }
    }*/
    public RestResp getTxInfo(String txStr){
        try {
            BitcoindRpcClient.RawTransaction rawTransaction = this.getClient().getRawTransaction(txStr);
            return RestResp.success(rawTransaction);
        } catch (BitcoinRpcException e) {
            log.error("send tx faild :",e.getCause(),e);
            return RestResp.fail();
        }
    }

}

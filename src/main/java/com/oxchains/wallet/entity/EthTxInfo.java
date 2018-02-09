package com.oxchains.wallet.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Created by huohuo on 2018/1/26.
 */
@Data
public class EthTxInfo {
    private BigDecimal amount;
    private String txId;
    private String from ;
    private String to;
    private BigInteger blockNumber;
    private Integer status;
    public void set(org.web3j.protocol.core.methods.response.Transaction result){
        double pow = Math.pow(10, 18);
        BigDecimal divisor = new BigDecimal(String.valueOf(pow));
        BigDecimal dividend =  new BigDecimal(result.getValue());
        this.amount = dividend.divide(divisor);
        this.txId = result.getHash();
        this.from = result.getFrom();
        this.to = result.getTo();
        this.status = 0;
        try {
            this.blockNumber = result.getBlockNumber();
        } catch (Exception e) {
            this.blockNumber = null;
        }
    }
}

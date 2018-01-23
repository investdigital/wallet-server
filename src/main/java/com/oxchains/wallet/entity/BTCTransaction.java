package com.oxchains.wallet.entity;

import lombok.Data;

/**
 * Created by xuqi on 2018/1/23.
 */
@Data
public class BTCTransaction {
    private String accountName;
    private String address;
    private double amount;
    private String priKey;
    private String pubKey;
    private String fromAddress;


}

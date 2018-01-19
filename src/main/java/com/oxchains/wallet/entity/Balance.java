package com.oxchains.wallet.entity;

import lombok.Data;

import java.math.BigInteger;

/**
 * Created by xuqi on 2018/1/19.
 */
@Data
public class Balance {
    private String address;
    private String type;
    private BigInteger value;
}

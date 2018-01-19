package com.oxchains.wallet.common;

/**
 * Created by xuqi on 2018/1/19.
 */
public enum  CoinType {
    ETH("eth",""),IDT("idt","0x02c4c78c462e32cca4a90bc499bf411fb7bc6afb");
    private String name;
    private String url;

    CoinType(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }
}

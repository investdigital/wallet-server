package com.oxchains.wallet.common;

import com.alibaba.fastjson.JSONObject;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author: huohuo
 * Created in 13:57  2018/2/1.
 */
@Component
public class PriceListening {
    public static double btc = 0d;
    public static double eth = 0d;
    private String btc_url = "https://api.coinmarketcap.com/v1/ticker/bitcoin/";
    private String eth_url = "https://api.coinmarketcap.com/v1/ticker/ethereum/";
    @Scheduled(cron = "*/30 * * * * ?")
    public void listening(){
        String btcStr = HttpUtils.sendGet(btc_url);
        List<JSONObject> btcs = JsonUtil.jsonToList(btcStr, JSONObject.class);
        if(btcs != null && btcs.size()>=1){
            Object price_usd = btcs.get(0).get("price_usd");
            btc = Double.valueOf(String.valueOf(price_usd));
        }
        String ethStr = HttpUtils.sendGet(eth_url);
        List<JSONObject> eths = JsonUtil.jsonToList(ethStr, JSONObject.class);
        if(eths != null && eths.size()>=1){
            String s = eths.get(0).get("price_usd").toString();
            eth = Double.valueOf(s);
        }
    }
}

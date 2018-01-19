package com.oxchains.wallet.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

/**
 * Created by xuqi on 2018/1/19.
 */
@Component
public class ETHUtil {
    @Value("${eth.web3j.url}")
    private String web3jUrl;
    private ETHUtil() {
    }
    private Web3j web3j;
    public Web3j getWeb3j(){
         if(web3j == null){
             synchronized (this){
                 if(web3j == null){
                     web3j = Web3j.build(new HttpService(web3jUrl));
                 }
             }
         }
         return web3j;
    }


}

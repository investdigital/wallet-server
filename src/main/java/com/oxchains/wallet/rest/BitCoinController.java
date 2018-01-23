package com.oxchains.wallet.rest;

import com.oxchains.wallet.common.RestResp;
import com.oxchains.wallet.entity.BTCTransaction;
import com.oxchains.wallet.service.BitcoinService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * Created by xuqi on 2018/1/22.
 */
@RestController
@RequestMapping("/BitCoin")
public class BitCoinController {
    @Resource
    private BitcoinService bitcoinService;
    @GetMapping("/getBalance/{accountName}")
    public RestResp getBalance(@PathVariable String accountName){
        double balance = bitcoinService.getBalance(accountName);
        return RestResp.success(balance);
    }
    @GetMapping("/getStatus/{txHash}")
    public RestResp getStatus(@PathVariable String txHash){
        return bitcoinService.getStatus(txHash);
    }
    @PostMapping("/sendFrom")
    public RestResp sendTx(@RequestBody BTCTransaction btcTransaction){
        return bitcoinService.sendFrom(btcTransaction);
    }
    @PostMapping("/sendToAddress")
    public RestResp sendToAddress(@RequestBody BTCTransaction btcTransaction){
        return bitcoinService.sendToAddress(btcTransaction);
    }
    @GetMapping("/getTxInfo/{txHash}")
    public RestResp getTxInfo(@PathVariable String txHash){
        return bitcoinService.getTxInfo(txHash);
    }
}

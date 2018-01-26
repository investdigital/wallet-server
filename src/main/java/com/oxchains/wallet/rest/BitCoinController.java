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
    @GetMapping("/getBalanceByAccount/{accountName}")
    public RestResp getBalanceByAccount(@PathVariable String accountName){
        double balance = bitcoinService.getBalanceByAccount(accountName);
        return RestResp.success(balance);
    }
    @GetMapping("/getBalanceByAddress/{address}")
    public RestResp getBalanceByAddress(@PathVariable String address){
        double balance = bitcoinService.getBalanceByAddress(address);
        return RestResp.success(balance);
    }
    @GetMapping("/getStatus/{txHash}")
    public RestResp getStatus(@PathVariable String txHash){
        return bitcoinService.getStatus(txHash);
    }
    @PostMapping("/sendFrom")
    public RestResp sendFrom(@RequestBody BTCTransaction btcTransaction){
        return bitcoinService.sendFrom(btcTransaction);
    }
    @PostMapping("/sendTx")
    public RestResp sendTx(@RequestParam String  txStr){
        return bitcoinService.sendTx(txStr);
    }
    @PostMapping("/sendToAddress")
    public RestResp sendToAddress(@RequestBody BTCTransaction btcTransaction){
        return bitcoinService.sendToAddress(btcTransaction);
    }
    @PostMapping("/initAddress")
    public RestResp initAddress(@RequestParam String address,@RequestParam String publicKey){
        return bitcoinService.initAddress(address,publicKey);
    }
    @GetMapping("/getTxInfo/{txHash}")
    public RestResp getTxInfo(@PathVariable String txHash){
        return bitcoinService.getTxInfo(txHash);
    }
    @GetMapping("/getUnspents/{address}")
    public RestResp getUnspents(@PathVariable  String address){
        return bitcoinService.getUnspents(address);
    }
    @GetMapping("/getAllPrice")
    public RestResp getNonce(){
        return bitcoinService.getBtcPrice();
    }
}

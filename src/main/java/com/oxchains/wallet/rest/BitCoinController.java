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
    @GetMapping("/getBalanceByAddress/{address}")
    public RestResp getBalanceByAddress(@PathVariable String address){
        if(this.checkAddress(address)){
            double balance = bitcoinService.getBalanceByAddress(address);
            return RestResp.success(balance);
        }
        return RestResp.fail("invalid address");
    }
    @GetMapping("/getStatus/{txHash}")
    public RestResp getStatus(@PathVariable String txHash){
        if(this.checkTxHash(txHash)){
            return bitcoinService.getStatus(txHash);
        }
        return RestResp.fail("invalid txHash");
    }
    @PostMapping("/sendTx")
    public RestResp sendTx(@RequestParam String  txStr){
        if(txStr == null){
            return RestResp.fail("txStr is null");
        }
        return bitcoinService.sendTx(txStr);
    }
    @PostMapping("/initAddress")
    public RestResp initAddress(@RequestParam String address,@RequestParam String publicKey){
        if(this.checkAddress(address) && publicKey != null){
            return bitcoinService.initAddress(address,publicKey);
        }
        return RestResp.fail("invalid address or publicKey");
    }
    @GetMapping("/getTxInfo/{txHash}")
    public RestResp getTxInfo(@PathVariable String txHash){
        if(this.checkTxHash(txHash)){
            return bitcoinService.getTxInfo(txHash);
        }
        return RestResp.fail("invalid txHash");
    }
    @GetMapping("/getUnspents/{address}")
    public RestResp getUnspents(@PathVariable  String address){
        if(this.checkAddress(address)){
            return bitcoinService.getUnspents(address);
        }
        return RestResp.fail("invalid address");
    }
    @GetMapping("/getAllPrice")
    public RestResp getNonce(){
        return bitcoinService.getBtcPrice();
    }

    private boolean checkAddress(String address){
        if(address == null || address.length() != 34){
            return false;
        }
        return true;
    }
    private boolean checkTxHash(String txHash){
        if(txHash == null || txHash.length() != 64){
            return false;
        }
        return true;
    }
}

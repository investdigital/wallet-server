package com.oxchains.wallet.rest;

import com.oxchains.wallet.common.CheckUtil;
import com.oxchains.wallet.common.RestResp;
import com.oxchains.wallet.service.BitcoinService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * Created by huohuo on 2018/1/22.
 */
@RestController
@RequestMapping("/BitCoin")
public class BitCoinController {
    @Resource
    private BitcoinService bitcoinService;

    /*
    * @desc get Balance by address
    * @param address --  The user's wallet address.
    * */
    @GetMapping("/getBalanceByAddress/{address}")
    public RestResp getBalanceByAddress(@PathVariable String address){
        if(CheckUtil.checkBtcAddress(address)){
            double balance = bitcoinService.getBalanceByAddress(address);
            return RestResp.success(balance);
        }
        return RestResp.fail("invalid address");
    }
    /*
    * @desc get transaction status
    * @param txHash  -- the transaction Id
    * */
    @GetMapping("/getStatus/{txHash}")
    public RestResp getStatus(@PathVariable String txHash){
        if(CheckUtil.checkBtcTxHash(txHash)){
            return bitcoinService.getStatus(txHash);
        }
        return RestResp.fail("invalid txHash");
    }
    /*
    * @desc send txStr to chain To trade
    * @param txStr -- Signed transaction string.
    * */
    @PostMapping("/sendTx")
    public RestResp sendTx(@RequestParam String  txStr){
        if(txStr == null){
            return RestResp.fail("txStr is null");
        }
        return bitcoinService.sendTx(txStr);
    }
    /*
    * @desc Initialize the account to your own node so that your node can see all transactions in this address.
    * @param address The user's wallet address.
    * @param publicKey The user's publicKey.
    * */
    @PostMapping("/initAddress")
    public RestResp initAddress(@RequestParam String address,@RequestParam String publicKey){
        if(CheckUtil.checkBtcAddress(address) && publicKey != null){
            return bitcoinService.initAddress(address,publicKey);
        }
        return RestResp.fail("invalid address or publicKey");
    }
    /*
    *@desc Get the details of the transaction.
    *@param txHash The user's transaction id.
    * */
    @GetMapping("/getTxInfo/{txHash}")
    public RestResp getTxInfo(@PathVariable String txHash){
        if(CheckUtil.checkBtcTxHash(txHash)){
            return bitcoinService.getTxInfo(txHash);
        }
        return RestResp.fail("invalid txHash");
    }
    /*
    * @desc Get this address's Available balance
    * @param The user's wallet address
    * */
    @GetMapping("/getUnspents/{address}")
    public RestResp getUnspents(@PathVariable  String address){
        if(CheckUtil.checkBtcAddress(address)){
            return bitcoinService.getUnspents(address);
        }
        return RestResp.fail("invalid address");
    }
    //获取价格行情
    /*
    * @desc Access to digital currency in real time prices.
    * */
    @GetMapping("/getAllPrice")
    public RestResp getNonce(){
        return bitcoinService.getBtcPrice();
    }
}

package com.oxchains.wallet.rest;
import com.oxchains.wallet.common.RestResp;
import com.oxchains.wallet.entity.Balance;
import com.oxchains.wallet.service.WalletService;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
/**
 * Created by xuqi on 2018/1/17.
 */
@RestController
@RequestMapping("/wallet")
public class WalletController {
    @Resource
    WalletService walletService;
    //get eth keyStore by a password
    @PostMapping("/getKeyStore")
    public RestResp getKeyStore(@NotNull(message = "password is null") @RequestParam String password){
        return walletService.getKeyStore(password);
    }
    //send tx to eth
    @PostMapping("/sendTx")
    public RestResp sendTx(@NotNull @RequestParam String tx){
        return walletService.sendTx(tx);
    }
    //get blanace by address
    @GetMapping("/getBlanace/{address}/{type}")
    public RestResp getBlanace(@PathVariable String address,@PathVariable String type){
        return walletService.getBalance(address,type);
    }
    //get tx info by txHash
    @GetMapping("/getTxInfo/{txHash}")
    public RestResp getTxInfo(@NotNull @PathVariable String txHash){
        return walletService.getTxInfo(txHash);
    }
    @GetMapping("/getStatus/{txHash}")
    public RestResp getStatus(@NotNull @PathVariable String txHash){
        return walletService.getStatus(txHash);
    }
    @GetMapping("/getNonce/{address}")
    public RestResp getNonce(@PathVariable String address){
        return walletService.getNonce(address);
    }

}

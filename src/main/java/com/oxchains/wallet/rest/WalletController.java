package com.oxchains.wallet.rest;
import com.oxchains.wallet.common.RestResp;
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
    @PostMapping("/getBlanace")
    public RestResp getBlanace(@NotNull @RequestParam String address){
        return walletService.getBalance(address);
    }
    //get tx info by txHash
    @PostMapping("/getTxInfo")
    public RestResp getTxInfo(@NotNull @RequestParam String txHash){
        return walletService.getTxInfo(txHash);
    }
    @PostMapping("/getStatus")
    public RestResp getStatus(@NotNull @RequestParam String txHash){
        return walletService.getStatus(txHash);
    }
}

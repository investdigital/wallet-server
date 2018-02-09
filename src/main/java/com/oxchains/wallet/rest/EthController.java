package com.oxchains.wallet.rest;
import com.oxchains.wallet.common.CheckUtil;
import com.oxchains.wallet.common.RestResp;
import com.oxchains.wallet.entity.Balance;
import com.oxchains.wallet.entity.TouchInfo;
import com.oxchains.wallet.service.WalletService;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
/**
 * Created by huohuo on 2018/1/17.
 */
@RestController
@RequestMapping("/wallet")
public class EthController {
    @Resource
    WalletService walletService;
    //get eth keyStore by a password
    @PostMapping("/getKeyStore")
    public RestResp getKeyStore(@NotNull(message = "password is null") @RequestParam String password){
        return walletService.getKeyStore(password);
    }
    //send tx to eth
    @PostMapping("/sendTx")
    public RestResp sendTx(@RequestParam String tx){
        if(tx == null){
            return RestResp.fail("txStr is null");
        }
        return walletService.sendTx(tx);
    }
    //get blanace by address
    @GetMapping("/getBlanace/{address}/{type}")
    public RestResp getBlanace(@PathVariable String address,@PathVariable String type){
        if(CheckUtil.checkEthAddress(address)){
            return walletService.getBalance(address,type);
        }
        return RestResp.fail("invalid address");
    }
    //get tx info by txHash
    @GetMapping("/getTxInfo/{txHash}")
    public RestResp getTxInfo(@PathVariable String txHash){
        if(CheckUtil.checkEthTxHash(txHash)){
            return walletService.getTxInfo(txHash);
        }
        return RestResp.fail("invalid txHash");
    }
    /*
    * desc: get status by txHash
    * */
    @GetMapping("/getStatus/{txHash}")
    public RestResp getStatus(@PathVariable String txHash){
        if(CheckUtil.checkEthTxHash(txHash)){
            return walletService.getStatus(txHash);
        }
        return RestResp.fail("invalid txHash");
    }
    /*
    *desc: get nonce by address
    * */
    @GetMapping("/getNonce/{address}")
    public RestResp getNonce(@PathVariable String address){
        if(CheckUtil.checkEthAddress(address)){
            return walletService.getNonce(address);
        }
        return RestResp.fail("invalid address");
    }
    /*
    * install app init device
    * */
    @PostMapping("/initDevice")
    public RestResp initDevice(@Valid @RequestBody TouchInfo touchInfo,BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return RestResp.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        return walletService.initDevice(touchInfo);
    }
}

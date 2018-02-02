package com.oxchains.wallet.service;
import cn.jiguang.common.ClientConfig;
import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.Notification;
import com.oxchains.wallet.common.CoinType;
import com.oxchains.wallet.common.ETHUtil;
import com.oxchains.wallet.common.RestResp;
import com.oxchains.wallet.entity.Balance;
import com.oxchains.wallet.entity.TouchInfo;
import com.oxchains.wallet.function.BalanceFunction.BalanceContext;
import com.oxchains.wallet.function.BalanceFunction.BalanceStrategy;
import com.oxchains.wallet.function.BalanceFunction.function.BalanceETH;
import com.oxchains.wallet.function.BalanceFunction.function.BalanceIDT;
import com.oxchains.wallet.repo.TouchInfoRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.Response;
import org.web3j.protocol.core.methods.request.*;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.*;
import org.web3j.protocol.http.HttpService;
import rx.Subscription;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.List;

/**
 * Created by xuqi on 2018/1/17.
 */
@Service
public class WalletService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Value("${keystore.file.path}")
    private String path;
    @Resource
    private ETHUtil ethUtil;
    @Resource
    private TouchInfoRepo touchInfoRepo;
    //get keystore with password
    public RestResp getKeyStore(String password){
        InputStream inputStream = null;
        try{
            String s = WalletUtils.generateFullNewWalletFile(password, new File(path));
            File file = new File(path+s);
            if(file.exists()){
                inputStream = new FileInputStream(file);
                byte[] cha = new byte[(int) file.length()];
                inputStream.read(cha);
                inputStream.close();
                boolean delete = file.delete();
                return RestResp.success(new String(cha));
            }
        }catch (Exception e){
             logger.error("get keystore fail :",e.getMessage(),e);
        }finally {
             if(inputStream != null){
                 try {
                     inputStream.close();
                 } catch (IOException e) {
                     logger.error("close inputStream fail :",e.getMessage(),e);
                 }
             }
        }
        return RestResp.fail("网络错误");
    }
    //send tx to chain
    public RestResp sendTx(String tx){
        try {
            Web3j web3j = ethUtil.getWeb3j();
            EthSendTransaction send = web3j.ethSendRawTransaction(tx).send();
            Response.Error error = send.getError();
            if(send.getTransactionHash() == null){
                if(error != null){
                    String message = error.getMessage();
                    if(message.startsWith("replacement")||message.startsWith("known")){
                        return RestResp.fail(-2,error.getMessage());
                    }
                    if(message.startsWith("insufficient funds for gas * price")){
                        return RestResp.fail("余额不足");
                    }
                        return RestResp.fail(error.getMessage());
                }
            }
            return RestResp.success(send.getTransactionHash());
        } catch (Exception e) {
            logger.error("send tx faild :",e.getMessage(),e);
            return RestResp.fail();
        }
    }
    //get tx info
    public RestResp getTxInfo(String txHash){
        try {
            Web3j web3j = ethUtil.getWeb3j();
            EthTransaction send = web3j.ethGetTransactionByHash(txHash).send();
            org.web3j.protocol.core.methods.response.Transaction result = send.getResult();
            if(null == result){
                return RestResp.fail("交易不存在");
            }
            return RestResp.success(result);
        } catch (Exception e) {
            logger.error("get tx info :",e.getMessage(),e);
            return RestResp.fail();
        }
    }
    //get tx  nonce by address
    public RestResp getNonce(String address){
        try {
            Web3j web3j = ethUtil.getWeb3j();
            EthGetTransactionCount count = web3j.ethGetTransactionCount(address, DefaultBlockParameterName.PENDING).send();
            BigInteger transactionCount = count.getTransactionCount();
            int i = transactionCount.intValue();
            String s = Integer.toHexString(i);
            if(s.length() % 2 == 0){
                s = "0x"+s;
            }
            else{
                s = "0x0"+s;
            }
            return RestResp.success(s);
        } catch (Exception e) {
            e.printStackTrace();
            return RestResp.fail();
        }
    }
    //get address balance
    public RestResp getBalance(String address,String type){
        try {
            Web3j web3j = ethUtil.getWeb3j();
            BalanceContext balanceContext = null;
            if(type.toLowerCase().equals(CoinType.ETH.getName())){
                balanceContext = new BalanceContext(new BalanceETH());
            }
            if(type.toLowerCase().equals(CoinType.IDT.getName())){
                balanceContext = new BalanceContext(new BalanceIDT());
            }
            String balance = balanceContext.getBalance(address, web3j);
            return balance != null ? RestResp.success(balance):RestResp.fail();
        } catch (Exception e) {
            logger.error("get balance faild :",e.getMessage(),e);
            return RestResp.fail();
        }
    }
    //get tx status by txHash
    public RestResp getStatus(String txhash){
        try {
            Web3j web3j = ethUtil.getWeb3j();

            EthTransaction send = web3j.ethGetTransactionByHash(txhash).send();
            org.web3j.protocol.core.methods.response.Transaction result = send.getResult();
            //交易详情不存在 交易不存在
            if(null == result){
                return RestResp.fail("交易不存在");
            }
            //交易存在 blocknumber为null 交易未完成
            if(result.getBlockNumber() == null){
                return RestResp.success("0");
            }
            //交易存在 blocknumber存在 状态为1 交易完成
            EthGetTransactionReceipt send1 = web3j.ethGetTransactionReceipt(txhash).send();
            String status = send1.getResult().getStatus();
            if(status.equals("0x1")){
                RestResp.success("1");
            }
            //交易存在 blocknumber存在 状态为0 交易失败
            return RestResp.fail("交易失败");
        } catch (Exception e) {
            logger.error("get balance faild :",e.getMessage(),e);
            return RestResp.fail();
        }
    }
    public RestResp initDevice(TouchInfo touchInfo){
        try {
            TouchInfo save = touchInfoRepo.save(touchInfo);
            if(save != null){
                return RestResp.success();
            }
            return RestResp.fail("init deviced faild");
        } catch (Exception e) {
            logger.error("init deviced faild :{}",e.getMessage(),e);
            return RestResp.fail("init deviced faild");
        }
    }
}

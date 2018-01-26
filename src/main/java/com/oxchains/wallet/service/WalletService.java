package com.oxchains.wallet.service;
import com.oxchains.wallet.common.CoinType;
import com.oxchains.wallet.common.ETHUtil;
import com.oxchains.wallet.common.RestResp;
import com.oxchains.wallet.entity.Balance;
import com.oxchains.wallet.function.BalanceFunction.BalanceContext;
import com.oxchains.wallet.function.BalanceFunction.BalanceStrategy;
import com.oxchains.wallet.function.BalanceFunction.function.BalanceETH;
import com.oxchains.wallet.function.BalanceFunction.function.BalanceIDT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.Response;
import org.web3j.protocol.core.methods.request.*;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.*;
import org.web3j.protocol.http.HttpService;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
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
                    return RestResp.fail(error.getMessage());
                }
            }
            return RestResp.success(send.getTransactionHash());
        } catch (Exception e) {
            logger.error("send tx faild :",e.getMessage(),e);
        }
        return RestResp.fail();
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
            return RestResp.success(transactionCount);
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
            BigInteger balance = balanceContext.getBalance(address, web3j);
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
            EthGetTransactionReceipt send = web3j.ethGetTransactionReceipt(txhash).send();
            String status = send.getResult().getStatus();
            return RestResp.success(status);
        } catch (Exception e) {
            logger.error("get balance faild :",e.getMessage(),e);
            return RestResp.fail();
        }
    }

}

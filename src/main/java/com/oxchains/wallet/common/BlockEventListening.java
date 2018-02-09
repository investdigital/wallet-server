package com.oxchains.wallet.common;
import cn.jiguang.common.ClientConfig;
import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.device.TagAliasResult;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.Notification;
import com.oxchains.wallet.entity.EthTxInfo;
import com.oxchains.wallet.entity.TouchInfo;
import com.oxchains.wallet.repo.TouchInfoRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.EthBlock;
import rx.Subscription;
import javax.annotation.Resource;
import java.util.List;

import static javax.accessibility.AccessibleRole.ALERT;

/**
 * Created by huohuo on 2018/1/30.
 */
@Component
public class BlockEventListening implements ApplicationListener<ContextRefreshedEvent>{
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Resource
    private JPushClient jPushClient;
    @Resource
    private TouchInfoRepo touchInfoRepo;
    @Resource
    private Web3j web3j;

    private Subscription subscription = null;
    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        this.BlockEventFunction();
    }
    private void BlockEventFunction(){
        if(subscription != null && !subscription.isUnsubscribed()){
            subscription.unsubscribe();
            subscription = null;
        }
         subscription = web3j.blockObservable(true).take(1).subscribe(ethBlock -> {
            EthBlock.Block block = ethBlock.getBlock();
            List<EthBlock.TransactionResult> transactions = block.getTransactions();
            transactions.stream().forEach(transactionResult -> {
                org.web3j.protocol.core.methods.response.Transaction o = (org.web3j.protocol.core.methods.response.Transaction) transactionResult.get();
                TouchInfo byAddress = touchInfoRepo.findByAddress(o.getTo());
                if(byAddress != null){
                    EthTxInfo ethTxInfo = new EthTxInfo();
                    ethTxInfo.set(o);
                    PushResult push = this.push(ethTxInfo,byAddress.getRegistrationID());
                    if(push.statusCode == 1){
                        logger.info("push success");
                    }
                }
            });
            this.BlockEventFunction();
        });
    }
    private PushResult push(EthTxInfo ethTxInfo,String registrationID){
        try {
            PushResult pushResult = jPushClient.sendAndroidMessageWithRegistrationID("", JsonUtil.toJson(ethTxInfo), registrationID);
            return pushResult;
        } catch (Exception e) {
            logger.error("push faild:{}",e.getMessage(),e);
            return null;
        }
    }
    /*public static void main(String[] args) throws APIConnectionException, APIRequestException {
        JPushClient jPushClient = new JPushClient("5efd487f203c711857786fcb","562b0cec8614a3ff48228735",null, ClientConfig.getInstance());
        PushResult pushResult = jPushClient.sendAndroidMessageWithRegistrationID("touchWalet system message", "收到了啊", "190e35f7e04f0c0a505");
        System.out.println(pushResult);
        jPushClient.close();
    }*/
}

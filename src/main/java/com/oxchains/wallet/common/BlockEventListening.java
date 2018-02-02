package com.oxchains.wallet.common;
import cn.jiguang.common.ClientConfig;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.Notification;
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
 * Created by xuqi on 2018/1/30.
 */
@Component
public class BlockEventListening implements ApplicationListener<ContextRefreshedEvent>{
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private JPushClient jPushClient;
    @Resource
    private TouchInfoRepo touchInfoRepo;
    @Resource
    private ETHUtil ethUtil;
    @Value("${jiguang.push.MasterSecret}")
    private String masterSecret;
    @Value("${jiguang.push.AppKey}")
    private String appKey;
    private Subscription subscription = null;
    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        Web3j web3j = ethUtil.getWeb3j();
        this.BlockEventFunction(web3j);
    }
    private void BlockEventFunction(Web3j web3j){
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
                    //推送
                    PushResult push = this.push(o);
                    if(push.statusCode == 1){
                        logger.info("push success");
                    }
                }
            });
            this.BlockEventFunction(web3j);
        });
    }
    public int findAllThreads() {
        ThreadGroup group = Thread.currentThread().getThreadGroup();
        ThreadGroup topGroup = group;
        // 遍历线程组树，获取根线程组
        while (group != null) {
            topGroup = group;
            group = group.getParent();
        }
        // 激活的线程数加倍
        int estimatedSize = topGroup.activeCount() * 2;
        Thread[] slacks = new Thread[estimatedSize];
        //获取根线程组的所有线程
        int actualSize = topGroup.enumerate(slacks);
        Thread[] threads = new Thread[actualSize];
        System.arraycopy(slacks, 0, threads, 0, actualSize);
        return threads.length;
    }

    private PushResult push(org.web3j.protocol.core.methods.response.Transaction transaction){
        try {
            JPushClient jpushClient = this.getjPushClient();
            //快捷地构建推送对象：所有平台，所有设备，内容为 ALERT 的通知。
            PushPayload payload = PushPayload.alertAll("alert");
            //构建推送对象：所有平台，推送目标是别名为 "alias1"，通知内容为 ALERT。
            PushPayload payload1 = PushPayload.newBuilder()
                    .setPlatform(Platform.all())
                    .setAudience(Audience.alias("alias1"))
                    .setNotification(Notification.alert(ALERT))
                    .build();
            //构建推送对象：平台是 Android，目标是 tag 为 "tag1" 的设备，内容是 Android 通知 ALERT，并且标题为 TITLE。
            PushPayload.newBuilder()
                    .setPlatform(Platform.android())
                    .setAudience(Audience.tag("tag1"))
                    .setNotification(Notification.android("alert", "title", null))
                    .build();


            PushResult result = jpushClient.sendPush(payload);
            return result;
        } catch (Exception e) {
            logger.error("push faild:{}",e.getMessage(),e);
            return null;
        }
    }
    private JPushClient getjPushClient(){
        if(this.jPushClient == null){
            this.jPushClient = new JPushClient(masterSecret, appKey, null, ClientConfig.getInstance());
            return this.jPushClient;
        }
        return this.jPushClient;
    }
}

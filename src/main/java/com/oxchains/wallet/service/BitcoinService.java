package com.oxchains.wallet.service;
import com.alibaba.fastjson.JSONObject;
import com.oxchains.bitcoin.rpcclient.BitcoinJSONRPCClient;
import com.oxchains.bitcoin.rpcclient.BitcoinRpcException;
import com.oxchains.bitcoin.rpcclient.BitcoindRpcClient;
import com.oxchains.wallet.common.*;
import com.oxchains.wallet.entity.BTCTransaction;
import com.oxchains.wallet.entity.Btc.BlockChainInfo;
import com.oxchains.wallet.entity.DigitalPrice;
import com.oxchains.wallet.entity.Eth.EthPrice;
import com.oxchains.wallet.entity.Eth.EthPriceDetail;
import com.oxchains.wallet.entity.Unspent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
/**
 * Created by xuqi on 2018/1/22.
 */
@Service
@Slf4j
public class BitcoinService {
    @Value("${bitcoin.min.minConf}")
    private int minConf;
    @Value("${bitcoin.max.maxConf}")
    private int maxConf;
    private static BitcoinJSONRPCClient client = null;
    private BitcoinJSONRPCClient getClient(){
        try {
            if(null == client){
                synchronized (this){
                    if(null == client){
                        String urlStr= BitcoinConfig.getUrlString();
                        URL url = new URL(urlStr);
                        client = new BitcoinJSONRPCClient(url);
                    }
                }
            }
            return client;
        } catch (MalformedURLException e) {
            log.error("get client faild:{}",e.getMessage(),e);
            return client;
        }
    }
    public RestResp initAddress(String address,String publicKey){
        try {
            this.getClient().importPubkey(publicKey);
            this.getClient().addWitnessAdress(address);
            return RestResp.success("");
        } catch (BitcoinRpcException e) {
            log.error("init address faild:{}",e.getMessage(),e);
            return RestResp.fail();
        }
    }
    public double getBalanceByAddress(String address) {
         double[] balance = {0.0d};
        try {
            List<BitcoindRpcClient.Unspent> unspentList = this.getClient().listUnspent(minConf, maxConf, address);
            unspentList.stream().forEach(unspent -> {
                balance[0] += unspent.amount();
            });
            return balance[0];
        } catch (Exception e) {
            log.error("获取余额失败", e);
            return balance[0];
        }
    }
    public RestResp getStatus(String txHash){
        try {
            BitcoindRpcClient.RawTransaction rawTransaction = this.getClient().getRawTransaction(txHash);
            if(null == rawTransaction){
                return RestResp.success(2);
            }
            int confirmations = rawTransaction.confirmations();
            return RestResp.success(1);
        } catch (Exception e) {
            log.error("get status faild:{}",e.getMessage(),e);
            return RestResp.success(0);
        }
    }
    public RestResp sendTx(String  txStr){
        try {
            String s = this.getClient().sendRawTransaction(txStr);
            return RestResp.success(s);
        } catch (BitcoinRpcException e) {
            log.error("send tx faild :",e.getCause(),e);
            return RestResp.fail();
        }
    }
    public RestResp getUnspents(String address){
        try {
            BitcoinJSONRPCClient client = this.getClient();
            List<BitcoindRpcClient.Unspent> unspents = this.getClient().listUnspent(minConf,maxConf,address);
            if(unspents != null){
                List<Unspent> unspentList = new ArrayList<>(unspents.size());
                unspents.stream().forEach(unspent -> {
                    Unspent unspent1 = new Unspent();
                    unspent1.setUnspent(unspent);
                    unspentList.add(unspent1);
                });
                return RestResp.success(unspentList);
            }
            return RestResp.fail();
        } catch (BitcoinRpcException e) {
            log.error("send tx faild :",e.getCause(),e);
            return RestResp.fail();
        }
    }
    public RestResp getTxInfo(String txStr){
        try {
            BitcoindRpcClient.RawTransaction rawTransaction = this.getClient().getRawTransaction(txStr);
            return RestResp.success(rawTransaction);
        } catch (BitcoinRpcException e) {
            log.error("send tx faild :",e.getCause(),e);
            return RestResp.fail();
        }
    }
    /**
     * 调度器
     * 每间隔 10 分钟执行一次
     *
     * 如果行情获取失败，就return
     * 如果行情获取成功，就保存，且数据库只保存一条btc-cny的信息，新得信息就update
     */
    public RestResp getBtcPrice() {
        //获取btc的行情
        DigitalPrice digitalPrice = new DigitalPrice();
        /*String url = "https://blockchain.info/ticker";
        String result = HttpUtils.sendGet(url);
        BlockChainInfo blockChainInfo = (BlockChainInfo) JsonUtil.fromJson(result, BlockChainInfo.class);
        digitalPrice.setBtc_usdt(blockChainInfo.getUSD().getLast());
        digitalPrice.setBtc_cny(blockChainInfo.getCNY().getLast());
        //获取以太币的行情
        String etcUrl = "https://api.huobi.pro/market/history/kline?symbol=ethusdt&period=1min&size=1";
        String userAgent = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.71 Safari/537.36";
        String s = HttpUtils.sendGet(etcUrl, userAgent);
        EthPrice ethPrice = JsonUtil.jsonToEntity(s, EthPrice.class);
        List<EthPriceDetail> ethPriceDetails = JsonUtil.objectToList(ethPrice.getData(), EthPriceDetail.class);
        if(ethPriceDetails != null || ethPriceDetails.size() >0 ){
            Double etcPrice = 0d;
            EthPriceDetail ethPriceDetail = ethPriceDetails.get(0);
            if(ethPriceDetail.getOpen() != null && ethPriceDetail.getClose() != null){
                etcPrice = (ethPriceDetail.getClose()+ethPriceDetail.getOpen())/2;
                digitalPrice.setEth_usdt(etcPrice);
                return RestResp.success(digitalPrice);
            }
            if(ethPriceDetail.getOpen() != null || ethPriceDetail.getClose() != null){
                etcPrice = ethPriceDetail.getOpen()!=null?ethPriceDetail.getOpen():ethPriceDetail.getClose();
                digitalPrice.setEth_usdt(etcPrice);
                return RestResp.success(digitalPrice);
            }
        }*/
        digitalPrice.setBtc_usdt(PriceListening.btc);
        digitalPrice.setEth_usdt(PriceListening.eth);
        return RestResp.success(digitalPrice);
    }

    public static void main(String[] args) {
        String url = "https://api.coinmarketcap.com/v1/ticker/bitcoin/";
        String s = HttpUtils.sendGet(url);
        List<JSONObject> jsonObjects = JsonUtil.jsonToList(s, JSONObject.class);
        System.out.println(jsonObjects.get(0).get("price_usd"));
    }
}

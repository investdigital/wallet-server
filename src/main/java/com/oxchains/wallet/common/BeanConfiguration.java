package com.oxchains.wallet.common;

import cn.jiguang.common.ClientConfig;
import cn.jpush.api.JPushClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

/**
 * @Author: huohuo
 * Created in 11:35  2018/2/8.
 */
@Component
public class BeanConfiguration {
    @Value("${jiguang.push.MasterSecret}")
    private String masterSecret;
    @Value("${jiguang.push.AppKey}")
    private String appKey;
    @Value("${eth.web3j.url}")
    private String web3jUrl;
    @Bean
    public JPushClient getJPushClient(){
        JPushClient jPushClient = new JPushClient(masterSecret, appKey, null, ClientConfig.getInstance());
        return jPushClient;
    }
    @Bean
    public Web3j getWeb3j(){
        Web3j web3j = Web3j.build(new HttpService(web3jUrl));
        return web3j;
    }

    /*@Bean
    public EmbeddedServletContainerFactory servletContainer() {

        TomcatEmbeddedServletContainerFactory tomcat = new TomcatEmbeddedServletContainerFactory() {

            @Override
            protected void postProcessContext(Context context) {

                SecurityConstraint securityConstraint = new SecurityConstraint();
                securityConstraint.setUserConstraint("CONFIDENTIAL");
                SecurityCollection collection = new SecurityCollection();
                collection.addPattern("*//*");
                securityConstraint.addCollection(collection);
                context.addConstraint(securityConstraint);
            }
        };
        tomcat.addAdditionalTomcatConnectors(initiateHttpConnector());
        return tomcat;
    }

    private Connector initiateHttpConnector() {

        Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
        connector.setScheme("http");
        connector.setPort(8080);
        connector.setSecure(false);
        connector.setRedirectPort(8882);
        return connector;
    }*/
}

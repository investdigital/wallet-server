package com.oxchains.wallet.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @author oxchains
 * @time 2017-10-13 17:51
 * @name AbstractConfig
 * @desc:
 */
public abstract class AbstractConfig implements Serializable {

    private static final Logger logger = LoggerFactory.getLogger(AbstractConfig.class);
    private static final long serialVersionUID = 8558830137024320514L;

    protected static final int MAX_LENGTH = 200;

    protected static final int MAX_PATH_LENGTH = 200;

    protected static final Pattern PATTERN_NAME = Pattern.compile("[\\-._0-9a-zA-Z]+");

    protected static final Pattern PATTERN_MULTI_NAME = Pattern.compile("[,\\-._0-9a-zA-Z]+");

    protected static final Pattern PATTERN_METHOD_NAME = Pattern.compile("[a-zA-Z][0-9a-zA-Z]*");

    protected static final Pattern PATTERN_PATH = Pattern.compile("[/\\-$._0-9a-zA-Z]+");

    protected static final Pattern PATTERN_NAME_HAS_SYMBOL = Pattern.compile("[:*,/\\-._0-9a-zA-Z]+");

    protected static final Pattern PATTERN_KEY = Pattern.compile("[*,\\-._0-9a-zA-Z]+");
    protected static final Map<String, String> LEGACY_PROPERTIES = new HashMap<String, String>();
    protected static final String[] SUFFIXS = new String[]{"Config", "Bean"};

    static {
        LEGACY_PROPERTIES.put("bitcoin.service.url", "bitcoin.service.address");
        LEGACY_PROPERTIES.put("bitcoin.service.port", "bitcoin.service.port");
        LEGACY_PROPERTIES.put("bitcoin.service.username", "bitcoin.service.rpcuser");
        LEGACY_PROPERTIES.put("bitcoin.service.password", "bitcoin.service.rpcpassword");
    }


}

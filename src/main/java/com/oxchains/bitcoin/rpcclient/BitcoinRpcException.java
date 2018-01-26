package com.oxchains.bitcoin.rpcclient;

/**
 * @author oxchains
 * @time 2017-10-30 9:55
 * @nameBitcoinRpcException
 * @desc:
 */
public class BitcoinRpcException extends RuntimeException{
    public BitcoinRpcException() {
    }
    public BitcoinRpcException(String msg) {
        super(msg);
    }

    public BitcoinRpcException(Throwable cause) {
        super(cause);
    }

    public BitcoinRpcException(String message, Throwable cause) {
        super(message, cause);
    }
}

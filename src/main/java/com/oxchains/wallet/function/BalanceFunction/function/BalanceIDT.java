package com.oxchains.wallet.function.BalanceFunction.function;

import com.oxchains.wallet.common.CoinType;
import com.oxchains.wallet.entity.Balance;
import com.oxchains.wallet.function.BalanceFunction.BalanceStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthCall;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;

/**
 * Created by xuqi on 2018/1/19.
 */
public class BalanceIDT implements BalanceStrategy{
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Override
    public Balance getBalance(Balance balance, Web3j web3j){
        try {
            Function function = new Function("balanceOf",
                    Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(balance.getAddress())),
                    Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
            String encodedFunction = FunctionEncoder.encode(function);
            EthCall ethCall1 = web3j.ethCall(Transaction.createEthCallTransaction("0x17Bc58b788808DaB201a9A90817fF3C168BF3d61", CoinType.IDT.getUrl(), encodedFunction), DefaultBlockParameterName.LATEST).sendAsync().get();
            String value = ethCall1.getValue().substring(2);
            balance.setValue(new BigInteger(value,16));
        } catch (Exception e) {
            logger.error("get blanace idt faild:",e.getMessage(),e);
        }
        return balance;
    }
}

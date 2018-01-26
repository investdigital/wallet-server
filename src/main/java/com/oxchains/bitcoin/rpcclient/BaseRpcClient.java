package com.oxchains.bitcoin.rpcclient;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author oxchains
 * @time 2017-10-30 11:28
 * @nameBaseRpcClient
 * @desc:
 */
public interface BaseRpcClient {
    interface DecodedScript extends Serializable {

        String asm();

        String hex();

        String type();

        int reqSigs();

        List<String> addresses();

        String p2sh();
    }

    interface Info extends Serializable {
        long version();

        long protocolVersion();

        long walletVersion();

        double balance();

        int blocks();

        int timeOffset();

        int connections();

        String proxy();

        double difficulty();

        boolean testnet();

        long keyPoolOldest();

        long keyPoolSize();

        double payTxFee();

        double relayFee();

        String errors();
    }

    interface MiningInfo extends Serializable {

        int blocks();

        int currentBlockSize();

        int currentBlockWeight();

        int currentBlockTx();

        double difficulty();

        String errors();

        double networkHashps();

        int pooledTx();

        boolean testNet();

        String chain();
    }

    interface NetTotals extends Serializable {

        long totalBytesRecv();

        long totalBytesSent();

        long timeMillis();

        interface uploadTarget extends Serializable {

            long timeFrame();

            int target();

            boolean targetReached();

            boolean serveHistoricalBlocks();

            long bytesLeftInCycle();

            long timeLeftInCycle();
        }

        uploadTarget uploadTarget();
    }

    interface BlockChainInfo extends Serializable {

        String chain();

        int blocks();

        String bestBlockHash();

        double difficulty();

        double verificationProgress();

        String chainWork();
    }

    interface WalletInfo extends Serializable {

        long walletVersion();

        BigDecimal balance();

        BigDecimal unconfirmedBalance();

        BigDecimal immatureBalance();

        long txCount();

        long keyPoolOldest();

        long keyPoolSize();

        long unlockedUntil();

        BigDecimal payTxFee();

        String hdMasterKeyId();
    }

    interface NetworkInfo extends Serializable {

        long version();

        String subversion();

        long protocolVersion();

        String localServices();

        boolean localRelay();

        long timeOffset();

        long connections();

        List<Network> networks();

        BigDecimal relayFee();

        List<String> localAddresses();

        String warnings();
    }

    interface Network extends Serializable {

        String name();

        boolean limited();

        boolean reachable();

        String proxy();

        boolean proxyRandomizeCredentials();
    }

    interface MultiSig extends Serializable {

        String address();

        String redeemScript();
    }

    interface NodeInfo extends Serializable {

        String addedNode();

        boolean connected();

        List<Address> addresses();

    }

    interface Address extends Serializable {

        String address();

        String connected();
    }

    interface TxOut extends Serializable {
        String bestBlock();

        long confirmations();

        BigDecimal value();

        String asm();

        String hex();

        long reqSigs();

        String type();

        List<String> addresses();

        long version();

        boolean coinBase();

    }

    interface Block extends Serializable {

        String hash();

        int confirmations();

        int size();

        int height();

        int version();

        String merkleRoot();

        List<String> tx();

        Date time();

        long nonce();

        String bits();

        double difficulty();

        String previousHash();

        String nextHash();

        String chainwork();

        Block previous() throws BitcoinRpcException;

        Block next() throws BitcoinRpcException;
    }

    interface TxOutSetInfo extends Serializable {

        long height();

        String bestBlock();

        long transactions();

        long txouts();

        long bytesSerialized();

        String hashSerialized();

        BigDecimal totalAmount();
    }

    interface RawTransaction extends Serializable {

        String hex();

        String txId();

        int version();

        long lockTime();

        long size();

        long vsize();

        String hash();

        /*
         *
         */
        interface In extends TxInput, Serializable {

            Map<String, Object> scriptSig();

            long sequence();

            RawTransaction getTransaction();

            Out getTransactionOutput();
        }

        /**
         * This method should be replaced someday
         *
         * @return the list of inputs
         */
        List<In> vIn(); // TODO : Create special interface instead of this

        interface Out extends Serializable {

            double value();

            int n();

            interface ScriptPubKey extends Serializable {

                String asm();

                String hex();

                int reqSigs();

                String type();

                List<String> addresses();
            }

            ScriptPubKey scriptPubKey();

            TxInput toInput();

            RawTransaction transaction();
        }

        /**
         * This method should be replaced someday
         */
        List<Out> vOut(); // TODO : Create special interface instead of this

        String blockHash();

        int confirmations();

        Date time();

        Date blocktime();
    }

    interface ReceivedAddress extends Serializable {

        String address();

        String account();

        double amount();

        int confirmations();
    }

    /**
     * returned by listsinceblock and listtransactions
     */
    interface Transaction extends Serializable {

        String account();

        String address();

        String category();

        double amount();

        double fee();

        int confirmations();

        String blockHash();

        int blockIndex();

        Date blockTime();

        String txId();

        Date time();

        Date timeReceived();

        String comment();

        String commentTo();

        RawTransaction raw();
    }

    interface TransactionsSinceBlock extends Serializable {

        List<Transaction> transactions();

        String lastBlock();
    }

    interface AddressValidationResult extends Serializable {

        boolean isValid();

        String address();

        boolean isMine();

        boolean isScript();

        String pubKey();

        boolean isCompressed();

        String account();
    }

    interface PeerInfoResult extends Serializable {

        long getId();

        String getAddr();

        String getAddrLocal();

        String getServices();

        long getLastSend();

        long getLastRecv();

        long getBytesSent();

        long getBytesRecv();

        long getConnTime();

        int getTimeOffset();

        double getPingTime();

        long getVersion();

        String getSubVer();

        boolean isInbound();

        int getStartingHeight();

        long getBanScore();

        int getSyncedHeaders();

        int getSyncedBlocks();

        boolean isWhiteListed();
    }

    interface TxInput extends Serializable {

        String txid();

        int vout();

        String scriptPubKey();
    }

    class BasicTxInput implements TxInput {

        public String txid;
        public int vout;
        public String scriptPubKey;

        public BasicTxInput(String txid, int vout) {
            this.txid = txid;
            this.vout = vout;
        }

        public BasicTxInput(String txid, int vout, String scriptPubKey) {
            this(txid, vout);
            this.scriptPubKey = scriptPubKey;
        }

        @Override
        public String txid() {
            return txid;
        }

        @Override
        public int vout() {
            return vout;
        }

        @Override
        public String scriptPubKey() {
            return scriptPubKey;
        }

    }

    class ExtendedTxInput extends BasicTxInput {

        public String redeemScript;
        public BigDecimal amount;

        public ExtendedTxInput(String txid, int vout) {
            super(txid, vout);
        }

        public ExtendedTxInput(String txid, int vout, String scriptPubKey) {
            super(txid, vout, scriptPubKey);
        }

        public ExtendedTxInput(String txid, int vout, String scriptPubKey,String redeemScript) {
            super(txid, vout, scriptPubKey);
            this.redeemScript = redeemScript;
        }

        public ExtendedTxInput(String txid, int vout, String scriptPubKey, String redeemScript, BigDecimal amount) {
            super(txid, vout, scriptPubKey);
            this.redeemScript = redeemScript;
            this.amount = amount;
        }

        public String redeemScript() {
            return redeemScript;
        }

        public BigDecimal amount() {
            return amount;
        }

    }

    interface TxOutput extends Serializable {

        String address();

        double amount();
    }

    class BasicTxOutput implements TxOutput {

        public String address;
        public double amount;

        public BasicTxOutput(String address, double amount) {
            this.address = address;
            this.amount = amount;
        }

        @Override
        public String address() {
            return address;
        }

        @Override
        public double amount() {
            return amount;
        }
    }

    interface Unspent extends TxInput, TxOutput, Serializable {

        @Override
        String txid();

        @Override
        int vout();

        @Override
        String address();

        String account();

        String scriptPubKey();

        @Override
        double amount();

        //TO DO
        int confirmations();
    }
}

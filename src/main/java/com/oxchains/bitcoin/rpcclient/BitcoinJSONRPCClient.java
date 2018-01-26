package com.oxchains.bitcoin.rpcclient;

import com.oxchains.bitcoin.util.BitcoinUtil;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

/**
 * @author ccl
 * @time 2017-11-07 11:47
 * @name BitcoinJSONRPCClient
 * @desc:
 */
public class BitcoinJSONRPCClient extends BaseBitcoinJSONRPCClient {

    /*public BitcoinJSONRPCClient() {
    }*/

    public BitcoinJSONRPCClient(URL rpc) {
        super(rpc);
    }
    public BitcoinJSONRPCClient(String rpcUrl) throws MalformedURLException {
        super(rpcUrl);
    }

    /*public BitcoinJSONRPCClient(boolean testNet) {
        super(testNet);
    }*/

    /** <> *** bitcoin rpcs ***</> */

    @Override
    public void abandonTransaction(String txid) throws BitcoinRpcException {
        query("abandontransaction",txid);
    }

    @Override
    public String addMultiSigAddress(int nRequired, List<String> keyObject, String account) {
        return (String) query("addmultisigaddress", nRequired, keyObject,account);
    }

    @Override
    public String addMultiSigAddress(int nRequired, List<String> keyObject) {
        return (String) query("addmultisigaddress", nRequired, keyObject);
    }

    @Override
    public void addNode(String node, String command) {
        query("addnode", node, command);
    }

    @Override
    public String addWitnessAdress(String address) {
        return (String) query("addwitnessaddress", address);
    }

    @Override
    public void backupWallet(String destination) {
        query("backupwallet", destination);
    }

    @Override
    public void clearBanned() {
        query("clearbanned");
    }

    @Override
    public MultiSig createMultiSig(int nRequired, List<String> keys) throws BitcoinRpcException {
        return new MultiSigWrapper((Map) query("createmultisig", nRequired, keys));
    }

    @Override
    public String createRawTransaction(List<TxInput> inputs, List<TxOutput> outputs, Integer locktime) throws BitcoinRpcException {
        List<Map> pInputs = new ArrayList<>();

        for (final TxInput txInput : inputs) {
            pInputs.add(new LinkedHashMap() {
                {
                    put("txid", txInput.txid());
                    put("vout", txInput.vout());
                }
            });
        }

        Map<String, Double> pOutputs = new LinkedHashMap();

        Double oldValue;
        for (TxOutput txOutput : outputs) {
            if ((oldValue = pOutputs.put(txOutput.address(), txOutput.amount())) != null) {
                pOutputs.put(txOutput.address(), BitcoinUtil.normalizeAmount(oldValue + txOutput.amount()));
            }
        }

        if(null == locktime){
            return (String) query("createrawtransaction", pInputs, pOutputs);
        }

        return (String) query("createrawtransaction", pInputs, pOutputs, locktime);
    }

    @Override
    public String createRawTransaction(List<TxInput> inputs, List<TxOutput> outputs) throws BitcoinRpcException {
        return createRawTransaction(inputs,outputs,null);
    }

    public RawTransaction decodeRawTransaction(String hex) throws BitcoinRpcException {
        Map result = (Map) query("decoderawtransaction", hex);
        RawTransaction rawTransaction = new RawTransactionImpl(result);
        return rawTransaction.vOut().get(0).transaction();
    }

    @Override
    public DecodedScript decodeScript(String hex) {
        return new DecodedScriptImpl((Map) query("decodescript", hex));
    }

    @Override
    public void disconnectNode(String address) {
        query("disconnect",address);
    }

    @Override
    public String dumpPrivKey(String address) throws BitcoinRpcException {
        return (String) query("dumpprivkey", address);
    }

    @Override
    public void dumpWallet(String filename) {
        query("dumpwallet", filename);
    }

    @Override
    public void encryptWallet(String passPhrase) {
        query("encryptwallet", passPhrase);
    }

    @Override
    public double estimateFee(int blockNum) {
        return ((Number) query("estimatefee", blockNum)).doubleValue();
    }

    @Override
    public double estimatePriority(int blockNum) {
        return ((Number) query("estimatepriority", blockNum)).doubleValue();
    }

    @Override
    public List<String> generate(int numBlocks, int maxtries) throws BitcoinRpcException {
        return (List<String>) query("generate", numBlocks,maxtries);
    }

    @Override
    public List<String> generate(int numBlocks) throws BitcoinRpcException {
        return (List<String>) query("generate", numBlocks);
    }

    @Override
    public List<String> generateToAddress(int numBlocks, String address, int maxtries) throws BitcoinRpcException {
        return (List<String>) query("generatetoaddress", numBlocks,address,maxtries);
    }

    @Override
    public List<String> generateToAddress(int numBlocks, String address) throws BitcoinRpcException {
        return (List<String>) query("generatetoaddress", numBlocks,address);
    }

    @Override
    public String getAccountAddress(String account) throws BitcoinRpcException {
        return (String) query("getaccountaddress", account);
    }

    @Override
    public String getAccount(String address) throws BitcoinRpcException {
        return (String) query("getaccount", address);
    }

    @Override
    public List<NodeInfo> getAddedNodeInfo(boolean dummy, String node) {
        List<Map> list = null;
        if(null==node){
            list =((List<Map>) query("getaddednodeinfo", dummy));
        }else {
            list =((List<Map>) query("getaddednodeinfo", dummy, node));
        }
        List<NodeInfo> nodeInfoList = new LinkedList<NodeInfo>();
        for(Map m: list){
            NodeInfoWrapper niw = new NodeInfoWrapper(m);
            nodeInfoList.add(niw);
        }
        return nodeInfoList;
    }

    @Override
    public List<NodeInfo> getAddedNodeInfo(boolean dummy) {
        return getAddedNodeInfo(dummy,null);
    }

    @Override
    public List<String> getAddressesByAccount(String account) throws BitcoinRpcException {
        return (List<String>) query("getaddressesbyaccount", account);
    }

    @Override
    public double getBalance(String account, int minConf, boolean watchOnly) throws BitcoinRpcException {
        return ((Number) query("getbalance", account, minConf,watchOnly)).doubleValue();
    }

    @Override
    public double getBalance(String account, int minConf) throws BitcoinRpcException {
        return ((Number) query("getbalance", account, minConf)).doubleValue();
    }

    @Override
    public double getBalance(String account) throws BitcoinRpcException {
        return ((Number) query("getbalance", account)).doubleValue();
    }

    @Override
    public double getBalance() throws BitcoinRpcException {
        return ((Number) query("getbalance")).doubleValue();
    }

    @Override
    public String getBestBlockHash() throws BitcoinRpcException {
        return (String) query("getbestblockhash");
    }

    @Override
    public Block getBlock(String blockHash) throws BitcoinRpcException {
        return new BlockMapWrapper((Map) query("getblock", blockHash));
    }

    @Override
    public Block getBlock(int height) throws BitcoinRpcException {
        String hash = (String) query("getblockhash", height);
        return getBlock(hash);
    }

    @Override
    public BlockChainInfo getBlockChainInfo() throws BitcoinRpcException {
        return new BlockChainInfoMapWrapper((Map) query("getblockchaininfo"));
    }

    @Override
    public int getBlockCount() throws BitcoinRpcException {
        return ((Number) query("getblockcount")).intValue();
    }

    @Override
    public String getBlockHash(int height) throws BitcoinRpcException {
        return (String) query("getblockhash", height);
    }

    @Override
    public String getBlockHeader(String hex, boolean format) {
        return null;
    }

    @Override
    public String getBlockHeader(String hex) {
        return null;
    }

    @Override
    public long getConnectionCount() {
        return (long) query("getconnectioncount");
    }

    @Override
    public double getDifficulty() {
        if (query("getdifficulty") instanceof Long) {
            return ((Long) query("getdifficulty")).doubleValue();
        } else {
            return (double) query("getdifficulty");
        }
    }

    /** It doesn't work */
    @Override
    public boolean getGenerate() {
        return (boolean) query("getgenerate");
    }

    @Override
    public Info getInfo() throws BitcoinRpcException {
        return new InfoWrapper((Map) query("getinfo"));
    }

    @Override
    public MiningInfo getMiningInfo() throws BitcoinRpcException {
        return new MiningInfoWrapper((Map) query("getmininginfo"));
    }

    @Override
    public NetTotals getNetTotals() throws BitcoinRpcException {
        return new NetTotalsImpl((Map) query("getnettotals"));
    }

    @Override
    public double getNetworkHashPs() {
        return (Double)query("getnetworkhashps");
    }

    @Override
    public NetworkInfo getNetworkInfo() throws BitcoinRpcException {
        return new NetworkInfoWrapper((Map) query("getnetworkinfo"));
    }

    @Override
    public String getNewAddress(String account) throws BitcoinRpcException {
        return (String) query("getnewaddress", account);
    }

    @Override
    public String getNewAddress() throws BitcoinRpcException {
        return (String) query("getnewaddress");
    }

    @Override
    public List<PeerInfoResult> getPeerInfo() {
        final List<Map> list = (List<Map>) query("getpeerinfo");

        return new AbstractList<PeerInfoResult>() {

            @Override
            public PeerInfoResult get(int index) {
                return new PeerInfoWrapper(list.get(index));
            }

            @Override
            public int size() {
                return list.size();
            }
        };
    }

    @Override
    public String getRawChangeAddress() {
        return (String) query("getrawchangeaddress");
    }

    @Override
    public List<String> getRawMemPool(boolean format) throws BitcoinRpcException {
        return (List<String>) query("getrawmempool",format);
    }

    @Override
    public List<String> getRawMemPool() throws BitcoinRpcException {
        return (List<String>) query("getrawmempool");
    }

    @Override
    public RawTransaction getRawTransaction(String txId, boolean format) throws BitcoinRpcException {
        return new RawTransactionImpl((Map)query("getrawtransaction",txId,true));
    }

    @Override
    public RawTransaction getRawTransaction(String txId) throws BitcoinRpcException {
        return new RawTransactionImpl((Map) query("getrawtransaction", txId, 1));
    }

    @Override
    public String getRawTransactionHex(String txId) throws BitcoinRpcException {
        return (String) query("getrawtransaction", txId);
    }

    @Override
    public BigDecimal getReceivedByAccount(String account, int minConf) throws BitcoinRpcException {
        return new BigDecimal((String)query("getreceivedbyaccount", account, minConf));
    }

    @Override
    public BigDecimal getReceivedByAccount(String account) throws BitcoinRpcException {
        return getReceivedByAccount(account, 1);
    }

    @Override
    public double getReceivedByAddress(String address, int minConf) throws BitcoinRpcException {
        return ((Number) query("getreceivedbyaddress", address, minConf)).doubleValue();
    }

    @Override
    public double getReceivedByAddress(String address) throws BitcoinRpcException {
        return ((Number) query("getreceivedbyaddress", address)).doubleValue();
    }

    @Override
    public Transaction getTransaction(String txid) {
        return null;
    }

    @Override
    public TxOut getTxOut(String txId, long vout) throws BitcoinRpcException {
        return new TxOutWrapper((Map) query("gettxout", txId, vout, true));
    }

    @Override
    public TxOutSetInfo getTxOutSetInfo() {
        return new TxOutSetInfoWrapper((Map) query("gettxoutsetinfo"));
    }

    @Override
    public double getUnconfirmedBalance() {
        return (double) query("getunconfirmedbalance");
    }

    @Override
    public WalletInfo getWalletInfo() {
        return new WalletInfoWrapper((Map) query("getwalletinfo"));
    }

    @Override
    public Object importAddress(String address, String label, boolean rescan) throws BitcoinRpcException {
        query("importaddress", address, label, rescan);
        return null;
    }

    @Override
    public void importPrivKey(String bitcoinPrivKey, String label, boolean rescan) throws BitcoinRpcException {
        query("importprivkey", bitcoinPrivKey,label,rescan);
    }

    @Override
    public void importPrivKey(String bitcoinPrivKey, String label) throws BitcoinRpcException {
        query("importprivkey", bitcoinPrivKey,label);
    }

    @Override
    public void importPrivKey(String bitcoinPrivKey) throws BitcoinRpcException {
        query("importprivkey", bitcoinPrivKey);
    }

    @Override
    public void importWallet(String filename) {
        query("dumpwallet", filename);
    }

    @Override
    public void keyPoolRefill(int size) {
        query("keypoolrefill", size);
    }

    @Override
    public void keyPoolRefill() {
        query("keypoolrefill", 100);
    }

    @Override
    public Map<String, Number> listAccounts() throws BitcoinRpcException {
        return (Map) query("listaccounts");
    }

    @Override
    public Map<String, Number> listAccounts(int minConf) throws BitcoinRpcException {
        return (Map) query("listaccounts", minConf);
    }

    @Override
    public List<ReceivedAddress> listReceivedByAddress() throws BitcoinRpcException {
        return new ReceivedAddressListWrapper((List) query("listreceivedbyaddress"));
    }

    @Override
    public List<ReceivedAddress> listReceivedByAddress(int minConf) throws BitcoinRpcException {
        return new ReceivedAddressListWrapper((List) query("listreceivedbyaddress", minConf));
    }

    @Override
    public List<ReceivedAddress> listReceivedByAddress(int minConf, boolean includeEmpty) throws BitcoinRpcException {
        return new ReceivedAddressListWrapper((List) query("listreceivedbyaddress", minConf, includeEmpty));
    }

    @Override
    public TransactionsSinceBlock listSinceBlock() throws BitcoinRpcException {
        return new TransactionsSinceBlockImpl((Map) query("listsinceblock"));
    }

    @Override
    public TransactionsSinceBlock listSinceBlock(String blockHash) throws BitcoinRpcException {
        return new TransactionsSinceBlockImpl((Map) query("listsinceblock", blockHash));
    }

    @Override
    public TransactionsSinceBlock listSinceBlock(String blockHash, int targetConfirmations) throws BitcoinRpcException {
        return new TransactionsSinceBlockImpl((Map) query("listsinceblock", blockHash, targetConfirmations));
    }

    @Override
    public List<Transaction> listTransactions() throws BitcoinRpcException {
        return new TransactionListMapWrapper((List) query("listtransactions"));
    }

    @Override
    public List<Transaction> listTransactions(String account) throws BitcoinRpcException {
        return new TransactionListMapWrapper((List) query("listtransactions", account));
    }

    @Override
    public List<Transaction> listTransactions(String account, int count) throws BitcoinRpcException {
        return new TransactionListMapWrapper((List) query("listtransactions", account, count));
    }

    @Override
    public List<Transaction> listTransactions(String account, int count, int from) throws BitcoinRpcException {
        return new TransactionListMapWrapper((List) query("listtransactions", account, count, from));
    }

    @Override
    public List<Unspent> listUnspent() throws BitcoinRpcException {
        return new UnspentListWrapper((List) query("listunspent"));
    }

    @Override
    public List<Unspent> listUnspent(int minConf) throws BitcoinRpcException {
        return new UnspentListWrapper((List) query("listunspent", minConf));
    }

    @Override
    public List<Unspent> listUnspent(int minConf, int maxConf) throws BitcoinRpcException {
        return new UnspentListWrapper((List) query("listunspent", minConf, maxConf));
    }

    @Override
    public List<Unspent> listUnspent(int minConf, int maxConf, String... addresses) throws BitcoinRpcException {
        return new UnspentListWrapper((List) query("listunspent", minConf, maxConf, addresses));
    }

    @Override
    public String move(String fromAccount, String toBitcoinAddress, double amount) throws BitcoinRpcException {
        return (String) query("move", fromAccount, toBitcoinAddress, amount);
    }

    @Override
    public String move(String fromAccount, String toBitcoinAddress, double amount, int minConf) throws BitcoinRpcException {
        return (String) query("move", fromAccount, toBitcoinAddress, amount, minConf);
    }

    @Override
    public String move(String fromAccount, String toBitcoinAddress, double amount, int minConf, String comment) throws BitcoinRpcException {
        return (String) query("move", fromAccount, toBitcoinAddress, amount, minConf, comment);
    }

    @Override
    public void ping() {
        query("ping");
    }

    @Override
    public String sendFrom(String fromAccount, String toBitcoinAddress, double amount, int minConf, String comment, String commentTo) throws BitcoinRpcException {
        return (String) query("sendfrom", fromAccount, toBitcoinAddress, amount, minConf, comment, commentTo);
    }

    @Override
    public String sendFrom(String fromAccount, String toBitcoinAddress, double amount, int minConf, String comment) throws BitcoinRpcException {
        return (String) query("sendfrom", fromAccount, toBitcoinAddress, amount, minConf, comment);
    }

    @Override
    public String sendFrom(String fromAccount, String toBitcoinAddress, double amount) throws BitcoinRpcException {
        return (String) query("sendfrom", fromAccount, toBitcoinAddress, amount);
    }

    @Override
    public String sendFrom(String fromAccount, String toBitcoinAddress, double amount, int minConf) throws BitcoinRpcException {
        return (String) query("sendfrom", fromAccount, toBitcoinAddress, amount, minConf);
    }

    @Override
    public String sendMany(String fromAccount, List<TxOutput> outputs) throws BitcoinRpcException {
        Map<String, Double> pOutputs = new LinkedHashMap();
        Double oldValue;
        for (TxOutput txOutput : outputs) {
            if ((oldValue = pOutputs.put(txOutput.address(), txOutput.amount())) != null) {
                pOutputs.put(txOutput.address(), BitcoinUtil.normalizeAmount(oldValue + txOutput.amount()));
            }
        }
        return (String) query("sendmany",fromAccount,pOutputs);
    }

    @Override
    public String sendRawTransaction(String hex) throws BitcoinRpcException {
        return (String) query("sendrawtransaction", hex);
    }

    @Override
    public String sendToAddress(String toAddress, double amount, String comment, String commentTo) throws BitcoinRpcException {
        return (String) query("sendtoaddress", toAddress, amount, comment, commentTo);
    }

    @Override
    public String sendToAddress(String toAddress, double amount) throws BitcoinRpcException {
        return (String) query("sendtoaddress", toAddress, amount);
    }

    @Override
    public String sendToAddress(String toAddress, double amount, String comment) throws BitcoinRpcException {
        return (String) query("sendtoaddress", toAddress, amount, comment);
    }

    @Override
    public void setAccount(String address, String account) {
        query("setaccount", address ,account);
    }

    @Override
    public void setGenerate(boolean doGenerate) throws BitcoinRpcException {
        query("setgenerate", doGenerate);
    }

    @Override
    public boolean setTxFee(BigDecimal amount) {
        return (boolean) query("settxfee", amount);
    }

    @Override
    public String signMessage(String bitcoinAdress, String message) {
        return (String) query("signmessage", bitcoinAdress, message);
    }

    @Override
    public String signMessageWithPrivKey(String prvKey, String message) {
        return null;
    }

    @Override
    public String signRawTransaction(String hex, List<ExtendedTxInput> inputs, List<String> privateKeys) throws BitcoinRpcException {
        return signRawTransaction(hex, inputs, privateKeys, "ALL");
    }

    @Override
    public String signRawTransaction1(String hex, ExtendedTxInput input, List<String> privateKeys) throws BitcoinRpcException {
        return signRawTransaction1(hex,input,privateKeys,"ALL");
    }

    public String signRawTransaction(String hex) throws BitcoinRpcException {
        return signRawTransaction(hex, null, null, "ALL");
    }

    private String signRawTransaction(String hex, List<ExtendedTxInput> inputs, List<String> privateKeys, String sigHashType) {
        List<Map> pInputs = null;

        if (inputs != null) {
            pInputs = new ArrayList<>();
            for (final ExtendedTxInput txInput : inputs) {
                pInputs.add(new LinkedHashMap() {
                    {
                        put("txid", txInput.txid());
                        put("vout", txInput.vout());
                        put("scriptPubKey", txInput.scriptPubKey());
                        put("redeemScript", txInput.redeemScript());
                        put("amount", txInput.amount());
                    }
                });
            }
        }

        Map result = (Map) query("signrawtransaction", hex, pInputs, privateKeys, sigHashType); //if sigHashType is null it will return the default "ALL"
        if ((Boolean) result.get("complete"))
            return (String) result.get("hex");
        else
            throw new BitcoinRpcException("Incomplete");
    }

    private String signRawTransaction1(String hex, ExtendedTxInput txInput, List<String> privateKeys, String sigHashType) {
        List<Map> pInputs = null;

        if (txInput != null) {
            pInputs = new ArrayList<>();
            pInputs.add(new LinkedHashMap() {
                {
                    put("txid", txInput.txid());
                    put("vout", txInput.vout());
                    put("scriptPubKey", txInput.scriptPubKey());
                    put("redeemScript", txInput.redeemScript());
                }
            });

        }

        Map result = (Map) query("signrawtransaction", hex, pInputs, privateKeys, sigHashType); //if sigHashType is null it will return the default "ALL"
        if ((Boolean) result.get("complete"))
            return (String) result.get("hex");
        else
            throw new BitcoinRpcException("Incomplete");
    }

    @Override
    public void stop() {
        query("stop");
    }

    @Override
    public void submitBlock(String hexData) {
        query("submitblock", hexData);
    }

    @Override
    public AddressValidationResult validateAddress(String address) throws BitcoinRpcException {
        final Map validationResult = (Map) query("validateaddress", address);
        return new AddressValidationResult() {

            @Override
            public boolean isValid() {
                return ((Boolean) validationResult.get("isvalid"));
            }

            @Override
            public String address() {
                return (String) validationResult.get("address");
            }

            @Override
            public boolean isMine() {
                return ((Boolean) validationResult.get("ismine"));
            }

            @Override
            public boolean isScript() {
                return ((Boolean) validationResult.get("isscript"));
            }

            @Override
            public String pubKey() {
                return (String) validationResult.get("pubkey");
            }

            @Override
            public boolean isCompressed() {
                return ((Boolean) validationResult.get("iscompressed"));
            }

            @Override
            public String account() {
                return (String) validationResult.get("account");
            }

            @Override
            public String toString() {
                return validationResult.toString();
            }

        };
    }

    @Override
    public boolean verifyChain() {
        return verifyChain(3, 6); //3 and 6 are the default values
    }

    private boolean verifyChain(int checklevel, int numblocks) {
        return (boolean)query("verifychain", checklevel, numblocks);
    }

    @Override
    public boolean verifyMessage(String bitcoinAddress, String signature, String message) {
        return (boolean) query("verifymessage", bitcoinAddress, signature, message);
    }

    @Override
    public void walletPassPhrase(String passPhrase, long timeOut) {
        query("walletpassphrase", passPhrase, timeOut);
    }

    @Override
    public double getEstimateFee(int nBlocks) throws BitcoinRpcException {
        return ((Number) query("estimatefee", nBlocks)).doubleValue();
    }

    @Override
    public double getEstimatePriority(int nBlocks) throws BitcoinRpcException {
        return ((Number) query("estimatepriority", nBlocks)).doubleValue();
    }

    @Override
    public void invalidateBlock(String hash) throws BitcoinRpcException {
        query("invalidateblock", hash);
    }

    @Override
    public void reconsiderBlock(String hash) throws BitcoinRpcException {
        query("reconsiderblock", hash);

    }

    public void importPubkey(String pubkey) throws BitcoinRpcException {
        query("importpubkey",pubkey);
    }
}

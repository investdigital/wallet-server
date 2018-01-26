package com.oxchains.bitcoin.rpcclient;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @author oxchains
 * @time 2017-10-30 9:53
 * @nameBitcoindRpcClient
 * @desc:
 * @url https://bitcoin.org/en/developer-reference#rpcs
 */
public interface BitcoindRpcClient extends BaseRpcClient {
    /** 1.
     * Added in Bitcoin Core 0.12.0
     * The abandontransaction RPC marks an in-wallet transaction and all its in-wallet descendants as abandoned.
     * This allows their inputs to be respent.
     * @param txid
     * @throws BitcoinRpcException
     */
    void abandonTransaction(String txid) throws BitcoinRpcException;

    /** 2.
     * Requires wallet support.
     * The addmultisigaddress RPC adds a P2SH multisig address to the wallet.
     * @param nRequired
     * @param keyObject
     * @param account
     * @return
     */
    String addMultiSigAddress(int nRequired, List<String> keyObject, String account);

    String addMultiSigAddress(int nRequired, List<String> keyObject);

    /** 3.
     * The addnode RPC attempts to add or remove a node from the addnode list,or to try a connection to a node once.
     * @param node
     * @param command
     */
    void addNode(String node, String command);

    /** 4.
     * Added in Bitcoin Core 0.13.0 Requires wallet support. Requires an unlocked wallet or an unencrypted wallet.
     * The addwitnessaddress RPC adds a witness address for a script (with pubkey or redeem script known).
     * @param address
     * @return
     */
    String addWitnessAdress(String address);

    /** 5.
     * Requires wallet support.
     * The backupwallet RPC safely copies wallet.dat to the specified file, which can be a directory or a path with filename.
     * @param destination
     */
    void backupWallet(String destination);

    /** 6.
     * Added in Bitcoin Core 0.14.0 Requires wallet support. Wallet must be unlocked.
     */
    //void bumpFee(String txid);

    /** 7.
     * Added in Bitcoin Core 0.12.0
     * The clearbanned RPC clears list of banned nodes.
     */
    void clearBanned();

    /** 8.
     * The createmultisig RPC creates a P2SH multi-signature address.
     * @param nRequired
     * @param keys
     * @return
     * @throws BitcoinRpcException
     */
    MultiSig createMultiSig(int nRequired, List<String> keys) throws BitcoinRpcException;

    /** 9.
     * The createrawtransaction RPC creates an unsigned serialized transaction that spends a previous output to a new output with a P2PKH or P2SH address.
     * The transaction is not stored in the wallet or transmitted to the network.
     * @param inputs
     * @param outputs
     * @param locktime
     * @return
     * @throws BitcoinRpcException
     */
    String createRawTransaction(List<TxInput> inputs, List<TxOutput> outputs, Integer locktime) throws BitcoinRpcException;

    String createRawTransaction(List<TxInput> inputs, List<TxOutput> outputs) throws BitcoinRpcException;

    /** 10.
     *
     */
    //void decodeRawTransaction(String hex);

    /** 11.
     * The decodescript RPC decodes a hex-encoded P2SH redeem script.
     * @param hex (redeem script)
     * @return
     */
    DecodedScript decodeScript(String hex);

    /** 12.
     * Added in Bitcoin Core 0.12.0 Updated in Bitcoin Core 0.14.1
     * The disconnectnode RPC immediately disconnects from a specified node.
     * @param address (hostname/ip)(ip:port)
     */
    void disconnectNode(String address);

    /** 13.
     * Requires wallet support. Requires an unlocked wallet or an unencrypted wallet.
     * The dumpprivkey RPC returns the wallet-import-format (WIP) private key corresponding to an address. (But does not remove it from the wallet.)
     * @param address
     * @return
     * @throws BitcoinRpcException
     */
    String dumpPrivKey(String address) throws BitcoinRpcException;

    /** 14.
     * Requires wallet support. Requires an unlocked wallet or an unencrypted wallet.The dumpwallet RPC creates or overwrites a file with all wallet keys in a human-readable format.
     * @param filename
     */
    void dumpWallet(String filename);

    /** 15.
     * Requires wallet support.
     * The encryptwallet RPC encrypts the wallet with a passphrase. This is only to enable encryption for the first time. After encryption is enabled, you will need to enter the passphrase to use private keys.
     * @param passPhrase
     */
    void encryptWallet(String passPhrase);

    /** 16.
     * The estimatefee RPC estimates the transaction fee per kilobyte that needs to be paid for a transaction to be included within a certain number of blocks.
     * @param blockNum
     * @return
     */
    double estimateFee(int blockNum);

    /** 17.
     * Added in Bitcoin Core 0.10.0.
     * Warning:estimatepriority has been removed and will no longer be available in the next major release (planned for Bitcoin Core 0.15.0). Use the RPC listed in the “See Also” subsection below instead.
     * The estimatepriority RPC estimates the priority (coin age) that a transaction needs in order to be included within a certain number of blocks as a free high-priority transaction.
     * This should not to be confused with the prioritisetransaction RPC which will remain supported for adding fee deltas to transactions.
     * @param blockNum
     * @return
     */
    @Deprecated
    double estimatePriority(int blockNum);

    /** 18.
     *  Requires wallet support.
     * The fundrawtransaction RPC adds inputs to a transaction until it has enough in value to meet its out value. This will not modify existing inputs, and will add one change output to the outputs.
     * Note that inputs which were signed may need to be resigned after completion since in/outputs have been added. The inputs added will not be signed, use signrawtransaction for that.
     * All existing inputs must have their previous output transaction be in the wallet.
     */
    //void  fundRawTranscaction();

    /** 19.
     * Requires wallet support. The generate RPC nearly instantly generates blocks. Used in regtest mode to generate an arbitrary number of blocks
     * @param numBlocks a boolean indicating if blocks must be generated with the cpu
     * @param maxtries
     * @return the list of hashes of the generated blocks
     * @throws BitcoinRpcException
     */
    List<String> generate(int numBlocks, int maxtries) throws BitcoinRpcException;

    List<String> generate(int numBlocks) throws BitcoinRpcException;

    /** 20.
     * Added in Bitcoin Core 0.13.0 Requires wallet support.
     * The generatetoaddress RPC mines blocks immediately to a specified address.
     * @param numBlocks
     * @param address
     * @param maxtries
     * @return
     * @throws BitcoinRpcException
     */
    List<String> generateToAddress(int numBlocks, String address, int maxtries) throws BitcoinRpcException;

    List<String> generateToAddress(int numBlocks, String address) throws BitcoinRpcException;


    /** 21.
     * Requires wallet support.
     * The getaccountaddress RPC returns the current Bitcoin address for receiving payments to this account. If the account doesn’t exist, it creates both the account and a new address for receiving payment.
     * Once a payment has been received to an address, future calls to this RPC for the same account will return a different address.
     * Warning: getaccountaddress will be removed in a later version of Bitcoin Core. Use the RPCs listed in the See Also subsection below instead.
     * @param account
     * @return
     * @throws BitcoinRpcException
     */
    String getAccountAddress(String account) throws BitcoinRpcException;

    /** 22.
     * Requires wallet support.
     * The getaccount RPC returns the name of the account associated with the given address.
     * @param address
     * @return
     * @throws BitcoinRpcException
     */
    String getAccount(String address) throws BitcoinRpcException;

    /** 23.
     * The getaddednodeinfo RPC returns information about the given added node, or all added nodes (except onetry nodes). Only nodes which have been manually added using the addnode RPC will have their information displayed.
     * parameter #1 Removed in Bitcoin Core 0.14.0
     * @param dummy
     * @param node
     * @return
     */
    List<NodeInfo> getAddedNodeInfo(boolean dummy, String node);

    List<NodeInfo> getAddedNodeInfo(boolean dummy);

    /** 24.
     * Requires wallet support.
     * The getaddressesbyaccount RPC returns a list of every address assigned to a particular account.
     * Warning icon Warning: getaddressesbyaccount will be removed in a later version of Bitcoin Core. Use the RPCs listed in the See Also subsection below instead.
     * @param account
     * @return
     * @throws BitcoinRpcException
     */
    List<String> getAddressesByAccount(String account) throws BitcoinRpcException;

    /** 25.
     * @param account
     * @param minConf
     * @param watchOnly
     * @return returns the balance in the account
     * @throws BitcoinRpcException
     */
    double getBalance(String account, int minConf, boolean watchOnly) throws BitcoinRpcException;

    double getBalance(String account, int minConf) throws BitcoinRpcException;

    double getBalance(String account) throws BitcoinRpcException;

    double getBalance() throws BitcoinRpcException;

    /** 26.
     * The getbestblockhash RPC returns the header hash of the most recent block on the best block chain.
     * @return
     * @throws BitcoinRpcException
     */
    String getBestBlockHash() throws BitcoinRpcException;

    /** 27.
     * The getblock RPC gets a block with a particular header hash from the local block database either as a JSON object or as a serialized block.
     * @param blockHash
     * @return
     * @throws BitcoinRpcException
     */
    Block getBlock(String blockHash) throws BitcoinRpcException;

    Block getBlock(int height) throws BitcoinRpcException;


    /** 28.
     * The getblockchaininfo RPC provides information about the current state of the block chain.
     * @return
     * @throws BitcoinRpcException
     */
    BlockChainInfo getBlockChainInfo() throws BitcoinRpcException;

    /** 29.
     * The getblockcount RPC returns the number of blocks in the local best block chain.
     * @return
     * @throws BitcoinRpcException
     */
    int getBlockCount() throws BitcoinRpcException;

    /** 30.
     * The getblockhash RPC returns the header hash of a block at the given height in the local best block chain.
     * @param height
     * @return
     * @throws BitcoinRpcException
     */
    String getBlockHash(int height) throws BitcoinRpcException;

    /** 31.
     * Added in Bitcoin Core 0.12.0
     * The getblockheader RPC gets a block header with a particular header hash from the local block database either as a JSON object or as a serialized block header.
     * @param hex
     * @param format
     * @return
     */
    String getBlockHeader(String hex, boolean format);

    String getBlockHeader(String hex);

    /** 32.
     *
     */
    //String getBlockTemplate();

    /** 33.
     *
     */
    //List<Block> getChainTips();

    /** 34.
     * The getconnectioncount RPC returns the number of connections to other nodes.
     * @return
     */
    long getConnectionCount();

    /** 35.
     *
     * @return
     */
    double getDifficulty();

    /** 36.
     * Requires wallet support.
     * The getgenerate RPC was removed in Bitcoin Core 0.13.0. If you have an older version of Bitcoin Core, use help getgenerate to get help.
     * @return
     */
    boolean getGenerate();

    /** 37.
     * Requires wallet support.
     * The gethashespersec RPC was removed in Bitcoin Core 0.11.0. If you have an older version of Bitcoin Core, use help gethashespersec to get help.
     */
    //void getHashesPerSec();

    /** 38.
     * The getinfo RPC prints various information about the node and the network.
     * Warning: getinfo will be removed in a later version of Bitcoin Core. Use the RPCs listed in the See Also subsection below instead.
     * @return infos about the bitcoind instance
     * @throws BitcoinRpcException
     */
    Info getInfo() throws BitcoinRpcException;

    /** 39.
     * Added in Bitcoin Core 0.14.0
     *The getmemoryinfo RPC returns information about memory usage.
     */
    //void getMemoryInfo();

    /** 40.
     * Added in Bitcoin Core 0.13.0
     * The getmempoolancestors RPC returns all in-mempool ancestors for a transaction in the mempool.
     */
    //void getMemPoolAncestors(String txid, boolean format);
    //void getMemPoolAncestors(String txid);

    /** 41.
     * Added in Bitcoin Core 0.13.0
     * The getmempoolancestors RPC returns all in-mempool ancestors for a transaction in the mempool.
     */
    //void getMempoolDescendants(String txid, boolean format);
    //void getMempoolDescendants(String txid);

    /** 42.
     * Added in Bitcoin Core 0.13.0
     * The getmempoolentry RPC returns mempool data for given transaction (must be in mempool).
     */
    //void getMemPoolEntry(String txid);

    /** 43.
     * The getmempoolinfo RPC returns information about the node’s current transaction
     */
    //void getMemPoolInfo();

    /** 44.
     * The getmininginfo RPC returns various mining-related information.
     * @return miningInfo about the bitcoind instance
     * @throws BitcoinRpcException
     */
    MiningInfo getMiningInfo() throws BitcoinRpcException;

    /** 45.
     * The getnettotals RPC returns information about network traffic, including bytes in, bytes out, and the current time.
     * @return
     */
    NetTotals getNetTotals() throws BitcoinRpcException;

    /** 46.
     * The getnetworkhashps RPC returns the estimated current or historical network hashes per second based on the last n blocks.
     * @return
     */
    double getNetworkHashPs();

    /** 47.
     * The getnetworkinfo RPC returns information about the node’s connection to the network.
     * @return
     * @throws BitcoinRpcException
     */
    NetworkInfo getNetworkInfo() throws BitcoinRpcException;

    /** 48.
     * The getnewaddress RPC returns a new Bitcoin address for receiving payments. If an account is specified, payments received with the address will be credited to that account.
     * @param account
     * @return
     * @throws BitcoinRpcException
     */
    String getNewAddress(String account) throws BitcoinRpcException;

    String getNewAddress() throws BitcoinRpcException;

    /** 49.
     * The getpeerinfo RPC returns data about each connected network node.
     * @return
     */
    List<PeerInfoResult> getPeerInfo();

    /** 50.
     * Requires wallet support.
     * The getrawchangeaddress RPC returns a new Bitcoin address for receiving change. This is for use with raw transactions, not normal use.
     * @return
     */
    String getRawChangeAddress();

    /** 51.
     * The getrawmempool RPC returns all transaction identifiers (TXIDs) in the memory pool as a JSON array, or detailed information about each transaction in the memory pool as a JSON object.
     * @param format
     * @return
     * @throws BitcoinRpcException
     */
    List<String> getRawMemPool(boolean format) throws BitcoinRpcException;

    List<String> getRawMemPool() throws BitcoinRpcException;

    /** 52.
     * The getrawtransaction RPC gets a hex-encoded serialized transaction or a JSON object describing the transaction.
     * By default, Bitcoin Core only stores complete transaction data for UTXOs and your own transactions, so the RPC may fail on historic transactions unless you use the non-default txindex=1 in your Bitcoin Core startup settings.
     * @param txId
     * @param format
     * @return
     * @throws BitcoinRpcException
     */
    RawTransaction getRawTransaction(String txId, boolean format) throws BitcoinRpcException;

    RawTransaction getRawTransaction(String txId) throws BitcoinRpcException;

    String getRawTransactionHex(String txId) throws BitcoinRpcException;

    /** 53.
     * Requires wallet support.
     * The getreceivedbyaccount RPC returns the total amount received by addresses in a particular account from transactions with the specified number of confirmations. It does not count coinbase transactions.
     * Warning: getreceivedbyaccount will be removed in a later version of Bitcoin Core. Use the RPCs listed in the See Also subsection below instead.
     * @param account
     * @return
     */
    BigDecimal getReceivedByAccount(String account, int minConf) throws BitcoinRpcException;
    BigDecimal getReceivedByAccount(String account) throws BitcoinRpcException;

    /** 54.
     * Requires wallet support.
     * The getreceivedbyaddress RPC returns the total amount received by the specified address in transactions with the specified number of confirmations. It does not count coinbase transactions.
     * @param address
     * @param minConf
     * @return the total amount received by &lt;bitcoinaddress&gt;
     * @throws BitcoinRpcException
     */
    double getReceivedByAddress(String address, int minConf) throws BitcoinRpcException;

    double getReceivedByAddress(String address) throws BitcoinRpcException;

    /** 55.
     * Requires wallet support.
     * The gettransaction RPC gets detailed information about an in-wallet transaction.
     * @param txid
     * @return
     */
    Transaction getTransaction(String txid);

    /** 56.
     * The gettxout RPC returns details about a transaction output. Only unspent transaction outputs (UTXOs) are guaranteed to be available.
     * @param txId
     * @param vout
     * @return
     */
    TxOut getTxOut(String txId, long vout) throws BitcoinRpcException;

    /** 57.
     *
     * @return
     */
    //void getTxOutProof(String txid);

    /** 58.
     * The gettxoutsetinfo RPC returns statistics about the confirmed unspent transaction output (UTXO) set.
     * Note that this call may take some time and that it only counts outputs from confirmed transactions—it does not count outputs from the memory pool.
     * @return
     */
    TxOutSetInfo getTxOutSetInfo();

    /** 59.
     * The getunconfirmedbalance RPC returns the wallet’s total unconfirmed balance.
     * @return
     */
    double getUnconfirmedBalance();

    /** 60.
     * Requires wallet support.
     * The getwalletinfo RPC provides information about the wallet.
     * @return
     */
    WalletInfo getWalletInfo();

    /** 61.
     * The getwork RPC was removed in Bitcoin Core 0.10.0. If you have an older version of Bitcoin Core, use help getwork to get help.
     */
    //void getWork();

    /** 62.
     * Requires wallet support.
     * The importaddress RPC adds an address or pubkey script to the wallet without the associated private key, allowing you to watch for transactions affecting that address or pubkey script without being able to spend any of its outputs.
     * @param address
     * @param label
     * @param rescan
     * @return
     * @throws BitcoinRpcException
     */
    Object importAddress(String address, String label, boolean rescan) throws BitcoinRpcException;

    /** 63.
     * Added in Bitcoin Core 0.14.0  Requires wallet support. Wallet must be unlocked.
     *The importmulti RPC imports addresses or scripts (with private keys, public keys, or P2SH redeem scripts) and optionally performs the minimum necessary rescan for all imports.
     * @param bitcoinPrivKey
     * @throws BitcoinRpcException
     */
    //void importMulti();

    /** 64.
     * Requires wallet support. Wallet must be unlocked.
     * The importprivkey RPC adds a private key to your wallet. The key should be formatted in the wallet import format created by the dumpprivkey RPC.
     * @param bitcoinPrivKey
     * @param label
     * @param rescan
     * @throws BitcoinRpcException
     */
    void importPrivKey(String bitcoinPrivKey, String label, boolean rescan) throws BitcoinRpcException;

    void importPrivKey(String bitcoinPrivKey, String label) throws BitcoinRpcException;

    void importPrivKey(String bitcoinPrivKey) throws BitcoinRpcException;

    /** 65.
     *
     */
    //void importPrunedFunds();

    /** 66.
     * Requires wallet support. Requires an unlocked wallet or an unencrypted wallet.
     * The importwallet RPC imports private keys from a file in wallet dump file format (see the dumpwallet RPC).
     * These keys will be added to the keys currently in the wallet. This call may need to rescan all or parts of the block chain for transactions affecting the newly-added keys, which may take several minutes.
     * @param filename
     */
    void importWallet(String filename);

    /** 67.
     * Requires wallet support. Requires an unlocked wallet or an unencrypted wallet.
     * The keypoolrefill RPC fills the cache of unused pre-generated keys (the keypool).
     * @param size
     */
    void keyPoolRefill(int size);

    void keyPoolRefill();

    /** 68.
     * listaccounts [minconf=1]
     *
     * @return Map that has account names as keys, account balances as values
     * @throws BitcoinRpcException
     */
    Map<String, Number> listAccounts() throws BitcoinRpcException;

    Map<String, Number> listAccounts(int minConf) throws BitcoinRpcException;

    /** 69.
     * Requires wallet support.
     * The listaddressgroupings RPC lists groups of addresses that may have had their common ownership made public by common use as inputs in the same transaction or from being used as change from a previous transaction.
     * @return
     * @throws BitcoinRpcException
     */
    //void listAddressGroupings();

    /** 70.
     *
     * @return
     * @throws BitcoinRpcException
     */
    //void listBanned();

    /** 71.
     *
     */
    //List<Unspent> listLockUnspent();

    /** 72.
     *
     */
    //void listReceivedBAccount();

    /** 73.
     * Requires wallet support.
     * The listreceivedbyaddress RPC lists the total number of bitcoins received by each address.
     * @return
     * @throws BitcoinRpcException
     */
    List<ReceivedAddress> listReceivedByAddress() throws BitcoinRpcException;

    List<ReceivedAddress> listReceivedByAddress(int minConf) throws BitcoinRpcException;

    List<ReceivedAddress> listReceivedByAddress(int minConf, boolean includeEmpty) throws BitcoinRpcException;


    /** 74.
     * Requires wallet support.
     * The listsinceblock RPC gets all transactions affecting the wallet which have occurred since a particular block, plus the header hash of a block at a particular depth.
     * @return
     * @throws BitcoinRpcException
     */
    TransactionsSinceBlock listSinceBlock() throws BitcoinRpcException;

    TransactionsSinceBlock listSinceBlock(String blockHash) throws BitcoinRpcException;

    TransactionsSinceBlock listSinceBlock(String blockHash, int targetConfirmations) throws BitcoinRpcException;

    /** 75.
     * Requires wallet support.
     * The listtransactions RPC returns the most recent transactions that affect the wallet.
     * @return
     * @throws BitcoinRpcException
     */
    List<Transaction> listTransactions() throws BitcoinRpcException;

    List<Transaction> listTransactions(String account) throws BitcoinRpcException;

    List<Transaction> listTransactions(String account, int count) throws BitcoinRpcException;

    List<Transaction> listTransactions(String account, int count, int from) throws BitcoinRpcException;

    /** 76.
     * Requires wallet support.
     * The listunspent RPC returns an array of unspent transaction outputs belonging to this wallet. Note: as of Bitcoin Core 0.10.0, outputs affecting watch-only addresses will be returned; see the spendable field in the results described below.
     * @return
     * @throws BitcoinRpcException
     */
    List<Unspent> listUnspent() throws BitcoinRpcException;

    List<Unspent> listUnspent(int minConf) throws BitcoinRpcException;

    List<Unspent> listUnspent(int minConf, int maxConf) throws BitcoinRpcException;

    List<Unspent> listUnspent(int minConf, int maxConf, String... addresses) throws BitcoinRpcException;

    /** 77.
     *
     */
    //void lockUnspent();

    /** 78.
     * Requires wallet support.
     * The move RPC moves a specified amount from one account in your wallet to another using an off-block-chain transaction.
     * Warning: move will be removed in a later version of Bitcoin Core. Use the RPCs listed in the See Also subsection below instead.
     * Warning: it's allowed to move more funds than are in an account, giving the sending account a negative balance and giving the receiving account a balance that may exceed the number of bitcoins in the wallet (or the number of bitcoins in existence).
     * @param fromAccount
     * @param toBitcoinAddress
     * @param amount
     * @return
     * @throws BitcoinRpcException
     */
    String move(String fromAccount, String toBitcoinAddress, double amount) throws BitcoinRpcException;

    String move(String fromAccount, String toBitcoinAddress, double amount, int minConf) throws BitcoinRpcException;

    String move(String fromAccount, String toBitcoinAddress, double amount, int minConf, String comment) throws BitcoinRpcException;

    /** 79.
     * The ping RPC sends a P2P ping message to all connected nodes to measure ping time. Results are provided by the getpeerinfo RPC pingtime and pingwait fields as decimal seconds. The P2P ping message is handled in a queue with all other commands, so it measures processing backlog, not just network ping.
     */
    void ping();

    /** 80.
     *
     */
    //void preciousBlock();

    /** 81.
     *
     */
    //void prioritiseTransaction();

    /** 82.
     *
     */
    //void pruneBlockChain();

    /** 83.
     *
     */
    //void removePrunedFunds();

    /** 84.
     * Will send the given amount to the given address, ensuring the account has a
     * valid balance using minConf confirmations.
     *
     * @param fromAccount
     * @param toBitcoinAddress
     * @param amount is a real and is rounded to 8 decimal places
     * @param minConf
     * @param comment
     * @param commentTo
     * @return the transaction ID if successful
     * @throws BitcoinRpcException
     */
    String sendFrom(String fromAccount, String toBitcoinAddress, double amount, int minConf, String comment, String commentTo) throws BitcoinRpcException;

    String sendFrom(String fromAccount, String toBitcoinAddress, double amount, int minConf, String comment) throws BitcoinRpcException;

    String sendFrom(String fromAccount, String toBitcoinAddress, double amount) throws BitcoinRpcException;

    String sendFrom(String fromAccount, String toBitcoinAddress, double amount, int minConf) throws BitcoinRpcException;

    /** 85.
     * Requires wallet support. Requires an unlocked wallet or an unencrypted wallet.
     * The sendmany RPC creates and broadcasts a transaction which sends outputs to multiple addresses.
     * @param fromAccount
     * @param outputs
     * @return
     * @throws BitcoinRpcException
     */
    String sendMany(String fromAccount, List<TxOutput> outputs) throws BitcoinRpcException;

    /** 86.
     * The sendrawtransaction RPC validates a transaction and broadcasts it to the peer-to-peer network.
     * @param hex
     * @return
     * @throws BitcoinRpcException
     */
    String sendRawTransaction(String hex) throws BitcoinRpcException;

    /** 87.
     * @param toAddress
     * @param amount is a real and is rounded to 8 decimal places
     * @param comment
     * @param commentTo
     * @return the transaction ID &lt;txid&gt; if successful
     * @throws BitcoinRpcException
     */
    String sendToAddress(String toAddress, double amount, String comment, String commentTo) throws BitcoinRpcException;

    String sendToAddress(String toAddress, double amount) throws BitcoinRpcException;

    String sendToAddress(String toAddress, double amount, String comment) throws BitcoinRpcException;

    /** 88.
     * Requires wallet support.
     * The setaccount RPC puts the specified address in the given account.
     * Warning: setaccount will be removed in a later version of Bitcoin Core. Use the RPCs listed in the See Also subsection below instead.
     * @param address
     * @param account
     */
    void setAccount(String address, String account);

    /** 89.
     *
     */
    //void setBan();

    /** 90.
     * @param doGenerate a boolean indicating if blocks must be generated with the cpu
     * @throws BitcoinRpcException
     */
    void setGenerate(boolean doGenerate) throws BitcoinRpcException;

    /** 91.
     *
     */
    //void setNetworkActive();

    /** 92.
     * Requires wallet support.
     * The settxfee RPC sets the transaction fee per kilobyte paid by transactions created by this wallet.
     * @param amount
     * @return
     */
    boolean setTxFee(BigDecimal amount);

    /** 93.
     * Requires wallet support. Requires an unlocked wallet or an unencrypted wallet.
     * The signmessage RPC signs a message with the private key of an address.
     * @param bitcoinAdress
     * @param message
     * @return
     */
    String signMessage(String bitcoinAdress, String message);

    /** 94.
     * Added in Bitcoin Core 0.13.0
     * The signmessagewithprivkey RPC signs a message with a given private key.
     * @param prvKey
     * @param message
     * @return
     */
    String signMessageWithPrivKey(String prvKey, String message);

    /** 95.
     * The signrawtransaction RPC signs a transaction in the serialized transaction format using private keys stored in the wallet or provided in the call.
     * @param hex
     * @param inputs
     * @param privateKeys
     * @return
     * @throws BitcoinRpcException
     */
    String signRawTransaction(String hex, List<ExtendedTxInput> inputs, List<String> privateKeys) throws BitcoinRpcException;
    String signRawTransaction1(String hex, ExtendedTxInput input, List<String> privateKeys) throws BitcoinRpcException;

    /** 96.
     *
     */
    void stop();

    /** 97.
     * The submitblock RPC accepts a block, verifies it is a valid addition to the block chain, and broadcasts it to the network. Extra parameters are ignored by Bitcoin Core but may be used by mining pools or other programs.
     * @param hexData
     */
    void submitBlock(String hexData);

    /** 98.
     * The validateaddress RPC returns information about the given Bitcoin address.
     * @param address
     * @return
     * @throws BitcoinRpcException
     */
    AddressValidationResult validateAddress(String address) throws BitcoinRpcException;

    /** 99.
     * The verifychain RPC verifies each entry in the local block chain database.
     * @return
     */
    boolean verifyChain();

    /** 100.
     * The verifymessage RPC verifies a signed message.
     * @param bitcoinAddress
     * @param signature
     * @param message
     * @return
     */
    boolean verifyMessage(String bitcoinAddress, String signature, String message);

    /** 101.
     *
     */
    //void verifyTxOutProof();

    /** 102.
     * The walletlock RPC removes the wallet encryption key from memory, locking the wallet. After calling this method, you will need to call walletpassphrase again before being able to call any methods which require the wallet to be unlocked.
     */
    //void walletLock();

    /** 103.
     * Requires wallet support. Requires an encrypted wallet.
     * The walletpassphrase RPC stores the wallet decryption key in memory for the indicated number of seconds.
     * Issuing the walletpassphrase command while the wallet is already unlocked will set a new unlock time that overrides the old one.
     * @param
     * @param
     */
    //void walletPassphrase(String passphrase,int seconds);

    /** 104.
     *
     */
    void walletPassPhrase(String passPhrase, long timeOut);

    double getEstimateFee(int nBlocks) throws BitcoinRpcException;

    double getEstimatePriority(int nBlocks) throws BitcoinRpcException;

    /**
     * In regtest mode, invalidates a block to create an orphan chain
     *
     * @param hash
     * @throws BitcoinRpcException
     */
    void invalidateBlock(String hash) throws BitcoinRpcException;

    /**
     * In regtest mode, undo the invalidation of a block, possibly making it on
     * the top of the chain
     *
     * @param hash
     * @throws BitcoinRpcException
     */
    void reconsiderBlock(String hash) throws BitcoinRpcException;
    void importPubkey(String pubkey) throws BitcoinRpcException;

}

package com.oxchains.bitcoin.rpcclient;

import com.oxchains.bitcoin.util.Base64Coder;
import com.oxchains.bitcoin.util.JSON;
import com.oxchains.bitcoin.util.ListMapWrapper;
import com.oxchains.bitcoin.util.MapWrapper;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;
import java.io.*;
import java.math.BigDecimal;
import java.net.*;
import java.nio.charset.Charset;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.oxchains.bitcoin.util.MapWrapper.*;

/**
 * @author oxchains
 * @time 2017-11-07 15:25
 * @name BaseBitcoinJSONRPCClient
 * @desc:
 */
public abstract class BaseBitcoinJSONRPCClient implements BitcoindRpcClient {
    private static final Logger LOGGER = Logger.getLogger(BaseBitcoinJSONRPCClient.class.getCanonicalName());

    public final URL rpcURL;
    private URL noAuthURL;
    private String authStr;
    /*public static final URL DEFAULT_JSONRPC_URL;
    public static final URL DEFAULT_JSONRPC_TESTNET_URL;*/

    /*static {
        String user = "user";
        String password = "pass";
        String host = "localhost";
        String port = null;

        try{
            File file = null;
            File home = new File(System.getProperty("user.home"));
            if((file = new File(home,".bitcoin"+File.separatorChar+"bitcoin.conf")).exists()){

            }else if((file = new File(home, "AppData" + File.separatorChar + "Roaming" + File.separatorChar + "Bitcoin" + File.separatorChar + "bitcoin.conf")).exists()){

            }else {

            }
            if(null != file){
                LOGGER.fine("Bitcoin configuration file found");
                Properties pro =new Properties();
                try (FileInputStream fis = new FileInputStream(file)){
                    pro.load(fis);
                }
                user = pro.getProperty("rpcuser", user);
                password = pro.getProperty("rpcpassword", password);
                host = pro.getProperty("rpcconnect", host);
                port = pro.getProperty("rpcport", port);
            }
        }catch (Exception e){
            LOGGER.log(Level.SEVERE,null,e);
        }

        try {
            DEFAULT_JSONRPC_URL = new URL("http://" + user + ':' + password + "@" + host + ":" + (port == null ? "8332" : port) + "/");
            DEFAULT_JSONRPC_TESTNET_URL = new URL("http://" + user + ':' + password + "@" + host + ":" + (port == null ? "18332" : port) + "/");
        } catch (MalformedURLException ex) {
            throw new RuntimeException(ex);
        }
    }*/

    public BaseBitcoinJSONRPCClient(String rpcUrl) throws MalformedURLException{
        this(new URL(rpcUrl));
    }
    public BaseBitcoinJSONRPCClient(URL rpc){
        this.rpcURL = rpc;
        try {
            noAuthURL = new URI(rpc.getProtocol(),null,rpc.getHost(),rpc.getPort(),rpc.getPath(),rpc.getQuery(),null).toURL();
        }catch (MalformedURLException | URISyntaxException ex){
            throw  new IllegalArgumentException(rpc.toString(),ex);
        }
        authStr = rpc.getUserInfo() == null ? null : String.valueOf(Base64Coder.encode(rpc.getUserInfo().getBytes(Charset.forName("UTF-8"))));
    }
   /* public BaseBitcoinJSONRPCClient(boolean testNet){
        this(testNet?DEFAULT_JSONRPC_TESTNET_URL:DEFAULT_JSONRPC_URL);
    }
    public BaseBitcoinJSONRPCClient(){
        this(DEFAULT_JSONRPC_URL);
    }
*/
    private HostnameVerifier hostnameVerifier = null;
    private SSLSocketFactory sslSocketFactory = null;

    public HostnameVerifier getHostnameVerifier() {
        return hostnameVerifier;
    }

    public void setHostnameVerifier(HostnameVerifier hostnameVerifier) {
        this.hostnameVerifier = hostnameVerifier;
    }

    public SSLSocketFactory getSslSocketFactory() {
        return sslSocketFactory;
    }

    public void setSslSocketFactory(SSLSocketFactory sslSocketFactory) {
        this.sslSocketFactory = sslSocketFactory;
    }

    public static final Charset QUERY_CHARSET = Charset.forName("UTF-8");//ISO8859-1
    public byte[] prepareRequest(final String method,final Object ... params){
        return JSON.stringify(new LinkedHashMap(){
            {
                put("method",method);
                put("params",params);
                put("id","1");
            }
        }).getBytes(QUERY_CHARSET);
    }

    private static byte[] loadStream(InputStream in, boolean close) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        for(;;){
            int len = in.read(buffer);
            if(len == -1){
                break;
            }
            if(len == 0){
                throw new IOException("Read timed out");
            }

            out.write(buffer,0,len);
        }
        return out.toByteArray();
    }

    public Object loadResponse(InputStream in, Object expectedID,boolean close) throws IOException, BitcoinRpcException {
        try{
            String r = new String(loadStream(in,close),QUERY_CHARSET);
            LOGGER.log(Level.FINE,"Bitcoin JSON-RPC response:\n{0}",r);
            try{
                Map response = (Map) JSON.parse(r);
                if (!expectedID.equals(response.get("id"))) {
                    throw new BitcoinRpcException("Wrong response ID (expected: " + String.valueOf(expectedID) + ", response: " + response.get("id") + ")");
                }
                if (response.get("error") != null) {
                    throw new BitcoinRpcException(JSON.stringify(response.get("error")));
                }
                return response.get("result");
            }catch (ClassCastException e){
                throw new BitcoinRpcException("Invalid server response format (data: \"" + r + "\")");
            }

        }finally {
            if(close){
                in.close();
            }
        }
    }

    public Object query(String method, Object... o) throws BitcoinRpcException {
        HttpURLConnection conn;
        try {
            conn = (HttpURLConnection) noAuthURL.openConnection();

            conn.setDoOutput(true);
            conn.setDoInput(true);

            if (conn instanceof HttpsURLConnection) {
                if (hostnameVerifier != null) {
                    ((HttpsURLConnection) conn).setHostnameVerifier(hostnameVerifier);
                }
                if (sslSocketFactory != null) {
                    ((HttpsURLConnection) conn).setSSLSocketFactory(sslSocketFactory);
                }
            }

            ((HttpURLConnection) conn).setRequestProperty("Authorization", "Basic " + authStr);
            byte[] r = prepareRequest(method, o);
            LOGGER.log(Level.FINE, "Bitcoin JSON-RPC request:\n{0}", new String(r, QUERY_CHARSET));
            conn.getOutputStream().write(r);
            conn.getOutputStream().close();
            int responseCode = conn.getResponseCode();
            if (responseCode != 200) {
                throw new BitcoinRpcException(method + "===>" + Arrays.deepToString(o) + "--->" + responseCode + "--->" + conn.getResponseMessage() + "--->" + new String(loadStream(conn.getErrorStream(), true)));
            }
            return loadResponse(conn.getInputStream(), "1", true);
        } catch (IOException ex) {
            throw new BitcoinRpcException(method+"===>"+ Arrays.deepToString(o), ex);
        }
    }



    protected class InfoWrapper extends MapWrapper implements Info, Serializable {

        public InfoWrapper(Map m) {
            super(m);
        }

        @Override
        public double balance() {
            return mapDouble("balance");
        }

        @Override
        public int blocks() {
            return mapInt("blocks");
        }

        @Override
        public int connections() {
            return mapInt("connections");
        }

        @Override
        public double difficulty() {
            return mapDouble("difficulty");
        }

        @Override
        public String errors() {
            return mapStr("errors");
        }

        @Override
        public long keyPoolOldest() {
            return mapLong("keypoololdest");
        }

        @Override
        public long keyPoolSize() {
            return mapLong("keypoolsize");
        }

        @Override
        public double payTxFee() {
            return mapDouble("paytxfee");
        }

        @Override
        public long protocolVersion() {
            return mapLong("protocolversion");
        }

        @Override
        public String proxy() {
            return mapStr("proxy");
        }

        @Override
        public double relayFee() {
            return mapDouble("relayfee");
        }

        @Override
        public boolean testnet() {
            return mapBool("testnet");
        }

        @Override
        public int timeOffset() {
            return mapInt("timeoffset");
        }

        @Override
        public long version() {
            return mapLong("version");
        }

        @Override
        public long walletVersion() {
            return mapLong("walletversion");
        }

    }

    protected class TxOutSetInfoWrapper extends MapWrapper implements TxOutSetInfo, Serializable {

        public TxOutSetInfoWrapper(Map m) {
            super(m);
        }

        @Override
        public long height() {
            return mapInt("height");
        }

        @Override
        public String bestBlock() {
            return mapStr("bestBlock");
        }

        @Override
        public long transactions() {
            return mapInt("transactions");
        }

        @Override
        public long txouts() {
            return mapInt("txouts");
        }

        @Override
        public long bytesSerialized() {
            return mapInt("bytes_serialized");
        }

        @Override
        public String hashSerialized() {
            return mapStr("hash_serialized");
        }

        @Override
        public BigDecimal totalAmount() {
            return mapBigDecimal("total_amount");
        }
    }

    protected class WalletInfoWrapper extends MapWrapper implements WalletInfo, Serializable {

        public WalletInfoWrapper(Map m) { super(m);}

        @Override
        public long walletVersion() {
            return mapLong("walletversion");
        }

        @Override
        public BigDecimal balance() {
            return mapBigDecimal("balance");
        }

        @Override
        public BigDecimal unconfirmedBalance() {
            return mapBigDecimal("unconfirmed_balance");
        }

        @Override
        public BigDecimal immatureBalance() {
            return mapBigDecimal("immature_balance");
        }

        @Override
        public long txCount() {
            return mapLong("txcount");
        }

        @Override
        public long keyPoolOldest() {
            return mapLong("keypoololdest");
        }

        @Override
        public long keyPoolSize() {
            return mapLong("keypoolsize");
        }

        @Override
        public long unlockedUntil() {
            return mapLong("unlocked_until");
        }

        @Override
        public BigDecimal payTxFee() {
            return mapBigDecimal("paytxfee");
        }

        @Override
        public String hdMasterKeyId() {
            return mapStr("hdmasterkeyid");
        }
    }

    protected class NetworkInfoWrapper extends MapWrapper implements NetworkInfo, Serializable {

        public NetworkInfoWrapper(Map m) {
            super(m);
        }

        @Override
        public long version() {
            return mapLong("version");
        }

        @Override
        public String subversion() {
            return mapStr("subversion");
        }

        @Override
        public long protocolVersion() {
            return mapLong("protocolversion");
        }

        @Override
        public String localServices() {
            return mapStr("localservices");
        }

        @Override
        public boolean localRelay() {
            return mapBool("localrelay");
        }

        @Override
        public long timeOffset() {
            return mapLong("timeoffset");
        }

        @Override
        public long connections() {
            return mapLong("connections");
        }

        @Override
        public List<Network> networks() {
            List<Map> maps = (List<Map>) m.get("networks");
            List<Network> networks = new LinkedList<Network>();
            for(Map m: maps) {
                Network net = new NetworkWrapper(m);
                networks.add(net);
            }
            return networks;
        }

        @Override
        public BigDecimal relayFee() {
            return mapBigDecimal("relayfee");
        }

        @Override
        public List<String> localAddresses() {
            return (List<String>) m.get("localaddresses");
        }

        @Override
        public String warnings() {
            return mapStr("warnings");
        }
    }

    protected class NetworkWrapper extends MapWrapper implements Network, Serializable {

        public NetworkWrapper(Map m) {
            super(m);
        }

        @Override
        public String name() {
            return mapStr("name");
        }

        @Override
        public boolean limited() {
            return mapBool("limited");
        }

        @Override
        public boolean reachable() {
            return mapBool("reachable");
        }

        @Override
        public String proxy() {
            return mapStr("proxy");
        }

        @Override
        public boolean proxyRandomizeCredentials() {
            return mapBool("proxy_randomize_credentials");
        }
    }

    protected class MultiSigWrapper extends MapWrapper implements MultiSig, Serializable {

        public MultiSigWrapper(Map m) { super(m);}


        @Override
        public String address() {
            return mapStr("address");
        }

        @Override
        public String redeemScript() {
            return mapStr("redeemScript");
        }
    }

    protected class NodeInfoWrapper extends MapWrapper implements NodeInfo, Serializable {

        public NodeInfoWrapper(Map m) { super(m); }

        @Override
        public String addedNode() {
            return mapStr("addednode");
        }

        @Override
        public boolean connected() {
            return mapBool("connected");
        }

        @Override
        public List<Address> addresses() {
            List<Map> maps = (List<Map>) m.get("addresses");
            List<Address> addresses = new LinkedList<Address>();
            for(Map m: maps) {
                Address add = new AddressWrapper(m);
                addresses.add(add);
            }
            return addresses;
        }
    }

    protected class AddressWrapper extends MapWrapper implements Address, Serializable {

        public AddressWrapper(Map m) { super(m); }

        @Override
        public String address() {
            return mapStr("address");
        }

        @Override
        public String connected() {
            return mapStr("connected");
        }
    }

    protected class TxOutWrapper extends MapWrapper implements TxOut, Serializable {

        public TxOutWrapper(Map m) {
            super(m);
        }

        @Override
        public String bestBlock() {
            return mapStr("bestblock");
        }

        @Override
        public long confirmations() {
            return mapLong("confirmations");
        }

        @Override
        public BigDecimal value() {
            return mapBigDecimal("value");
        }

        @Override
        public String asm() {
            return mapStr("asm");
        }

        @Override
        public String hex() {
            return mapStr("hex");
        }

        @Override
        public long reqSigs() {
            return mapLong("reqSigs");
        }

        @Override
        public String type() {
            return mapStr("type");
        }

        @Override
        public List<String> addresses() {
            return (List<String>) m.get("addresses");
        }

        @Override
        public long version() {
            return mapLong("version");
        }

        @Override
        public boolean coinBase() {
            return mapBool("coinbase");
        }
    }

    protected class MiningInfoWrapper extends MapWrapper implements MiningInfo, Serializable {

        public MiningInfoWrapper(Map m) {
            super(m);
        }

        @Override
        public int blocks() {
            return mapInt("blocks");
        }

        @Override
        public int currentBlockSize() {
            return mapInt("currentblocksize");
        }

        @Override
        public int currentBlockWeight() {
            return mapInt("currentblockweight");
        }

        @Override
        public int currentBlockTx() {
            return mapInt("currentblocktx");
        }

        @Override
        public double difficulty() {
            return mapDouble("difficulty");
        }

        @Override
        public String errors() {
            return mapStr("errors");
        }

        @Override
        public double networkHashps() {
            return Double.valueOf(mapStr("networkhashps"));
        }

        @Override
        public int pooledTx() {
            return mapInt("pooledtx");
        }

        @Override
        public boolean testNet() {
            return mapBool("testnet");
        }

        @Override
        public String chain() {
            return mapStr("chain");
        }
    }

    protected class BlockChainInfoMapWrapper extends MapWrapper implements BlockChainInfo, Serializable {

        public BlockChainInfoMapWrapper(Map m) {
            super(m);
        }

        @Override
        public String chain() {
            return mapStr("chain");
        }

        @Override
        public int blocks() {
            return mapInt("blocks");
        }

        @Override
        public String bestBlockHash() {
            return mapStr("bestblockhash");
        }

        @Override
        public double difficulty() {
            return mapDouble("difficulty");
        }

        @Override
        public double verificationProgress() {
            return mapDouble("verificationprogress");
        }

        @Override
        public String chainWork() {
            return mapStr("chainwork");
        }
    }

    protected class BlockMapWrapper extends MapWrapper implements Block, Serializable {

        public BlockMapWrapper(Map m) {
            super(m);
        }

        @Override
        public String hash() {
            return mapStr("hash");
        }

        @Override
        public int confirmations() {
            return mapInt("confirmations");
        }

        @Override
        public int size() {
            return mapInt("size");
        }

        @Override
        public int height() {
            return mapInt("height");
        }

        @Override
        public int version() {
            return mapInt("version");
        }

        @Override
        public String merkleRoot() {
            return mapStr("merkleroot");
        }

        @Override
        public String chainwork() {
            return mapStr("chainwork");
        }

        @Override
        public List<String> tx() {
            return (List<String>) m.get("tx");
        }

        @Override
        public Date time() {
            return mapCTime("time");
        }

        @Override
        public long nonce() {
            return mapLong("nonce");
        }

        @Override
        public String bits() {
            return mapStr("bits");
        }

        @Override
        public double difficulty() {
            return mapDouble("difficulty");
        }

        @Override
        public String previousHash() {
            return mapStr("previousblockhash");
        }

        @Override
        public String nextHash() {
            return mapStr("nextblockhash");
        }

        @Override
        public Block previous() throws BitcoinRpcException {
            if (!m.containsKey("previousblockhash"))
                return null;
            return getBlock(previousHash());
        }

        @Override
        public Block next() throws BitcoinRpcException {
            if (!m.containsKey("nextblockhash"))
                return null;
            return getBlock(nextHash());
        }

    }

    protected class PeerInfoWrapper extends MapWrapper implements PeerInfoResult, Serializable {

        public PeerInfoWrapper(Map m) {
            super(m);
        }

        @Override
        public long getId() {
            return mapLong("id");
        }

        @Override
        public String getAddr() {
            return mapStr("addr");
        }

        @Override
        public String getAddrLocal() {
            return mapStr("addrlocal");
        }

        @Override
        public String getServices() {
            return mapStr("services");
        }

        @Override
        public long getLastSend() {
            return mapLong("lastsend");
        }

        @Override
        public long getLastRecv() {
            return mapLong("lastrecv");
        }

        @Override
        public long getBytesSent() {
            return mapLong("bytessent");
        }

        @Override
        public long getBytesRecv() {
            return mapLong("bytesrecv");
        }

        @Override
        public long getConnTime() {
            return mapLong("conntime");
        }

        @Override
        public int getTimeOffset() {
            return mapInt("timeoffset");
        }

        @Override
        public double getPingTime() {
            return mapDouble("pingtime");
        }

        @Override
        public long getVersion() {
            return mapLong("version");
        }

        @Override
        public String getSubVer() {
            return mapStr("subver");
        }

        @Override
        public boolean isInbound() {
            return mapBool("inbound");
        }

        @Override
        public int getStartingHeight() {
            return mapInt("startingheight");
        }

        @Override
        public long getBanScore() {
            return mapLong("banscore");
        }

        @Override
        public int getSyncedHeaders() {
            return mapInt("synced_headers");
        }

        @Override
        public int getSyncedBlocks() {
            return mapInt("synced_blocks");
        }

        @Override
        public boolean isWhiteListed() {
            return mapBool("whitelisted");
        }

    }

    protected static class ReceivedAddressListWrapper extends AbstractList<ReceivedAddress> {

        private final List<Map<String, Object>> wrappedList;

        public ReceivedAddressListWrapper(List<Map<String, Object>> wrappedList) {
            this.wrappedList = wrappedList;
        }

        @Override
        public ReceivedAddress get(int index) {
            final Map<String, Object> e = wrappedList.get(index);
            return new ReceivedAddress() {

                @Override
                public String address() {
                    return (String) e.get("address");
                }

                @Override
                public String account() {
                    return (String) e.get("account");
                }

                @Override
                public double amount() {
                    return ((Number) e.get("amount")).doubleValue();
                }

                @Override
                public int confirmations() {
                    return ((Number) e.get("confirmations")).intValue();
                }

                @Override
                public String toString() {
                    return e.toString();
                }

            };
        }

        @Override
        public int size() {
            return wrappedList.size();
        }
    }

    protected class TransactionListMapWrapper extends ListMapWrapper<Transaction> {

        public TransactionListMapWrapper(List<Map> list) {
            super(list);
        }

        @Override
        protected Transaction wrap(final Map m) {
            return new Transaction() {

                @Override
                public String account() {
                    return mapStr(m, "account");
                }

                @Override
                public String address() {
                    return mapStr(m, "address");
                }

                @Override
                public String category() {
                    return mapStr(m, "category");
                }

                @Override
                public double amount() {
                    return mapDouble(m, "amount");
                }

                @Override
                public double fee() {
                    return mapDouble(m, "fee");
                }

                @Override
                public int confirmations() {
                    return mapInt(m, "confirmations");
                }

                @Override
                public String blockHash() {
                    return mapStr(m, "blockhash");
                }

                @Override
                public int blockIndex() {
                    return mapInt(m, "blockindex");
                }

                @Override
                public Date blockTime() {
                    return mapCTime(m, "blocktime");
                }

                @Override
                public String txId() {
                    return mapStr(m, "txid");
                }

                @Override
                public Date time() {
                    return mapCTime(m, "time");
                }

                @Override
                public Date timeReceived() {
                    return mapCTime(m, "timereceived");
                }

                @Override
                public String comment() {
                    return mapStr(m, "comment");
                }

                @Override
                public String commentTo() {
                    return mapStr(m, "to");
                }

                private RawTransaction raw = null;

                @Override
                public RawTransaction raw() {
                    if (raw == null)
                        try {
                            raw = getRawTransaction(txId());
                        } catch (BitcoinRpcException ex) {
                            throw new RuntimeException(ex);
                        }
                    return raw;
                }

                @Override
                public String toString() {
                    return m.toString();
                }

            };
        }

    }

    protected class UnspentListWrapper extends ListMapWrapper<Unspent> {

        public UnspentListWrapper(List<Map> list) {
            super(list);
        }

        @Override
        protected Unspent wrap(final Map m) {
            return new Unspent() {

                @Override
                public String txid() {
                    return mapStr(m, "txid");
                }

                @Override
                public int vout() {
                    return mapInt(m, "vout");
                }

                @Override
                public String address() {
                    return mapStr(m, "address");
                }

                @Override
                public String scriptPubKey() {
                    return mapStr(m, "scriptPubKey");
                }

                @Override
                public String account() {
                    return mapStr(m, "account");
                }

                @Override
                public double amount() {
                    return mapDouble(m, "amount");
                }

                @Override
                public int confirmations() {
                    return mapInt(m, "confirmations");
                }

            };
        }
    }



    public class DecodedScriptImpl extends MapWrapper implements DecodedScript, Serializable {

        public DecodedScriptImpl(Map m) {
            super(m);
        }


        @Override
        public String asm() {
            return mapStr("asm");
        }

        @Override
        public String hex() {
            return mapStr("hex");
        }

        @Override
        public String type() {
            return mapStr("type");
        }

        @Override
        public int reqSigs() {
            return mapInt("reqSigs");
        }

        @Override
        public List<String> addresses() {
            return (List) m.get("addresses");
        }

        @Override
        public String p2sh() {
            return mapStr("p2sh");
        }
    }

    public class NetTotalsImpl extends MapWrapper implements NetTotals, Serializable {

        public NetTotalsImpl(Map m) {
            super(m);
        }

        @Override
        public long totalBytesRecv() {
            return mapLong("totalbytesrecv");
        }

        @Override
        public long totalBytesSent() {
            return mapLong("totalbytessent");
        }

        @Override
        public long timeMillis() {
            return mapLong("timemillis");
        }

        public class uploadTargetImpl extends MapWrapper implements uploadTarget, Serializable {

            public uploadTargetImpl(Map m) {
                super(m);
            }


            @Override
            public long timeFrame() {
                return mapLong("timeframe");
            }

            @Override
            public int target() {
                return mapInt("target");
            }

            @Override
            public boolean targetReached() {
                return mapBool("targetreached");
            }

            @Override
            public boolean serveHistoricalBlocks() {
                return mapBool("servehistoricalblocks");
            }

            @Override
            public long bytesLeftInCycle() {
                return mapLong("bytesleftincycle");
            }

            @Override
            public long timeLeftInCycle() {
                return mapLong("timeleftincycle");
            }
        }

        @Override
        public NetTotals.uploadTarget uploadTarget() {
            return new uploadTargetImpl((Map) m.get("uploadtarget"));
        }
    }

    public class RawTransactionImpl extends MapWrapper implements RawTransaction, Serializable {

        public RawTransactionImpl(Map<String, Object> tx) {
            super(tx);
        }

        @Override
        public String hex() {
            return mapStr("hex");
        }

        @Override
        public String txId() {
            return mapStr("txid");
        }

        @Override
        public int version() {
            return mapInt("version");
        }

        @Override
        public long lockTime() {
            return mapLong("locktime");
        }

        @Override
        public String hash() {
            return mapStr("hash");
        }

        @Override
        public long size() {
            return mapLong("size");
        }

        @Override
        public long vsize() {
            return mapLong("vsize");
        }

        private class InImpl extends MapWrapper implements In, Serializable {

            public InImpl(Map m) {
                super(m);
            }

            @Override
            public String txid() {
                return mapStr("txid");
            }

            @Override
            public int vout() {
                return mapInt("vout");
            }

            @Override
            public Map<String, Object> scriptSig() {
                return (Map) m.get("scriptSig");
            }

            @Override
            public long sequence() {
                return mapLong("sequence");
            }

            @Override
            public RawTransaction getTransaction() {
                try {
                    return getRawTransaction(mapStr("txid"));
                } catch (BitcoinRpcException ex) {
                    throw new RuntimeException(ex);
                }
            }

            @Override
            public Out getTransactionOutput() {
                return getTransaction().vOut().get(mapInt("vout"));
            }

            @Override
            public String scriptPubKey() {
                return mapStr("scriptPubKey");
            }

        }

        @Override
        public List<In> vIn() {
            final List<Map<String, Object>> vIn = (List<Map<String, Object>>) m.get("vin");
            return new AbstractList<In>() {

                @Override
                public In get(int index) {
                    return new InImpl(vIn.get(index));
                }

                @Override
                public int size() {
                    return vIn.size();
                }
            };
        }

        private class OutImpl extends MapWrapper implements Out, Serializable {

            public OutImpl(Map m) {
                super(m);
            }

            @Override
            public double value() {
                return mapDouble("value");
            }

            @Override
            public int n() {
                return mapInt("n");
            }

            private class ScriptPubKeyImpl extends MapWrapper implements ScriptPubKey, Serializable {

                public ScriptPubKeyImpl(Map m) {
                    super(m);
                }

                @Override
                public String asm() {
                    return mapStr("asm");
                }

                @Override
                public String hex() {
                    return mapStr("hex");
                }

                @Override
                public int reqSigs() {
                    return mapInt("reqSigs");
                }

                @Override
                public String type() {
                    return mapStr("type");
                }

                @Override
                public List<String> addresses() {
                    return (List) m.get("addresses");
                }

            }

            @Override
            public ScriptPubKey scriptPubKey() {
                return new ScriptPubKeyImpl((Map) m.get("scriptPubKey"));
            }

            @Override
            public TxInput toInput() {
                return new BasicTxInput(transaction().txId(), n());
            }

            @Override
            public RawTransaction transaction() {
                return RawTransactionImpl.this;
            }

        }

        @Override
        public List<Out> vOut() {
            final List<Map<String, Object>> vOut = (List<Map<String, Object>>) m.get("vout");
            return new AbstractList<Out>() {

                @Override
                public Out get(int index) {
                    return new OutImpl(vOut.get(index));
                }

                @Override
                public int size() {
                    return vOut.size();
                }
            };
        }

        @Override
        public String blockHash() {
            return mapStr("blockhash");
        }

        @Override
        public int confirmations() {
            return mapInt("confirmations");
        }

        @Override
        public Date time() {
            return mapCTime("time");
        }

        @Override
        public Date blocktime() {
            return mapCTime("blocktime");
        }

    }

    protected class TransactionsSinceBlockImpl implements TransactionsSinceBlock, Serializable {

        public final List<Transaction> transactions;
        public final String lastBlock;

        public TransactionsSinceBlockImpl(Map r) {
            this.transactions = new TransactionListMapWrapper((List) r.get("transactions"));
            this.lastBlock = (String) r.get("lastblock");
        }

        @Override
        public List<Transaction> transactions() {
            return transactions;
        }

        @Override
        public String lastBlock() {
            return lastBlock;
        }

    }





}

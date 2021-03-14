package com.cnft;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.admin.Admin;
import org.web3j.protocol.http.HttpService;

/**
 * Created by shizhiguo on 2018/1/17
 */
public class EthClient {
    private volatile static Web3j web3j = null;
    private volatile static Admin admin = null;
//    private static String RPC_URL = "https://ropsten.infura.io/v3/31cdf53e7ebf4f0490fa4039d6358e40";
    private static String RPC_URL = "http://172.20.10.8:8545";
    private static String RPC_URL_MAINNET = "https://mainnet.infura.io/v3/31cdf53e7ebf4f0490fa4039d6358e40";

    public static Web3j getWeb3jClient() {
        if (web3j == null) {
            synchronized (EthClient.class) {
                if (web3j == null) {
                    web3j = Web3j.build(new HttpService(RPC_URL));
                }
            }
        }
        return web3j;
    }

    public static Web3j getWeb3jClient_MainNet() {
        if (web3j == null) {
            synchronized (EthClient.class) {
                if (web3j == null) {
                    web3j = Web3j.build(new HttpService(RPC_URL_MAINNET));
                }
            }
        }
        return web3j;
    }


}

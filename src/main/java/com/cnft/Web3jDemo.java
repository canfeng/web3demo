package com.cnft;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import org.web3j.protocol.http.HttpService;

import java.io.IOException;

public class Web3jDemo {
    public static void main(String args[]) throws Exception{
        System.out.println("test");
        testWeb3();
    }

    public static void testWeb3() throws IOException {
        Web3j web3 = Web3j.build(new HttpService("https://ropsten.infura.io/v3/31cdf53e7ebf4f0490fa4039d6358e40"));  // defaults to http://localhost:8545/
        Web3ClientVersion web3ClientVersion = web3.web3ClientVersion().send();
        String clientVersion = web3ClientVersion.getWeb3ClientVersion();
        System.out.println(clientVersion);
    }
}

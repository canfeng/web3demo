package com.cnft;

import org.junit.Test;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.Request;
import org.web3j.protocol.core.methods.response.EthSendTransaction;

public class EthClientTest {

    @Test
    public void ethSendRawTransaction() {
        Web3j web3jClient = EthClient.getWeb3jClient();
        String signedData = "";
        Request<?, EthSendTransaction> ethSendTransactionRequest = web3jClient.ethSendRawTransaction(signedData);
    }
}
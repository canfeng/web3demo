package com.cnft.contract.com.cnft.blockchain.model;

import com.cnft.EthClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.reactivex.Flowable;
import io.reactivex.disposables.Disposable;
import org.junit.Assert;
import org.junit.Test;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.Transaction;
import org.web3j.tx.ReadonlyTransactionManager;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.DefaultGasProvider;

import java.util.Arrays;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class OwnerTest {
    private static final int EVENT_COUNT = 5;
    private static final int TIMEOUT_MINUTES = 1;


    @Test
    public void name() {

    }

    @Test
    public void changeOwner() {
    }

    @Test
    public void getOwner() throws Exception {
        String contractAddr = "0xF8652d733ed090014e256fA3300e32F63bEa5E47";
        Web3j web3jClient = EthClient.getWeb3jClient();
        String fromAddr = "0xf82f96aeD5F343545CaCc393F33BFE8E006f6928";
        TransactionManager transactionManager = new ReadonlyTransactionManager(web3jClient, fromAddr);
        Owner owner = Owner.load(contractAddr, web3jClient, transactionManager, new DefaultGasProvider());
        RemoteFunctionCall<String> ownerIns = owner.getOwner();
        String result = ownerIns.send();
        System.out.println(result);
    }

    @Test
    public void transactionFlowable() {
        Web3j web3jClient = EthClient.getWeb3jClient();
        Flowable<Transaction> transactionFlowable = web3jClient.transactionFlowable();
        transactionFlowable.blockingForEach(transaction -> {
            System.out.println("收到一笔交易==>" + transaction.getHash());
            System.out.println(new ObjectMapper().writeValueAsString(transaction));
        });
    }

    @Test
    public void logFlowable() throws JsonProcessingException {
        Web3j web3jClient = EthClient.getWeb3jClient_MainNet();
        EthFilter ethFilter = new EthFilter(
                DefaultBlockParameterName.EARLIEST,
                DefaultBlockParameterName.LATEST,
                Arrays.asList("0xdAC17F958D2ee523a2206206994597C13D831ec7")
        );
        Event event = new Event("Transfer", Arrays.asList(
                new TypeReference<Address>() {
                }, new TypeReference<Address>() {
                }, new TypeReference<Uint256>() {
                })
        );
        String encode = EventEncoder.encode(event);
        ethFilter.addSingleTopic(encode);
        Flowable<Log> logFlowable = web3jClient.ethLogFlowable(ethFilter);
        Iterable<Log> logIterable = logFlowable.blockingLatest();
        while (logIterable.iterator().hasNext()) {
            Log log = logIterable.iterator().next();
            System.out.println("收到一条事件==>" + log.getTransactionHash());
            System.out.println(new ObjectMapper().writeValueAsString(log));
        }
    }

    @Test
    public void logFlowable2() throws Exception {
        Web3j web3jClient = EthClient.getWeb3jClient_MainNet();
        EthFilter ethFilter = new EthFilter(
                DefaultBlockParameterName.EARLIEST,
                DefaultBlockParameterName.LATEST,
                Arrays.asList("0xdAC17F958D2ee523a2206206994597C13D831ec7")
        );
        Event event = new Event("Transfer", Arrays.asList(
                new TypeReference<Address>() {
                }, new TypeReference<Address>() {
                }, new TypeReference<Uint256>() {
                })
        );
        String encode = EventEncoder.encode(event);
        ethFilter.addSingleTopic(encode);
        Flowable<Log> logFlowable = web3jClient.ethLogFlowable(ethFilter);
        run(logFlowable);
    }

    private <T> void run(Flowable<T> flowable) throws Exception {
        CountDownLatch countDownLatch = new CountDownLatch(EVENT_COUNT);
        CountDownLatch completedLatch = new CountDownLatch(EVENT_COUNT);

        Disposable subscription =
                flowable.subscribe(
                        x -> countDownLatch.countDown(),
                        Throwable::printStackTrace,
                        completedLatch::countDown);

        countDownLatch.await(TIMEOUT_MINUTES, TimeUnit.MINUTES);
        subscription.dispose();
        completedLatch.await(1, TimeUnit.SECONDS);

        System.out.println(
                "CountDownLatch=" + countDownLatch.getCount() + ", CompletedLatch=" + completedLatch.getCount());
        Assert.assertTrue(subscription.isDisposed());
    }
}
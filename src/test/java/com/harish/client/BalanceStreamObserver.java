package com.harish.client;

import com.harish.models.Balance;
import com.harish.server.CashStreamingRequest;
import io.grpc.stub.StreamObserver;

import java.util.concurrent.CountDownLatch;

public class BalanceStreamObserver implements StreamObserver<Balance> {
private CountDownLatch countDownLatch;

    public BalanceStreamObserver(CountDownLatch countDownLatch) {
    }

    @Override
    public void onNext(Balance balance) {
        System.out.println("Final Balance"+balance);
        this.countDownLatch.countDown();
    }

    @Override
    public void onError(Throwable t) {

    }

    @Override
    public void onCompleted() {
        System.out.println("server is done from client side");
        this.countDownLatch.countDown();
    }
}

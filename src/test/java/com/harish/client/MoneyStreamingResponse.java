package com.harish.client;

import com.harish.models.Money;
import io.grpc.stub.StreamObserver;

import java.util.concurrent.CountDownLatch;

public class MoneyStreamingResponse implements StreamObserver<Money> {
    private CountDownLatch countDownLatch;

    public MoneyStreamingResponse(CountDownLatch countDownLatch) {
        this.countDownLatch = countDownLatch;
    }

    @Override
    public void onNext(Money value) {
        System.out.println("Received Async"+value.getValue());
    }

    @Override
    public void onError(Throwable t) {
        System.out.println(t.getMessage());
        countDownLatch.countDown();
    }

    @Override
    public void onCompleted() {
        System.out.println("server is done");
        countDownLatch.countDown();
    }
}

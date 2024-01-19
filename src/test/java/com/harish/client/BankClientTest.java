package com.harish.client;

import com.google.common.util.concurrent.Uninterruptibles;
import com.harish.models.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BankClientTest {
    BankServiceGrpc.BankServiceBlockingStub blockingStub;
    BankServiceGrpc.BankServiceStub bankServiceStub;
    @BeforeAll
    public void setup(){
        ManagedChannel managedChannel = ManagedChannelBuilder
                .forAddress("localhost", 4445).usePlaintext().build();
         blockingStub = BankServiceGrpc.newBlockingStub(managedChannel);
         bankServiceStub=BankServiceGrpc.newStub(managedChannel);
    }

    @Test
    public void balanceTest(){
        BalanceCheckRequest balanceCheckRequest = BalanceCheckRequest.newBuilder().setAccountNumber(5).build();
        Balance balance = this.blockingStub.getBalance(balanceCheckRequest);
        System.out.println(
                balance.getAmount()
        );
    }
    @Test
    public void withdrawTest(){
        WithdrawRequest withdrawRequest= WithdrawRequest.newBuilder().setAccountNumber(7).setAmount(40).build();
        this.blockingStub.withdraw(withdrawRequest).forEachRemaining(money -> System.out.println("Receive money"+money.getValue()));
    }
    @Test
    public void withdrawAsyncTest() throws InterruptedException {
        CountDownLatch countDownLatch=new CountDownLatch(1);
        WithdrawRequest withdrawRequest=WithdrawRequest.newBuilder().setAccountNumber(10).setAmount(50).build();
        this.bankServiceStub.withdraw(withdrawRequest,new MoneyStreamingResponse(countDownLatch));
        countDownLatch.await();
      /*  Uninterruptibles.sleepUninterruptibly(3, TimeUnit.SECONDS);*/
    }
    @Test
    public void cashStreamingRequest() throws InterruptedException {
CountDownLatch countDownLatch=new CountDownLatch(1);
StreamObserver<DepositRequest> streamObserver = this.bankServiceStub.cashDeposit(new BalanceStreamObserver(countDownLatch));
        for (int i = 0; i <10 ; i++) {
            DepositRequest depositRequest = DepositRequest.newBuilder().setAccountNumber(8).setAmount(10).build();
        streamObserver.onNext(depositRequest);
        }
        streamObserver.onCompleted();
        countDownLatch.await();
    }

}

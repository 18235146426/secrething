package com.secrething.test;

import com.secrething.rpc.factory.RemoteServiceFactory;
import com.secrething.rpc.remote.NettyClient;

import java.util.concurrent.TimeUnit;

/**
 * Created by Idroton on 2018/8/17 10:58 PM.
 */
public class Consumer {
    public static void main(String[] args) throws InterruptedException {
        NettyClient client = new NettyClient("127.0.0.1",9999);

        HelloService helloService = RemoteServiceFactory.getJDKProxyInstance(HelloService.class,"hello",client);

        for (int i = 0; i < 5 ; i++) {
            System.out.println(helloService.hello("zhangsan"+i));
            TimeUnit.SECONDS.sleep(1);
        }
    }
}
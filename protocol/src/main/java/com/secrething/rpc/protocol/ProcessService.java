package com.secrething.rpc.protocol;

/**
 * Created by Idroton on 2018/8/11.
 */
public interface ProcessService<R,S> {
    S process(R inputMsg);
}

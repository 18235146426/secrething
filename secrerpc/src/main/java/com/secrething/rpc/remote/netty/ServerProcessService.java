package com.secrething.rpc.remote.netty;

import com.secrething.rpc.core.RemoteRequest;
import com.secrething.rpc.core.RemoteResponse;
import com.secrething.rpc.protocol.ProcessService;
import com.secrething.rpc.registry.Invoker;
import com.secrething.rpc.registry.ServiceLocalStorage;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by Idroton on 2018/8/17 10:37 PM.
 */
@Slf4j
public class ServerProcessService implements ProcessService<RemoteRequest, RemoteResponse> {
    @Override
    public RemoteResponse process(RemoteRequest inputMsg) {
        RemoteResponse response = new RemoteResponse();
        response.setId(inputMsg.getId());
        try {
            Object result = invoke(inputMsg);
            response.setResult(result);
        } catch (Exception e) {
            log.error("request invoke fail", e);
            response.setError("error");
            response.setThrowable(e);
        }
        return response;
    }

    private Object invoke(RemoteRequest request) throws Exception {
        Invoker invoker = ServiceLocalStorage.borrow(request.getBeanName());
        if (null == invoker)
            invoker = ServiceLocalStorage.borrow(request.getClzzName());
        if (null == invoker) {
            throw new Exception("service class not found");
        }
        return invoker.invoke(request.getMethodName(),request.getParameterTypes(),request.getParameters());
    }
}

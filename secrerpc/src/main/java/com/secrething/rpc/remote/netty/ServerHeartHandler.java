package com.secrething.rpc.remote.netty;

import com.secrething.rpc.core.RemoteRequest;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Idroton on 2018/8/11.
 * heart beat check client's channel is active
 */
public class ServerHeartHandler extends SimpleChannelInboundHandler<RemoteRequest> {
    private static final Logger logger = LoggerFactory.getLogger(ServerHeartHandler.class);
    private final int maxTimes;
    private int times = 0;

    public ServerHeartHandler(int maxTimeoutTimes) {
        this.maxTimes = maxTimeoutTimes;
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, RemoteRequest in) throws Exception {
        times = 0;
        ctx.fireChannelRead(in);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.READER_IDLE) {
                if (times < maxTimes) {
                    times++;
                } else {
                    logger.info("five heartbeat cycles not read, so colse this channel");
                    times = 0;
                    ctx.channel().disconnect();
                }

            }
        }
    }
}

package com.boot.example.netty.inoutboundhandler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author TuoZhou
 */
@Slf4j
public class ByteToLongReplayingDecoder extends ReplayingDecoder<Void> {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        log.info("decode被调用......");

        //不需要判断数据是否足够读取，内部会进行处理判断
        // if (in.readableBytes() >= 8) {
        //     out.add(in.readLong());
        // }

        out.add(in.readLong());
    }
}

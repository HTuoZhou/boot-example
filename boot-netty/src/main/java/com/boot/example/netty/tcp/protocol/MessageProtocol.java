package com.boot.example.netty.tcp.protocol;

import lombok.Data;

/**
 * @author TuoZhou
 */
@Data
public class MessageProtocol {

    private int length;
    private byte[] content;

}

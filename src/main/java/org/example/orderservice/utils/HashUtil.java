package org.example.orderservice.utils;

import org.apache.commons.codec.digest.DigestUtils;
import org.example.orderservice.dto.CreateOrderRequest;

public class HashUtil {
    public static String hash(CreateOrderRequest request){
        String canonical = request.userId() + "|" +
                request.customerName() + "|" +
                request.orderAmount();

        String hash = DigestUtils.sha256Hex(canonical);
        return  hash;
    }
}

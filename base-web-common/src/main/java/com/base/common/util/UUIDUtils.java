package com.base.common.util;

import java.util.UUID;

/**
 * uuid生产
 *
 * @author lixon
 *
 */
public class UUIDUtils {

    /**
     * 随机生成32位的UUID字符串
     *
     * @return
     */
    public static String uuid2String() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     * 随机生成36位的UUID字符串
     *
     * @return
     */
    public static String uuid() {
        return UUID.randomUUID().toString();
    }

    public static void main(String[] args) {
        System.out.println(uuid2String());
    }
}

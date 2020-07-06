package com.github.wxshop;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;

public class LoggerDemo {
    //simple log facade for java===>slf4j
    // a.b.c.d是a.b.c的孩子,也是是a.b的孩子,也是是a的孩子, 也是ROOT logger的孩子
    private static Logger logger1 = LoggerFactory.getLogger("com");
    private static Logger logger2 = LoggerFactory.getLogger("com.github");

    // 等价于 com.github.wxshop.LoggerDemo 等价于LoggerDemo.class
    private static Logger logger3 = LoggerFactory.getLogger(LoggerDemo.class);

    private static Logger logger4 = LoggerFactory.getLogger("org");

    public static void main(String[] args) {
        RuntimeException e = new RuntimeException();
        logger1.debug("1 {} {} {}", new HashMap<>(), 1, new Object());
        logger1.info("1 {}", new ArrayList<>());
        logger1.error("1", e);
        logger2.debug("1 {}", new HashMap<>());
        logger2.info("1 {}", new ArrayList<>());
        logger2.error("1", e);
        logger3.debug("4 {}", new HashMap<>());
        logger3.info("4 {}", new ArrayList<>());
        logger3.error("4", e);

        logger4.info("I'm org!");
    }


}

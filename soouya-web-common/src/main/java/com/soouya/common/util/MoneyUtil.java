package com.soouya.common.util;

import java.math.BigDecimal;

/**
 * Created by xuyuli on 2016/6/12.
 */
public class MoneyUtil {

    /**
     * 根据单位将金额转换为人民币元
     *
     * @param centMoney
     * @return
     */
    public static Double getRmbYuan(Integer centMoney) {
        if (centMoney == null) {
            return null;
        }
        return Double.valueOf(centMoney) / 100;
    }

    public static Double getRmbYuan(Long centMoney) {
        if (centMoney == null) {
            return null;
        }
        return Double.valueOf(centMoney) / 100;
    }

    public static Integer getRmbCentInt(Double yuanMoney) {
        if (yuanMoney == null) {
            return null;
        }
        BigDecimal myYuan = new BigDecimal(yuanMoney);
        BigDecimal myYuanCent = myYuan.multiply(new BigDecimal(100));
        myYuanCent = myYuanCent.setScale(0, BigDecimal.ROUND_HALF_UP);
        return myYuanCent.intValue();
    }

    public static Long getRmbCentLong(Double yuanMoney) {
        if (yuanMoney == null) {
            return null;
        }
        BigDecimal myYuan = new BigDecimal(yuanMoney);
        BigDecimal myYuanCent = myYuan.multiply(new BigDecimal(100));
        myYuanCent = myYuanCent.setScale(0, BigDecimal.ROUND_HALF_UP);
        return myYuanCent.longValue();
    }

}

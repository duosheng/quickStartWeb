package com.base.qs.controller.cmc.bizSystem.param;

import javacommon.base.JsonDoc;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Created by xuyuli on 2016/10/31.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddInReposityParam {

    @JsonDoc(description = "订单号")
    private String orderNumber;

    @JsonDoc(description="仓库名称")
    private String reposityName;

    /**
     * 入仓类型 (数据库字段)
     */
    @JsonDoc(description="入仓类型 1,采购入仓 2,采购换货入仓 3,客户发回入仓 4,售后换货入仓")
    private Integer inType;


    @JsonDoc(description = " 退换货id", def = "")
    private String returnReplaceId;

    /**
     * 入仓明细
     */
    @JsonDoc(description="入仓明细")
    private List<ClothLone> clothLoneList;



    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ClothLone{

        @JsonDoc(description = "匹id", def = "")
        private String id;

        @JsonDoc(description="货号")
        private String productNumber;

        @JsonDoc(description="匹号")
        private String clothLoneNumber;

        @JsonDoc(description="色号")
        private String color;

        @JsonDoc(description="实际布匹长度(入仓数量)" ,allowNull = false)
        private Double quantity;

        @JsonDoc(description="数量单位")
        private String quantityUnit;

    }
}

package com.base.qs.controller.cmc.bizSystem.vo;

import javacommon.base.JsonDoc;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by xuyuli on 2016/10/30.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchOrderVo {

    @JsonDoc(description="订单号",def = "10620601")
    private String orderNumber;//订单号

    @JsonDoc(description="订单业务号",def = "DLGD16092110620601")
    private String serviceNumber;//订单业务号

    @JsonDoc(description="订单发布时间")
    private Long createTime;//订单时间

    @JsonDoc(description="采购商名称")
    private String buyerName;//采购商

    @JsonDoc(description="供应商名称")
    private String sellerName;//供应商

    @JsonDoc(description="跟单员名称")
    private String followerName;//跟单员

    @JsonDoc(description = "采购员", def = "DLG16110600")
    private String purchaserName = "";

    @JsonDoc(description = "当前状态", def = "履约中")
    private String statusDesc = "";

    @JsonDoc(description = "是否能入仓,1可以,-1不行", def = "")
    private Integer isCanRc = 1;




}

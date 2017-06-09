package com.base.qs.domain;

import javacommon.base.JsonDoc;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 业务系统
 * Created by xuyuli on 17-5-12.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BizSystem {

    private String id;

    private String number;
    private String name;
    @JsonDoc(description = "业务系统的所有人", def = "")
    private String owner;
    @JsonDoc(description = "业务系统的所有人联系方式", def = "")
    private String ownerContact;
    @JsonDoc(description = "端口", def = "")
    private Integer port;
    private Integer httpPort;
    @JsonDoc(description = "描述", def = "")
    private String description;
    @JsonDoc(description = "所属产品线id", def = "")
    private Integer productId;
    private String zk;
    private Long dimension;
    @JsonDoc(description = "域名", def = "")
    private String domain;
    private String product;
}

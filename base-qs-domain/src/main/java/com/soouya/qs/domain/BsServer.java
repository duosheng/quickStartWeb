package com.base.qs.domain;

import javacommon.base.JsonDoc;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by xuyuli on 17-5-12.
 * 业务系统的服务节点
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BsServer {

    private String id;

    @JsonDoc(description = "ip", def = "")
    private String ip;
    @JsonDoc(description = "域名", def = "")
    private String hostname;
    private Integer httpPort;
    @JsonDoc(description = "所属业务系统id", def = "")
    private Integer bizSystemId;
}

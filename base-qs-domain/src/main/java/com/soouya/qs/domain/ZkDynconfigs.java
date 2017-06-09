package com.base.qs.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by xuyuli on 17-5-12.
 * 动态配置
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ZkDynconfigs {

    private String id;

    private Long bizSysId;
    private String appName;
    private String groupName;
    private String dataId;
    private String content;
    private String description;
    private String product;
}

package com.soouya.qs.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by xuyuli on 17-5-12.
 * 产品线
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    private String id;

    private String name;
    private String code;
    private String domain;
    private Integer showInBi;
}

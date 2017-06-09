package com.base.doc;

import javacommon.base.JsonDoc;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by xuyuli on 17-4-5.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Tag {

    @JsonDoc(description = "名称")
    private String name;

    @JsonDoc(description = "描述")
    private String description;

}

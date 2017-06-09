package com.base.doc;

/**
 * Created by xuyuli on 17-4-5.
 */
public enum TagsEnum {


//    默认("",""),

    业务系统管理("bizSystem","业务系统管理");



    private String name;

    private String description;

    private TagsEnum(String name, String description) {
        this.name = name;
        this.description = description;
    }


    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}

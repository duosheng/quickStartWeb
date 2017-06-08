package com.soouya.qs.controller.cmc;

import com.soouya.doc.ApiDoc;
import com.soouya.doc.TagsEnum;
import com.soouya.qs.cmc.service.BizSystemService;
import com.soouya.qs.controller.cmc.bizSystem.param.AddInReposityParam;
import com.soouya.qs.controller.cmc.bizSystem.vo.SearchOrderVo;
import javacommon.base.ListVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by xuyuli on 17-5-9.
 */
@RestController
@RequestMapping("/cmc/BizSystem")
public class BizSystemController {

    @Autowired
    private BizSystemService bizSystemService;

    @ResponseBody
    @ApiDoc(desc = "获取所有的业务系统",tag = TagsEnum.业务系统管理)
    public ListVo<SearchOrderVo> list(@RequestBody AddInReposityParam param) {

        ListVo<SearchOrderVo> listVo = new ListVo<>();
        return listVo;
    }

}

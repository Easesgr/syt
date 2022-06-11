package com.anyi.yygh.cmn.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.anyi.yygh.cmn.mapper.DictMapper;
import com.anyi.yygh.model.cmn.Dict;
import com.anyi.yygh.vo.cmn.DictEeVo;
import org.springframework.beans.BeanUtils;

import javax.annotation.Resource;

/**
 * @author 安逸i
 * @version 1.0
 */
public class DictListener extends AnalysisEventListener<DictEeVo> {
    private DictMapper dictMapper;

    public DictListener(DictMapper dictMapper) {
        this.dictMapper = dictMapper;
    }

    @Override
    public void invoke(DictEeVo dictEeVo, AnalysisContext analysisContext) {
        //调用方法添加数据库
        Dict dict = new Dict();
        BeanUtils.copyProperties(dictEeVo,dict);
        dict.setIsDeleted(0);
        dictMapper.insert(dict);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }
}

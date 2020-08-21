package com.zicms.web.system.controller;

import com.zicms.commons.constant.UserConstants;
import com.zicms.commons.annotation.Log;
import com.zicms.commons.base.BaseController;
import com.zicms.commons.base.JsonResult;
import com.zicms.commons.enums.BusinessType;
import com.zicms.commons.page.TableDataInfo;
import com.zicms.commons.utils.poi.ExcelUtil;
import com.zicms.framework.util.ShiroUtils;
import com.zicms.framework.web.model.Ztree;
import com.zicms.web.system.model.SysDictType;
import com.zicms.web.system.service.ISysDictTypeService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 数据字典信息
 *
 * @author ruoyi
 */
@Controller
@RequestMapping("/system/dict")
public class SysDictTypeController extends BaseController {
    private final String prefix = "system/dict/type";

    @Autowired
    private ISysDictTypeService dictTypeService;

    @RequiresPermissions("system:dict:view")
    @GetMapping()
    public String dictType() {
        return prefix + "/type";
    }

    @PostMapping("/list")
    @RequiresPermissions("system:dict:list")
    @ResponseBody
    public TableDataInfo list(SysDictType dictType) {
        startPage();
        List<SysDictType> list = dictTypeService.selectDictTypeList(dictType);
        return getDataTable(list);
    }

    @Log(title = "字典类型", businessType = BusinessType.EXPORT)
    @RequiresPermissions("system:dict:export")
    @PostMapping("/export")
    @ResponseBody
    public JsonResult export(SysDictType dictType) {

        List<SysDictType> list = dictTypeService.selectDictTypeList(dictType);
        ExcelUtil<SysDictType> util = new ExcelUtil<SysDictType>(SysDictType.class);
        return util.exportExcel(list, "字典类型");
    }

    /**
     * 新增字典类型
     */
    @GetMapping("/add")
    public String add() {
        return prefix + "/add";
    }

    /**
     * 新增保存字典类型
     */
    @Log(title = "字典类型", businessType = BusinessType.INSERT)
    @RequiresPermissions("system:dict:add")
    @PostMapping("/add")
    @ResponseBody
    public JsonResult addSave(@Validated SysDictType dict) {
        if (UserConstants.DICT_TYPE_NOT_UNIQUE.equals(dictTypeService.checkDictTypeUnique(dict))) {
            return error("新增字典'" + dict.getDictName() + "'失败，字典类型已存在");
        }
        dict.setCreateBy(ShiroUtils.getLoginName());
        return toAjax(dictTypeService.insertDictType(dict));
    }

    /**
     * 修改字典类型
     */
    @GetMapping("/edit/{dictId}")
    public String edit(@PathVariable("dictId") Long dictId, ModelMap mmap) {
        mmap.put("dict", dictTypeService.selectDictTypeById(dictId));
        return prefix + "/edit";
    }

    /**
     * 修改保存字典类型
     */
    @Log(title = "字典类型", businessType = BusinessType.UPDATE)
    @RequiresPermissions("system:dict:edit")
    @PostMapping("/edit")
    @ResponseBody
    public JsonResult editSave(@Validated SysDictType dict) {
        if (UserConstants.DICT_TYPE_NOT_UNIQUE.equals(dictTypeService.checkDictTypeUnique(dict))) {
            return error("修改字典'" + dict.getDictName() + "'失败，字典类型已存在");
        }
        dict.setUpdateBy(ShiroUtils.getLoginName());
        return toAjax(dictTypeService.updateDictType(dict));
    }

    @Log(title = "字典类型", businessType = BusinessType.DELETE)
    @RequiresPermissions("system:dict:remove")
    @PostMapping("/remove")
    @ResponseBody
    public JsonResult remove(String ids) {
        return toAjax(dictTypeService.deleteDictTypeByIds(ids));
    }

    /**
     * 清空缓存
     */
    @RequiresPermissions("system:dict:remove")
    @Log(title = "字典类型", businessType = BusinessType.CLEAN)
    @GetMapping("/clearCache")
    @ResponseBody
    public JsonResult clearCache() {
        dictTypeService.clearCache();
        return success();
    }

    /**
     * 查询字典详细
     */
    @RequiresPermissions("system:dict:list")
    @GetMapping("/detail/{dictId}")
    public String detail(@PathVariable("dictId") Long dictId, ModelMap mmap) {
        mmap.put("dict", dictTypeService.selectDictTypeById(dictId));
        mmap.put("dictList", dictTypeService.selectDictTypeAll());
        return "system/dict/data/data";
    }

    /**
     * 校验字典类型
     */
    @PostMapping("/checkDictTypeUnique")
    @ResponseBody
    public String checkDictTypeUnique(SysDictType dictType) {
        return dictTypeService.checkDictTypeUnique(dictType);
    }

    /**
     * 选择字典树
     */
    @GetMapping("/selectDictTree/{columnId}/{dictType}")
    public String selectDeptTree(@PathVariable("columnId") Long columnId, @PathVariable("dictType") String dictType,
                                 ModelMap mmap) {
        mmap.put("columnId", columnId);
        mmap.put("dict", dictTypeService.selectDictTypeByType(dictType));
        return prefix + "/tree";
    }

    /**
     * 加载字典列表树
     */
    @GetMapping("/treeData")
    @ResponseBody
    public List<Ztree> treeData() {
        List<Ztree> ztrees = dictTypeService.selectDictTree(new SysDictType());
        return ztrees;
    }
}

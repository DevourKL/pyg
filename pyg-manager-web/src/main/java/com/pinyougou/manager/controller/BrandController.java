package com.pinyougou.manager.controller;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.TbBrand;
import com.pinyougou.sellergoods.service.BrandService;

import entity.PageResult;
import entity.Result;

/**
 * 
 * @author admin
 *
 */
@RestController
@RequestMapping("/brand")
public class BrandController {
	
	@Reference
	private BrandService brandService;
	
	/**
	 * 查找所有品牌信息
	 * @return
	 */
	@RequestMapping("/findAll")
	public List<TbBrand> findAll(){
		return brandService.findAll();
	}
	
	/**
	 * 分页查询品牌信息
	 * @param page 当前页
	 * @param size 每页记录数
	 * @return
	 */
	@RequestMapping("/findPage")
	public PageResult findPage(int page, int size) {
		return brandService.findPage(page, size);
	}
	
	/**
	 * 模糊查询加分页
	 * @param brand 模糊查询条件
	 * @param page 当前页
	 * @param size 每页记录数
	 * @return
	 */
	@RequestMapping("/search")
	public PageResult search(@RequestBody TbBrand brand, int page, int size) {
		return brandService.findPage(brand, page, size);
	}
	/**
	 * 添加品牌
	 * @param tbBrand
	 * @return
	 */
	@RequestMapping("/addBrand")
	public Result addBrand(@RequestBody TbBrand tbBrand) {
		
		try {
			brandService.add(tbBrand);
			return new Result(true,"添加成功！");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false,"添加失败！");
		}
	}
	
	/**
	 * 根据品牌id查询品牌信息
	 * @param id 品牌id
	 * @return
	 */
	@RequestMapping("/findById")
	public TbBrand findById(long id) {
		return brandService.findById(id);
	}
	
	/**
	 * 修改品牌信息
	 * @param tbBrand
	 * @return
	 */
	@RequestMapping("/updateBrand")
	public Result updateBrand(@RequestBody TbBrand tbBrand) {
		
		try {
			brandService.updateBrand(tbBrand);
			return new Result(true,"修改成功！");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false,"修改失败！");
		}
	}
	
	@RequestMapping("/deleteBrand")
	public Result deleteBrand(long[] ids) {
		
		try {
			brandService.delete(ids);;
			return new Result(true,"删除成功！");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false,"删除失败！");
		}
	}
	
	@RequestMapping("/selectOptionList")
	public List<Map> selectOptionList(){
		return brandService.selectOptionList();
	}
}

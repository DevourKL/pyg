package com.pinyougou.sellergoods.service;

import java.util.List;
import java.util.Map;

import com.pinyougou.pojo.TbBrand;

import entity.PageResult;

/**
 * 品牌管理接口
 * @author admin
 *
 */
public interface BrandService {
	
	List<TbBrand> findAll();
	
	/**
	 * 品牌分页
	 * @param pageNum 当前页码
	 * @param pageSize 每页记录数
	 * @return
	 */
	PageResult findPage(int pageNum, int pageSize);
	
	/**
	 * 品牌模糊查询、分页
	 * @param brand 模糊查询条件
	 * @param pageNum 当前页码
	 * @param pageSize 每页记录数
	 * @return
	 */
	PageResult findPage(TbBrand brand, int pageNum, int pageSize);
	
	/**
	 * 添加品牌
	 * @param tbBrand
	 */
	void add(TbBrand tbBrand);
	
	/**
	 * 根据id查询品牌信息
	 * @param id
	 * @return
	 */
	TbBrand findById(long id);
	
	/**
	 * 根据id修改品牌信息
	 * @param tbBrand
	 */
	void updateBrand(TbBrand tbBrand);
	
	/**
	 * 根据id的数组删除品牌
	 * @param ids
	 */
	void delete(long[] ids);
	
	/**
	 * 返回品牌下拉列表数据
	 * @return
	 */
	List<Map> selectOptionList();
}

package com.pinyougou.sellergoods.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pinyougou.mapper.TbBrandMapper;
import com.pinyougou.pojo.TbBrand;
import com.pinyougou.pojo.TbBrandExample;
import com.pinyougou.pojo.TbBrandExample.Criteria;
import com.pinyougou.sellergoods.service.BrandService;

import entity.PageResult;
/**
 * 品牌管理
 * @author admin
 *
 */
@Service
public class BrandServiceImpl implements BrandService {
	
	@Autowired
	private TbBrandMapper brandMapper;
	@Override
	public List<TbBrand> findAll() {
		return brandMapper.selectByExample(null);
	}
	
	/**
	 * 分页
	 */
	@Override
	public PageResult findPage(int pageNum, int pageSize) {
		
		PageHelper.startPage(pageNum, pageSize);//mybatis 分页插件
		Page<TbBrand> page = (Page<TbBrand>) brandMapper.selectByExample(null);
		return new PageResult(page.getTotal(),page.getResult());
	}
	
	/**
	 * 添加
	 */
	@Override
	public void add(TbBrand tbBrand) {
		brandMapper.insert(tbBrand);
	}
	
	/**
	 * 根据id查找一个
	 */
	@Override
	public TbBrand findById(long id) {
		return brandMapper.selectByPrimaryKey(id);
	}
	
	/**
	 * 修改
	 */
	@Override
	public void updateBrand(TbBrand tbBrand) {
		brandMapper.updateByPrimaryKey(tbBrand);
		
	}

	/**
	 * 批量删除
	 */
	@Override
	public void delete(long[] ids) {
		
		for (long id :ids) {
			brandMapper.deleteByPrimaryKey(id);
		}
	}
	
	/**
	 * 搜索，分页
	 */
	@Override
	public PageResult findPage(TbBrand brand, int pageNum, int pageSize) {
		
		PageHelper.startPage(pageNum, pageSize);//mybatis 分页插件
		
		TbBrandExample example = null;
		if(brand != null) {
			example = new TbBrandExample();
			Criteria criteria = example.createCriteria();
			if (brand.getName() !=null && brand.getName().length()>0) {
				criteria.andNameLike("%"+brand.getName()+"%");
			}
			if (brand.getFirstChar() != null && brand.getFirstChar().length() > 0) {
				criteria.andFirstCharLike("%"+brand.getFirstChar()+"%");
			}
		}		
		Page<TbBrand> page = (Page<TbBrand>) brandMapper.selectByExample(example);;
		return new PageResult(page.getTotal(),page.getResult());
	}
	
	/**
	 * 查询品牌列表
	 */
	@Override
	public List<Map> selectOptionList() {
		return brandMapper.selectOptionList();
	}

}

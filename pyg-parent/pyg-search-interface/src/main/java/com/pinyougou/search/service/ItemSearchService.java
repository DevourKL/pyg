package com.pinyougou.search.service;

import java.util.List;
import java.util.Map;

public interface ItemSearchService {
	
	Map itemSearch(Map searchMap);
	
	/**
	 * 导入索引数据库
	 * @param list
	 */
	void importList(List list);
	
	/**
	 * 从索引库中删除
	 * @param list
	 */
	void deleteByGoodsIds(List goodsIds);

}

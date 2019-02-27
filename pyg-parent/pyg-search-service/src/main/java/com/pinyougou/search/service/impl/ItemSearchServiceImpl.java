package com.pinyougou.search.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.FilterQuery;
import org.springframework.data.solr.core.query.GroupOptions;
import org.springframework.data.solr.core.query.HighlightOptions;
import org.springframework.data.solr.core.query.HighlightQuery;
import org.springframework.data.solr.core.query.Query;
import org.springframework.data.solr.core.query.SimpleFilterQuery;
import org.springframework.data.solr.core.query.SimpleHighlightQuery;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.data.solr.core.query.SolrDataQuery;
import org.springframework.data.solr.core.query.result.GroupEntry;
import org.springframework.data.solr.core.query.result.GroupPage;
import org.springframework.data.solr.core.query.result.GroupResult;
import org.springframework.data.solr.core.query.result.HighlightEntry;
import org.springframework.data.solr.core.query.result.HighlightEntry.Highlight;
import org.springframework.data.solr.core.query.result.HighlightPage;
import org.springframework.data.solr.core.query.result.ScoredPage;

import com.alibaba.dubbo.config.annotation.Service;
import com.pinyougou.pojo.TbItem;
import com.pinyougou.search.service.ItemSearchService;

@Service(timeout=5000)
public class ItemSearchServiceImpl implements ItemSearchService {
	
	@Autowired
	private SolrTemplate solrTemplate;
	
	@Override
	public Map itemSearch(Map searchMap) {
		Map map = new HashMap();
		String keywords = (String) searchMap.get("keywords");
		searchMap.put("keywords", keywords.replace(" ", ""));
		map.putAll(searchList(searchMap));
		List<String> categoryList = searchCategoryList(searchMap);
		map.put("categoryList", categoryList);
		String category = (String) searchMap.get("category");
		if(!category.equals("")) {
			map.putAll(searchBrandAndSpecList(category));
		} else {
			if(categoryList.size()>0) {
				map.putAll(searchBrandAndSpecList(categoryList.get(0)));
			}
		}		
		return map;
	}
	
	/**
	 * 查询列表，高亮显示关键字
	 * @param searchMap
	 * @return
	 */
	private Map searchList(Map searchMap) {
		Map map = new HashMap();
		
		//高亮显示		
		HighlightQuery query = new SimpleHighlightQuery();		
		//构建高亮选项对象
		HighlightOptions highlightOptions = new HighlightOptions().addField("item_title");
		highlightOptions.setSimplePrefix("<em style='color:red'>");//前缀
		highlightOptions.setSimplePostfix("</em>");//后缀
		query.setHighlightOptions(highlightOptions);//为查询对象设置高亮选项
		
		//1.1设置关键字搜索
		Criteria criteria = new Criteria("item_keywords").is(searchMap.get("keywords"));
		query.addCriteria(criteria);
		
		
		//1.2按商品分类过滤		
		if (!"".equals(searchMap.get("category"))) {
			FilterQuery filterQuery = new SimpleFilterQuery();			
			Criteria filterCriteria = new Criteria("item_category").is(searchMap.get("category"));
			filterQuery.addCriteria(filterCriteria);
			query.addFilterQuery(filterQuery);
		}
		
		//1.3按品牌过滤		
		if (!"".equals(searchMap.get("brand"))) {
			FilterQuery filterQuery = new SimpleFilterQuery();			
			Criteria filterCriteria = new Criteria("item_brand").is(searchMap.get("brand"));
			filterQuery.addCriteria(filterCriteria);
			query.addFilterQuery(filterQuery);
		}
		
		Map<String,String> specMap = (Map<String,String>) searchMap.get("spec");
		//1.4按选项列表过滤		
		if (specMap!=null) {
			
			for (String key :specMap.keySet()) {
				FilterQuery filterQuery = new SimpleFilterQuery();			
				Criteria filterCriteria = new Criteria("item_spec_"+key).is(specMap.get(key));
				filterQuery.addCriteria(filterCriteria);
				query.addFilterQuery(filterQuery);
			}
			
		}
		
		//1.5按价格区间过滤		
		if (!"".equals(searchMap.get("price"))) {
			String[] price = ((String) searchMap.get("price")).split("-");
			FilterQuery filterQuery = new SimpleFilterQuery();
			if(!"0".equals(price[0])) {
				Criteria filterCriteria = new Criteria("item_price").greaterThanEqual(price[0]);
				filterQuery.addCriteria(filterCriteria);
				query.addFilterQuery(filterQuery);
			}
			if(!"*".equals(price[1])) {
				Criteria filterCriteria = new Criteria("item_price").lessThanEqual(price[1]);
				filterQuery.addCriteria(filterCriteria);
				query.addFilterQuery(filterQuery);
			}
		}
		
		//1.6分页
		Integer pageNo = (Integer) searchMap.get("pageNo");
		if(pageNo==null) {
			pageNo=1;
		}
		Integer pageSize = (Integer) searchMap.get("pageSize");
		if(pageSize==null) {
			pageSize=20;
		}
		query.setOffset((pageNo-1)*pageSize);//设置起始索引
		query.setRows(pageSize);//设置每页记录数
		
		//1.7排序		
		String sortValue = (String) searchMap.get("sort");
		String sortField = (String) searchMap.get("sortField");
		System.out.println(sortValue+"  "+sortField);
		if(sortValue!=null && !sortValue.equals("")) {			
			if("ASC".equals(sortValue)) {
				Sort sort = new Sort(Sort.Direction.ASC,"item_"+sortField);
				System.out.println(sortValue+"  "+sortField+"升序");
				query.addSort(sort);
			}
			if("DESC".equals(sortValue)) {
				Sort sort = new Sort(Sort.Direction.DESC,"item_"+sortField);
				System.out.println(sortValue+"  "+sortField+"降序");
				query.addSort(sort);
			}						
		}
		
		
		HighlightPage<TbItem> page = solrTemplate.queryForHighlightPage(query, TbItem.class);
		//高亮入口集合（每条记录的高亮入口）
		List<HighlightEntry<TbItem>> entryList = page.getHighlighted();
		for (HighlightEntry<TbItem> entry : entryList) {
			//获取高亮列表（高亮域的个数）
			List<Highlight> highlights = entry.getHighlights();
			
			/*for (Highlight h : highlights) {
				List<String> sns = h.getSnipplets();
				System.out.println(sns);
			}*/
			if(highlights.size()>0 && highlights.get(0).getSnipplets().size()>0) {
				TbItem item = entry.getEntity();
				item.setTitle(highlights.get(0).getSnipplets().get(0));
			}		
		}
		map.put("rows", page.getContent());	
		map.put("totalPages", page.getTotalPages());
		map.put("total", page.getTotalElements());
		return map;
	}
	/**
	 * 查询商品分类名称
	 * @param searchMap
	 * @return
	 */
	private List<String> searchCategoryList(Map searchMap) {
		
		List<String> list = new ArrayList<String>();
		
		//关键字查询
		Query query = new SimpleQuery("*:*");
		Criteria criteria = new Criteria("item_keywords").is(searchMap.get("keywords"));//where ...
		query.addCriteria(criteria);
		
		//分组条件选项
		GroupOptions groupOptions = new GroupOptions().addGroupByField("item_category");//group by ...
		query.setGroupOptions(groupOptions);
		
		//获取分组页
		GroupPage<TbItem> page = solrTemplate.queryForGroupPage(query, TbItem.class);
		
		//获取分组结果
		GroupResult<TbItem> groupResult = page.getGroupResult("item_category");
		
		//获取分组入口页
		Page<GroupEntry<TbItem>> groupEntries = groupResult.getGroupEntries();
		//获取分组入口集合
		List<GroupEntry<TbItem>> content = groupEntries.getContent();
		
		for (GroupEntry<TbItem> entry : content) {
			list.add(entry.getGroupValue());//将分组的结果添加到返回值中
		}
		return list;
		
	}
	
	@Autowired
	private RedisTemplate redisTemplate;
	
	/**
	 * 根据商品种类名称获取品牌列表和规格列表
	 * @param category
	 * @return
	 */
	private Map searchBrandAndSpecList(String category) {
		
		Map map = new HashMap();
		//1.根据商品名称获取模板ID
		Long templateId = (Long)redisTemplate.boundHashOps("itemCat").get(category);
		
		if (templateId != null) {
			//2.根据模板ID获取品牌列表
			List brandList = (List) redisTemplate.boundHashOps("brandList").get(templateId);
			map.put("brandList", brandList);
			//3.根据模板ID获取规格列表
			List specList = (List) redisTemplate.boundHashOps("specList").get(templateId);
			map.put("specList", specList);
			
		}
		return map;
	}

	@Override
	public void importList(List list) {
		solrTemplate.saveBeans(list);
		solrTemplate.commit();
	}

	@Override
	public void deleteByGoodsIds(List goodsIds) {
		Query query = new SimpleQuery("*:*");
		
		Criteria criteria = new Criteria("item_goodsid").in(goodsIds);
		query.addCriteria(criteria);
		solrTemplate.delete(query);
		solrTemplate.commit();
		
	}
}

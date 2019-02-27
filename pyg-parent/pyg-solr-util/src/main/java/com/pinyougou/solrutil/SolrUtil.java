package com.pinyougou.solrutil;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.Query;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.pinyougou.mapper.TbItemMapper;
import com.pinyougou.pojo.TbItem;
import com.pinyougou.pojo.TbItemExample;
import com.pinyougou.pojo.TbItemExample.Criteria;

@Component
public class SolrUtil {
	
	@Autowired
	private TbItemMapper itemMapper;
	
	@Autowired
	private SolrTemplate solrTemplate;
	
	public void importItemData() {
		
		TbItemExample example = new TbItemExample();
		Criteria criteria = example.createCriteria();
		criteria.andStatusEqualTo("1");//只添加状态为1的
		
		List<TbItem> list = itemMapper.selectByExample(example);
		
		System.out.println("---商品列表---");
		for (TbItem item : list) {
			System.out.println(item.getId()+" "+item.getTitle());
			
			Map specMap = JSON.parseObject(item.getSpec(),Map.class);
			item.setSpecMap(specMap);
			
		}
		System.out.println("---商品列表结束---");
		solrTemplate.saveBeans(list);
		solrTemplate.commit();
		/*Query query = new SimpleQuery("*:*");;
		solrTemplate.delete(query);
		solrTemplate.commit();*/
		
		
	}
	
	public static void main(String[] args) {
		ApplicationContext app = new ClassPathXmlApplicationContext("classpath*:spring/applicationContext*.xml");
		SolrUtil solrUtil = (SolrUtil) app.getBean("solrUtil");
		solrUtil.importItemData();
		
	}

}

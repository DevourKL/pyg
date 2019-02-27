app.service('searchService' ,function($http){
	
	//根据广告分类ID查询广告
	this.search=function(searchMap){
		return $http.post('itemSearch/search.do',searchMap);
	}
	
});
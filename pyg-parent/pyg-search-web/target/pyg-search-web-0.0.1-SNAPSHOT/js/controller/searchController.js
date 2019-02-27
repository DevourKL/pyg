app.controller('searchController',function($scope,$controller,searchService){
	
	$controller("baseController",{$scope:$scope});
	
	$scope.searchMap={'keywords':'','category':'','brand':'','spec':{},'price':'','pageNo':1,'pageSize':40,'sort':'','sortField':''};
	$scope.resultMap={};
	

	//搜索的方法	
	$scope.search=function(){
		$scope.searchMap.pageNo=parseInt($scope.searchMap.pageNo);
		searchService.search($scope.searchMap).success(
			function(response){
				$scope.resultMap=response;
				buildPageLable();//构建分页栏
			}
		);		
	}
	
	//构建分页栏
	buildPageLable=function(){
		$scope.pageLable=[];
		$scope.startPage=1;
		$scope.endPage=$scope.resultMap.totalPages;
		$scope.fristDot=true;
		$scope.lastDot=true;
		if($scope.resultMap.totalPages>5){
			if($scope.searchMap.pageNo<=3){
				$scope.endPage=5;
				$scope.fristDot=false;
			}else if ($scope.searchMap.pageNo>=$scope.resultMap.totalPages-2){
				$scope.startPage=$scope.resultMap.totalPages-4;
				$scope.lastDot=false;
			}else{				
				$scope.startPage=$scope.searchMap.pageNo-2;
				$scope.endPage=$scope.searchMap.pageNo+2;
			}
		}else{
			$scope.fristDot=false;
			$scope.lastDot=false;
		}
		for(var i=$scope.startPage;i<=$scope.endPage;i++){
			$scope.pageLable.push(i);
		}		
	}
	
	
	//添加筛选项
	$scope.addSearchItem=function(key,value){
		if(key =='category' || key=='brand' || key=='price'){
			$scope.searchMap[key]=value;
		}else{
			$scope.searchMap.spec[key]=value;
		}
		$scope.search();
	}
	//删除筛选项
	$scope.removeSearchItem=function(key){
		if(key =='category' || key=='brand' || key=='price'){
			$scope.searchMap[key]='';
		}else{
			delete $scope.searchMap.spec[key];
		}
		$scope.search();
	}
	
	//根据页码查询
	$scope.queryByPage=function(pageNo){
		if(pageNo<1||pageNo>$scope.resultMap.totalPages){
			return;
		}
		$scope.searchMap.pageNo=pageNo;
		$scope.search();		
	}
	
	//上一页禁用
	$scope.isTopPage=function(){
		if($scope.searchMap.pageNo==1){
			return true;
		}else{
			return false;
		}
	}
	//下一页禁用
	$scope.isEndPage=function(){
		if($scope.searchMap.pageNo==$scope.resultMap.totalPages){
			return true;
		}else{
			return false;
		}
	}
	//排序
	$scope.sortSearch=function(sort,sortField){
		$scope.searchMap.sort=sort;
		$scope.searchMap.sortField=sortField;
		$scope.search();
	}
	
	//判断关键字是否是品牌
	$scope.keywordsIsBrand=function(){
		for(var i=0; i<$scope.resultMap.brandList.length;i++){
			if($scope.resultMap.brandList[i].text.indexof($scope.searchMap.keywords)>0){
				return true;
			}
		}
		return false;
	}
});
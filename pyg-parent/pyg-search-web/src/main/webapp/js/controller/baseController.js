app.controller("baseController",function($scope){
	
	//重新加载列表 数据
	$scope.reloadList=function(){
		//切换页码  
		$scope.search( $scope.paginationConf.currentPage, $scope.paginationConf.itemsPerPage);
	}
	
	//分页控件配置 
	$scope.paginationConf = {
			 currentPage: 1,
			 totalItems: 10,
			 itemsPerPage: 10,
			 perPageOptions: [10, 20, 30, 40, 50],
			 onChange: function(){
			        	 $scope.reloadList();//重新加载
			 }
	};
	//用户勾选的品牌id
	$scope.selectIds = [];
	$scope.selectId=function($event,id){
		if($event.target.checked){
			$scope.selectIds.push(id);
		} else {
			var index = $scope.selectIds.indexOf(id);//获取id值在数组中的位置
			$scope.selectIds.splice(index,1);//从index位置删除数组中的1个元素
		}				
	}
	$scope.jsonToString=function(jsonString,key){
		var json=JSON.parse(jsonString);
		var value="";
		for (var i = 0; i < json.length; i++) {
			if(i>0){
				value +=",";
			}
			value += json[i][key];
		}
		return value;
	}
});
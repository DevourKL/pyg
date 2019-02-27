app.controller("brandController",function($scope,$controller,brandService){
    		
	$controller("baseController",{$scope:$scope});
	//加载所有品牌
	$scope.findAll=function(){
		brandService.findAll().success(
    			function(response){
    				$scope.brandList = response;
    			}		
    		);
	}
	   		
	//分页
	$scope.findPage=function(page,size){	
		brandService.findPage(page,size).success(
				function(response){
					$scope.brandList=response.rows;	
					$scope.paginationConf.totalItems=response.total;//更新总记录数
				}			
		);
	}
	
	$scope.searchEntity={};
	//搜索品牌
	$scope.search=function(page,size){	
		brandService.search(page,size,$scope.searchEntity).success(
				function(response){
					$scope.brandList=response.rows;	
					$scope.paginationConf.totalItems=response.total;//更新总记录数
				}			
		);
	}
	//添加品牌、修改品牌
	$scope.save=function(){
		var obj = null;
		if ($scope.entity.id != null){
			obj = brandService.updateBrand($scope.entity);
		} else {
			obj = brandService.addBrand($scope.entity);
		}
		obj.success(
			function(response){
				if(response.success){
					$scope.reloadList();//重新加载
				}else{
					alert(response.message);
				}
			}		
		);
	}
	
	//根据id查找品牌信息
	$scope.findById=function(id){
		brandService.findById(id).success(
			function(response){
				$scope.entity=response;
			}		
		);
	}
	
	//删除用户选中的品牌
	$scope.delBrands=function(){
		brandService.delBrands($scope.selectIds).success(
				function(response){
					if(response.success){
						$scope.reloadList();//重新加载
					}else{
						alert(response.message);
					}
				}		
			);
	}

});
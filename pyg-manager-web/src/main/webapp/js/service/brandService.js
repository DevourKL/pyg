app.service("brandService",function($http){
		
			this.findAll=function(){
				return $http.get("../brand/findAll.do");
			}
			this.findPage=function(page,size){
				return $http.get('../brand/findPage.do?page='+page+'&size='+size);
			}
			this.search=function(page,size,searchEntity){
				return $http.post('../brand/search.do?page='+page+'&size='+size,searchEntity);
			}
			this.addBrand=function(entity){
				return $http.post('../brand/addBrand.do',entity);
			}
			this.updateBrand=function(entity){
				return $http.post('../brand/updateBrand.do',entity);
			}
			this.findById=function(id){
				return $http.get('../brand/findById.do?id='+id);
			}
			this.delBrands=function(ids){
				return $http.get('../brand/deleteBrand.do?ids='+ids);
			}
			this.selectOptionList=function(){
				return $http.get('../brand/selectOptionList.do');
			}
	})
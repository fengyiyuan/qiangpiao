//两端去空格函数
String.prototype.trim = function() {    return this.replace(/(^\s*)|(\s*$)/g,""); };

var stationArr = station_names.split("@");
//车站自动补全
var InitStation = function ($dom){
		this.tEvent = null;
		this.dom = $dom;
		var id = $dom.attr("id");
		var htmlStr = '<div class="dropdown"><input type="text" class="form-control dropdown-toggle" id="'+id+'Str" name="'+id+'Str"  data-toggle="dropdown" aria-expanded="true">' +
				  '<ul class="dropdown-menu" role="menu" aria-labelledby="'+id+'Str">' +
				  '</ul><input type="hidden" name="'+id+'"></input></div>';
		$dom.html(htmlStr);
		var liStr = '';
		for(var i = 0,len = stationArr.length; i < len; i++){
			var station = stationArr[i].split("|");
			liStr += '<li role="presentation" searchtext="'+stationArr[i]+'"><a role="menuitem" tabindex="-1" href="javascript:void(0);">'+station[1]+'</a></li>';
		}
		$dom.find("ul").append(liStr);
		$dom.find("ul").find("li").hide();
		$dom.find("ul").hide();
		$dom.find("ul").find("li").bind("click",{curr:this},function(e){
			var dom = e.data.curr.dom;
			var station = $(this).attr("searchtext").split("|");
			dom.find("input[name='"+$dom.attr("id")+"']").val(station[2]);
			dom.find("input[name='"+$dom.attr("id")+"Str']").val(station[1]);
			dom.find("ul").hide();
		});
		$dom.find("#" + id + "Str").bind("blur",{currDom :this},function(e){
			var dom = e.data.currDom.dom;
			var id = $dom.attr("id");
			var sta = dom.find("input[name='"+id+"']").val();
			if(sta == ""){
				dom.find("#" + id +"Str").val("");
			}
			//dom.find("ul").hide();
		});
		$dom.find("#" + id + "Str").bind("keyup",{currDom :this},function(e){
			var dom = e.data.currDom.dom;
			dom.find("input[name='"+dom.attr("id")+"']").val("");
			if(dom.tEvent){
				clearTimeout(dom.tEvent);
			}
			dom.tEvent=setTimeout(function(){
				return stationKeyUpEvent(dom);
			},500);
		});
};
function stationKeyUpEvent(dom){
	var str = $(dom).find("input").val();
	$(dom).find("li").hide();
	var ret = false;
	if(str != ''){
		$(dom).find("li").each(function(){
			if($(this).attr("searchtext").indexOf(str) != -1){
				$(this).show();
				ret = true;
			}
		});
	}
	if(ret){
		$(dom).find("ul").show();
	}else{
		$(dom).find("ul").hide();
	}
}
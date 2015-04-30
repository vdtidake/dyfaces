/**
 * 
 */

var dyHighlightRegionFn = function(data,userCallback) {
	console.log('highlight data '+JSON.stringify(data))
	
    return function(canvas, area, g) {
		_.each(data, function(d,i){
			var min = moment(d.minX, ["YYYY/MM/DD hh:mm:ss"], true);
			var max = moment(d.maxX, ["YYYY/MM/DD hh:mm:ss"], true);
			
			if(min.isValid() && max.isValid()){
				dyHighlightRegionHelper(canvas, area, g, min, max, d.hexColor);
			}else{
				dyHighlightRegionHelper(canvas, area, g, d.minX, d.maxX, d.hexColor);
			}
			
		});
		if(userCallback != ''){
			var funcCall = userCallback + "(canvas, area, g);";
			eval(funcCall);
		}
    }
};

function dyHighlightRegionHelper(canvas, area, g, start, end, fill) {
	var bottomleft = g.toDomCoords(start, -20);
	var topright = g.toDomCoords(end, +20);

	var left = bottomleft[0];
	var right = topright[0];

	canvas.fillStyle = fill;
	canvas.fillRect(left, area.y, right - left, area.h);
}
var dyClickCallbackFn = function(userCallback,ajaxFn,graphId) {
	 return function(event, x, points) {
		 $('#'+graphId+'closestPoints').val(JSON.stringify(points));
		 if(ajaxFn != ''){
				eval(ajaxFn);
		  }
		 if(userCallback != ''){
				var funcCall = userCallback + "(event, x, points);";
				eval(funcCall);
		 }
	 }
}
var dyPointClickCallbackFn = function(userCallback,ajaxFn,graphId) {
	 return function(event, point) {
		 $('#'+graphId+'selectedPoint').val(JSON.stringify(point));
		 if(ajaxFn != ''){
				eval(ajaxFn);
		  }
		 if(userCallback != ''){
				var funcCall = userCallback + "(event, point);";
				eval(funcCall);
		 }
	 }
}
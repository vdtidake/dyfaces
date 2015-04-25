/**
 * 
 */

var dyhighlightRegion = function(data,userCallback) {
	console.log('highlight data '+JSON.stringify(data))
	
    return function(canvas, area, g) {
		_.each(data, function(d,i){
			var min = moment(d.minX, ["YYYY/MM/DD hh:mm:ss"], true);
			var max = moment(d.maxX, ["YYYY/MM/DD hh:mm:ss"], true);
			
			if(min.isValid() && max.isValid()){
				highlightRegionHelper(canvas, area, g, min, max, d.hexColor);
			}else{
				highlightRegionHelper(canvas, area, g, d.minX, d.maxX, d.hexColor);
			}
			
		});
		if(userCallback != ''){
			var funcCall = userCallback + "(canvas, area, g);";
			eval(funcCall);
		}
    }
};

function highlightRegionHelper(canvas, area, g, start, end, fill) {
	var bottomleft = g.toDomCoords(start, -20);
	var topright = g.toDomCoords(end, +20);

	var left = bottomleft[0];
	var right = topright[0];

	canvas.fillStyle = fill;
	canvas.fillRect(left, area.y, right - left, area.h);
}
/**
 * 
 */

var dyhighlightRegion = function(data) {
	console.log('highlight data '+JSON.stringify(data))
	
    return function(canvas, area, g) {
		_.each(data, function(d,i){
			highlightRegionHelper(canvas, area, g, d.minX, d.maxX, d.hexColor);
		});
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
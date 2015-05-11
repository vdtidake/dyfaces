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
		 /**
		  * remove json circular reference (Chrome bug)
		  */
		 var pointsJson = [];
		 _.each(points, function(d){
			 pointsJson.push({canvasx: d.canvasx,canvasy: d.canvasy,idx: d.idx,name: d.name,x: d.x,xval: d.xval,y: d.y,yval: d.yval});
		 });
		 $('#'+graphId+'closestPoints').val(JSON.stringify(pointsJson));
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
var dyAnnotationClickHandlerFn = function(userCallback,ajaxFn,graphId) {
	 return function(annotation, point, dygraph, event) {
		 if(userCallback != ''){
			 /**
			  * remove json circular reference (Chrome bug)
			  */
			 annotation.div=null;
			 $('#'+graphId+'annotationPoint').val(JSON.stringify(annotation));
			 $('#'+graphId+'selectedPoint').val(JSON.stringify(point));
			 if(ajaxFn != ''){
					eval(ajaxFn);
			  }
			  var funcCall = userCallback + "(annotation, point, dygraph, event);";
			  eval(funcCall);
		 }
	 }
}
var dyAnnotationDblClickHandlerFn = function(userCallback,ajaxFn,graphId) {
	 return function(annotation, point, dygraph, event) {
		 if(userCallback != ''){
				var funcCall = userCallback + "(annotation, point, dygraph, event);";
				eval(funcCall);
		 }
	 }
}
var dyAnnotationMouseOutHandlerFn = function(userCallback) {
	 return function(annotation, point, dygraph, event) {
		 if(userCallback != ''){
				var funcCall = userCallback + "(annotation, point, dygraph, event);";
				eval(funcCall);
		 }
	 }
}
var dyannotationMouseOverHandlerFn = function(userCallback) {
	 return function(annotation, point, dygraph, event) {
		 if(userCallback != ''){
				var funcCall = userCallback + "(annotation, point, dygraph, event);";
				eval(funcCall);
		 }
	 }
}
var dyZoomCallbackFn = function(userCallback,ajaxFn,graphId) {
	 return function(minDate, maxDate, yRanges) {
		 var event = null;
		 if(ajaxFn != ''){
			 var xrange = [minDate, maxDate]
			 $('#'+graphId+'dateWindow').val('['+xrange+']');
			 $('#'+graphId+'valueRange').val('['+yRanges+']');
			 eval(ajaxFn);
		  }
		 if(userCallback != ''){
				var funcCall = userCallback + "(minDate, maxDate, yRanges);";
				eval(funcCall);
		 }
	 }
}

var dyValueFormatterFn = function(userCallback) {
	 return function(num , opts, dygraph) {
		 if(userCallback != ''){
				var funcCall = userCallback + "(num , opts, dygraph);";
				eval(funcCall);
		 }
	 }
}

var dyRollPeriodChangeFn = function(graph,rollPeriodVal){
	if(!($.isNumeric(rollPeriodVal))){
		return;
	}
	graph.updateOptions({rollPeriod:parseInt(rollPeriodVal)});
}

var dyHighlightCallbackFn = function(userCallback,graphId) {
	return function(event, x, points, row, seriesName) {
		//seriesName: name of the highlighted series, only present if highlightSeriesOpts is set.
		 var  api = tooltip.qtip();
		 var tooltipContent = dyTooltipContent(event, x, points, row, seriesName);
         api.set('content.text',tooltipContent);
         //console.log('seriesName '+JSON.stringify(points));
         //api.set('content.title',seriesName);
         api.show(event);
	}
}
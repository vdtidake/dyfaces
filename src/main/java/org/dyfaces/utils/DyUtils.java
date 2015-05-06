package org.dyfaces.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.dyfaces.DyConstants;
import org.dyfaces.data.api.Point;
import org.dyfaces.data.api.impl.DyPoint;

import eu.bengreen.data.utility.DownSampleImpl;

public class DyUtils {

	public static String getDyDate(Object date){
		return DyConstants.dateFormat.format(Date.class.cast(date));
	}
	
	public static String getJSDyDate(Object date){
		return "new Date('"+DyConstants.dateFormat.format(Date.class.cast(date))+"')";
	}

	public static List<Point> desampleData(List<Point> points,Integer threshold) {
		Number[][] desample = null;
		Number[][] pointsToArray = new Number[points.size()][];
		boolean isTime = false;
		List<Point> desamplePoints =new ArrayList<Point>();
		
		for (int i = 0; i < pointsToArray.length; i++) {
			Point p = points.get(i);
			if(i ==0 && p.getxValue() instanceof Date){
				isTime = true;
			}
			pointsToArray[i] = new Number[2];
			if(isTime){
				pointsToArray[i][0] = ((Date)p.getxValue()).getTime();
			}else{
				pointsToArray[i][0] = (Number) p.getxValue();
			}
			pointsToArray[i][1] = p.getyValue();
			
		}

		if(isTime){
			desample = DownSampleImpl.largestTriangleThreeBucketsTime(pointsToArray, threshold);
		}else{
			desample = DownSampleImpl.largestTriangleThreeBuckets(pointsToArray, threshold);
		}
		
		for (Number[] point : desample) {
			if(!isTime){
				desamplePoints.add(new DyPoint(point[0].longValue(), point[1]));
			}else{
				desamplePoints.add(new DyPoint(new Date(point[0].longValue()), point[1]));
			}
		}
		return desamplePoints;
	}
}

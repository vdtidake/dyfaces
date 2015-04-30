package org.dyfaces;

import java.text.SimpleDateFormat;

public class DyConstants {
	public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
	public enum UIOutputResourceType{Style,Script}
	public class Callback{
		public static final String ClickCallback = "clickCallback";
		public static final String DrawCallback = "drawCallback";
		public static final String HighlightCallback = "highlightCallback";
		public static final String PointClickCallback = "pointClickCallback";
		public static final String UnderlayCallback = "underlayCallback";
		public static final String UnhighlightCallback = "unhighlightCallback";
		public static final String ZoomCallback = "zoomCallback";
	}
	public class EventHandler{
		public static final String AnnotationClickHandler = "annotationClickHandler";
		public static final String AnnotationDblClickHandler = "annotationDblClickHandler";
		public static final String AnnotationMouseOutHandler = "annotationMouseOutHandler";
		public static final String AnnotationMouseOverHandler = "annotationMouseOverHandler";
	}
}

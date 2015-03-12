package ajmas74.experimental.geo;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ajmas74.experimental.util.LargestElements;

public class Distance {

	public double distanceBetween (double x1, double y1, double x2, double y2 ) {
		
		double dx = x1 - x2;
		double dy = y1 - y2;
		
		return Math.sqrt( dx*dx + dy*dy ); 
		
	}
	
	public double distanceBetween(Point2D point1, Point2D point2) {
		return distanceBetween(point1.getX(), point1.getY(), point2.getX(), point2.getY());
	}

	
	public Point2D findClosestPoint (Point2D refPoint, List<Point2D> points) {
		
		double closestDistance = Double.MAX_VALUE;
		Point2D closestPoint = null;
		
		for (int i=0; i<points.size(); i++) {
			double distance = distanceBetween(refPoint,points.get(i));
			if (distance < closestDistance) {
				closestDistance = distance;
				closestPoint = points.get(i);
			}
		}
	
		return closestPoint;
	}
	
	
	public List<Point2D> findClosestPoints (Point2D refPoint, List<Point2D> points, int pointCount) {
		LargestElements<DistanceAndIndex> largestIntegers = new LargestElements<DistanceAndIndex>(pointCount);
		
		for (int i=0; i<points.size(); i++) {
			largestIntegers.add(new DistanceAndIndex(distanceBetween(refPoint,points.get(i)), i));
		}
		
		List<DistanceAndIndex> elements = largestIntegers.getElements();
		
		List<Point2D> largestPoints = new ArrayList<Point2D>();
		
		for (int i=0; i<elements.size(); i++) {
			largestPoints.add(points.get(elements.get(i).index));
		}
		
		return largestPoints;
	}
	
	class DistanceAndIndex implements Comparable<DistanceAndIndex>{
		double distance;
		int index;
		
		public DistanceAndIndex(double distance, int index) {
			this.distance = distance;
			this.index = index;
		}

		@Override
		public int compareTo(DistanceAndIndex o) {
			// TODO Auto-generated method stub
			return (int) (distance - o.distance);
		}
		
		
		
	}
	
	public static void main(String[] args)  {
		
		LargestElements<Integer> largestIntegers = new LargestElements<Integer>(5);
		
		Integer[] integers = new Integer[] { 2, 3, 4, 1, 0, 3, 9, 3, 5, 8, 10, 20, 30, 15, 16, 100, 1, 0, 25, 60, 0, 99, 98 };
		
		for (int i=0; i<integers.length; i++) {
			integers[i] = (int)(Math.random() * 300);
		}
		
		System.out.println(Arrays.toString(integers));
		
		for (int i=0; i<integers.length; i++) {
			largestIntegers.add(integers[i]);
		}
		
		System.out.println(largestIntegers.getElements());
		
		Arrays.sort(integers);
		System.out.println(Arrays.toString(integers));
		
		
	}
	
}

package main.arithmetic.TSPGA;

import main.arithmetic.data.SimUtils;
import main.entity.Grid;
import main.entity.Map;
import main.entity.geometry.LineSegment;
import main.entity.geometry.Point;

/**
 * The main Evaluation class for the TSP. It's pretty simple -- given an
 * Individual (ie, a chromosome) and a list of canonical cities, calculate the
 * total distance required to travel to the cities in the specified order. The
 * result returned by getDistance() is used by GeneticAlgorithm.calcFitness.
 * 
 * @author bkanber
 *
 */

public class Route {
	private LineSegment[] route;
	private Point start;
	private double distance = 0;

	/**
	 * Initialize Route
	 * 
	 * @param individual
	 *            A GA individual
	 * @param gridLines
	 *            The cities referenced
	 */
	public Route(Individual individual, LineSegment gridLines[],Point start) {
		// Get individual's chromosome
		int[] chromosome = individual.getChromosome();
		// Create route
		this.route = new LineSegment[gridLines.length];
		for (int geneIndex = 0; geneIndex < chromosome.length; geneIndex++) {
			this.route[geneIndex] = gridLines[chromosome[geneIndex]];
		}
		this.start=start;
	}

	/**
	 * Get route distance
	 * 
	 * @return distance The route's distance
	 */
	public double getDistance() {

		// Loop over cities in route and calculate route distance
		double totalDistance = 0;
		Point current = start;
		for (int lineIndex = 0; lineIndex + 1 < this.route.length; lineIndex++) {
			double len1 = Grid.distanceOfTwoPoints(current, route[lineIndex].endPoint1);
			double len2 = Grid.distanceOfTwoPoints(current, route[lineIndex].endPoint2);
			if(len1>len2) {
				totalDistance += len2;
				current=this.route[lineIndex].endPoint1;
			}else {
				totalDistance += len1;
				current=this.route[lineIndex].endPoint2;
			}
			totalDistance += this.route[lineIndex].length;
		}

		totalDistance += Grid.distanceOfTwoPoints(current, start);
		this.distance = totalDistance;
		return totalDistance;
	}

	public LineSegment[] route() {
		return route;
	}
}
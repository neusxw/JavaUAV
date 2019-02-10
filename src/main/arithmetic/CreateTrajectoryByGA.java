package main.arithmetic;

import java.util.ArrayList;
import java.util.List;

import main.arithmetic.TSPGA.GeneticAlgorithm;
import main.arithmetic.TSPGA.Population;
import main.arithmetic.TSPGA.Route;
import main.arithmetic.data.SimUtils;
import main.entity.Grid;
import main.entity.Map;
import main.entity.geometry.LineSegment;
import main.entity.geometry.Point;

public class CreateTrajectoryByGA {
	GeneticAlgorithm ga;
	public List<Point> createTrajectory() {
		int maxGenerations = 10000;
		// Create cities
		int numLines = Grid.size();
		LineSegment gridLines[] = new LineSegment[numLines];
		Point start = Map.getInstance().UAVs.get(0).getTakeOffPoint();

		// Loop to create random cities
		for (int cityIndex = 0; cityIndex < numLines; cityIndex++) {
			gridLines[cityIndex] = Map.getInstance().gridLines.get(cityIndex);
		}

		// Initial GA
		ga = new GeneticAlgorithm(100, 0.005, 0.9, 2, 5);

		// Initialize population
		Population population = ga.initPopulation(gridLines.length);

		// Evaluate population
		ga.evalPopulation(population, gridLines,start);

		Route startRoute = new Route(population.getFittest(0), gridLines,start);
		System.out.println("Start Distance: " + startRoute.getDistance());

		// Keep track of current generation
		int generation = 1;
		// Start evolution loop
		while (ga.isTerminationConditionMet(generation, maxGenerations) == false) {
			// Print fittest individual from population
			Route route = new Route(population.getFittest(0), gridLines,start);
			System.out.println("G"+generation+" Best distance: " + route.getDistance());

			// Apply crossover
			population = ga.crossoverPopulation(population);
			// Apply mutation
			population = ga.mutatePopulation(population);
			// Evaluate population
			ga.evalPopulation(population, gridLines,start);

			// Increment the current generation
			generation++;
		}

		System.out.println("Stopped after " + maxGenerations + " generations.");
		Route route = new Route(population.getFittest(0), gridLines,start);
		System.out.println("Best distance: " + route.getDistance());
		
		List<Point> trajectory =new ArrayList<Point>();
		trajectory.add(start);
		Point current = start;
		for(LineSegment lineSegment:route.route()) {
			Point next= getNext(current,lineSegment);
			Point next2 = Grid.getBrotherPoint(next);
			System.out.println("_______________________");
			System.out.println(current);
			System.out.println(next);
			System.out.println(Grid.getConnectedRelation(current, next));
			addTrajectory(trajectory,current, next);
			trajectory.add(next2);
			current=next2;
		}
		addTrajectory(trajectory,current, start);
		return trajectory;
	}

	private Point getNext(Point point,LineSegment lineSegment) {
		if (SimUtils.doubleEqual(point.distanceToLineSegment(lineSegment, "mindis"), 
				point.distanceToPoint(lineSegment.endPoint1))) {
			return lineSegment.endPoint1;
		}else {
			return lineSegment.endPoint2;
		}
	}
	
	private void addTrajectory(List<Point> trajectory,Point point1,Point point2) {
		if (!Grid.getConnectedRelation(point1,point2)) {
			List<Point> path = Grid.getPath(point1,point2);
			path.remove(path.indexOf(point1));
			for(Point point:path) {
				trajectory.add(point);
			}
		}else {
			trajectory.add(point2);
		}
	}

}

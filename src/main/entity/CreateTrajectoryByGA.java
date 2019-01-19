package main.entity;

import java.util.ArrayList;
import java.util.List;

import main.arithmetic.SimUtils;
import main.arithmetic.GA.GeneticAlgorithm;
import main.arithmetic.GA.Population;
import main.arithmetic.GA.Route;
import main.entity.geometry.LineSegment;
import main.entity.geometry.Point;

public class CreateTrajectoryByGA implements CreateTrajectory {
	GeneticAlgorithm ga;
	@Override
	public List<Point> createTrajectory() {
		int maxGenerations = 1000;
		// Create cities
		int numLines = Map.getInstance().grid.size();
		LineSegment gridLines[] = new LineSegment[numLines];
		Point start = Map.getInstance().UAVs.get(0).getTakeOffPoint();

		// Loop to create random cities
		for (int cityIndex = 0; cityIndex < numLines; cityIndex++) {
			gridLines[cityIndex] = Map.getInstance().grid.getGridLines().get(cityIndex);
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
			Point next2 = Map.getInstance().grid.getBrotherPoint(next);
			if (Map.getInstance().isConnected(current, next)) {
				List<Point> path = Map.getInstance().getPath(current, next);
				path.remove(path.indexOf(current));
				for(Point point:path) {
					trajectory.add(point);
				}
			}else {
				trajectory.add(next);
			}
			trajectory.add(next2);
			current=next2;
		}
		trajectory.add(start);
		return trajectory;
	}

	public Point getNext(Point point,LineSegment lineSegment) {
		if (SimUtils.doubleEqual(point.distanceToLineSegment(lineSegment, "mindis"), 
				point.distanceToPoint(lineSegment.endPoint1))) {
			return lineSegment.endPoint1;
		}else {
			return lineSegment.endPoint2;
		}
	}

}

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
	LineSegment[] lines;
	Point start;
	public CreateTrajectoryByGA(List<LineSegment> lineList,Point start) {
		for(int i=0;i<lineList.size();i++) {
			lines[i]=lineList.get(i);
		}
		this.start=start;
	}
	
	public CreateTrajectoryByGA(LineSegment[] lines,Point start) {
		this.lines=lines;
		this.start=start;
	}

	public List<Point> createTrajectory() {
		int maxGenerations = 1000;
		// Create cities
		int numLines = lines.length;

		// Initial GA
		ga = new GeneticAlgorithm(100, 0.005, 0.9, 2, 5);

		// Initialize population
		Population population = ga.initPopulation(lines.length);

		// Evaluate population
		ga.evalPopulation(population, lines,start);

		Route startRoute = new Route(population.getFittest(0), lines,start);
		System.out.println("Start Distance: " + startRoute.getDistance());

		// Keep track of current generation
		int generation = 1;
		// Start evolution loop
		while (ga.isTerminationConditionMet(generation, maxGenerations) == false) {
			// Print fittest individual from population
			Route route = new Route(population.getFittest(0), lines,start);
			System.out.println("G"+generation+" Best distance: " + route.getDistance());

			// Apply crossover
			population = ga.crossoverPopulation(population);
			// Apply mutation
			population = ga.mutatePopulation(population);
			// Evaluate population
			ga.evalPopulation(population, lines,start);

			// Increment the current generation
			generation++;
		}

		System.out.println("Stopped after " + maxGenerations + " generations.");
		Route route = new Route(population.getFittest(0), lines,start);
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

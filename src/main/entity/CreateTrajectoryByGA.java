package main.entity;

import main.arithmetic.GA.City;
import main.arithmetic.GA.GeneticAlgorithm;
import main.arithmetic.GA.Population;
import main.arithmetic.GA.Route;

public class CreateTrajectoryByGA implements CreateTrajectory {
	GeneticAlgorithm ga;
	@Override
	public void createTrajectory() {
		int maxGenerations = 10000;
			// Create cities
			int numCities = Map.getInstance().gridLines.size();
			City cities[] = new City[numCities];
			
			// Loop to create random cities
			for (int cityIndex = 0; cityIndex < numCities; cityIndex++) {
				// Generate x,y position
				int xPos = (int) (100 * Math.random());
				int yPos = (int) (100 * Math.random());
				
				// Add city
				cities[cityIndex] = new City(xPos, yPos);
			}

			// Initial GA
			ga = new GeneticAlgorithm(100, 0.001, 0.9, 2, 5);

			// Initialize population
			Population population = ga.initPopulation(cities.length);

			// Evaluate population
			ga.evalPopulation(population, cities);

			Route startRoute = new Route(population.getFittest(0), cities);
			System.out.println("Start Distance: " + startRoute.getDistance());

			// Keep track of current generation
			int generation = 1;
			// Start evolution loop
			while (ga.isTerminationConditionMet(generation, maxGenerations) == false) {
				// Print fittest individual from population
				Route route = new Route(population.getFittest(0), cities);
				System.out.println("G"+generation+" Best distance: " + route.getDistance());

				// Apply crossover
				population = ga.crossoverPopulation(population);

				// Apply mutation
				population = ga.mutatePopulation(population);

				// Evaluate population
				ga.evalPopulation(population, cities);

				// Increment the current generation
				generation++;
			}
			
			System.out.println("Stopped after " + maxGenerations + " generations.");
			Route route = new Route(population.getFittest(0), cities);
			System.out.println("Best distance: " + route.getDistance());
	}

}

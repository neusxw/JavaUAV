package main.arithmetic.TSPGA;

import java.util.Arrays;
import java.util.Random;

public class Individual {

	/**
	 * In this case, the chromosome is an array of integers rather than a string. 
	 */
	private int[] chromosome;
	private double fitness = -1;

	/**
	 * Initializes individual with specific chromosome
	 * 
	 * @param chromosome
	 *            The chromosome to give individual
	 */
	public Individual(int[] chromosome) {
		// Create individualchromosome
		this.chromosome = chromosome;
	}

	/**
	 * Initializes random individual
	 * 
	 * @param chromosomeLength
	 *            The length of the individuals chromosome
	 */
	public Individual(int chromosomeLength) {
		chromosome = new int[chromosomeLength];
		Arrays.fill(chromosome, -1);
		for (int i = 0; i < chromosomeLength; i++) {
			Random rand = new Random();
			while(true) {
				int gene = rand.nextInt(chromosomeLength);
				if(!containsGene(gene)) {
					chromosome[i] = gene;
					break;
				}
			}
		}
	}

	/**
	 * Gets individual's chromosome
	 * 
	 * @return The individual's chromosome
	 */
	public int[] getChromosome() {
		return this.chromosome;
	}

	/**
	 * Gets individual's chromosome length
	 * 
	 * @return The individual's chromosome length
	 */
	public int getChromosomeLength() {
		return this.chromosome.length;
	}

	/**
	 * Set gene at offset
	 * 
	 * @param gene
	 * @param offset
	 */
	public void setGene(int offset, int gene) {
		this.chromosome[offset] = gene;
	}

	/**
	 * Get gene at offset
	 * 
	 * @param offset
	 * @return gene
	 */
	public int getGene(int offset) {
		return this.chromosome[offset];
	}

	/**
	 * Store individual's fitness
	 * 
	 * @param fitness
	 *            The individuals fitness
	 */
	public void setFitness(double fitness) {
		this.fitness = fitness;
	}

	/**
	 * Gets individual's fitness
	 * 
	 * @return The individual's fitness
	 */
	public double getFitness() {
		return this.fitness;
	}

	public String toString() {
		String output = "";
		for (int gene = 0; gene < this.chromosome.length; gene++) {
			output += this.chromosome[gene] + ",";
		}
		return output;
	}

	/**
	 * Search for a specific integer gene in this individual.
	 * 
	 * For instance, in a Traveling Salesman Problem where cities are encoded as
	 * integers with the range, say, 0-99, this method will check to see if the
	 * city "42" exists.
	 * 
	 * @param gene
	 * @return
	 */
	public boolean containsGene(int gene) {
		for (int i = 0; i < this.chromosome.length; i++) {
			if (this.chromosome[i] == gene) {
				return true;
			}
		}
		return false;
	}



}

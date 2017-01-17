package main.controller;

import java.util.Random;


import main.data.Vector;
import main.controller.exceptions.BadParameterException;

public class CoordinateUtils {

	 
	 private Random random;
	 
	 
	 public CoordinateUtils() {
		 random = new Random();
	 }
	 
	 
	/**
	 * generates a vector with random values of approximate given length
	 * @param length
	 * @return
	 */
	 public Vector generateRandom(int length) throws BadParameterException{
		if (length <= 0) {
			throw new BadParameterException("length","> 0");
		}
		 
		// any direction
		double direct_x = Math.random();
		double direct_y = Math.random();
			
		// normalize 
		double d = Math.sqrt(Math.pow(direct_x, 2)+ Math.pow(direct_y, 2));
		direct_x = direct_x / d;
		direct_y = direct_y / d;
			
		// possibly negative
		double neg = Math.random();	
		if (neg > 0.5) {
			direct_x = -1.0 * direct_x;
		}
		 neg = Math.random();	
		if (neg > 0.5) {
			direct_y = -1.0 * direct_y;
		}
		return new Vector(Math.toIntExact(Math.round(length*direct_x)), Math.toIntExact(Math.round(length*direct_y)));
	 }
		
		
	 /**
	  * generates a vector that deviates by given fraction randomly from given vector
	  * @param orig
	  * @param length
	  * @return
	 * @throws BadParameterException 
	  */
	public Vector generateDeviating(Vector orig, int length, double deviation) throws BadParameterException  {
		if (deviation < 0 || deviation > 1) {
			throw new BadParameterException("deviation", "0 <= p <= 1");
		}
		if (length <= 0) {
			throw new BadParameterException("length", "> 0");
		}
		
		double neg = Math.random();	
		double r = Math.random() * deviation;
		if (neg > 0.5) 
			r = -1.0* r;
		double d = Math.sqrt(Math.pow(orig.x, 2)+ Math.pow(orig.y, 2));
		double direct_x = orig.x / d;
		double direct_y = orig.y / d;
		
		//add deviations to vectors
		direct_x = direct_x + direct_x*r;
		r = Math.random() * length;
		neg = Math.random();	
		if (neg > 0.5) 
			r = -1.0* r;
		direct_y = direct_y + direct_y * r;
	
		return new Vector(Math.toIntExact(Math.round(length*direct_x)), Math.toIntExact(Math.round(length*direct_y)));
	}
	
	/**
	 * generates random vector where values are positive and limited by given values
	 * @return
	 */
	public Vector generateRandom(int x, int y) throws BadParameterException {
		if (x <= 0) {
			throw new BadParameterException("x", "> 0");
		}
		if (y <= 0) {
			throw new BadParameterException("y", "> 0");
		}
		double randx = Math.random() * x;
		double randy = Math.random() * y;
		return new Vector(Math.toIntExact(Math.round(randx)), Math.toIntExact(Math.round(randy)));
	}
	
	/**
	 * generates a position that is gauss distributed at given center within given radius
	 * @param center
	 * @param rad
	 * @return
	 * @throws BadParameterException 
	 */
	public Vector makeGaussPosition(Vector center, int rad) throws BadParameterException {
		if (rad <= 0) {
			throw new BadParameterException("Radius", "> 0");
		}
		double gaussPosx = random.nextGaussian() * rad;
		double gaussPosy = random.nextGaussian() * rad;
		return new Vector(Math.toIntExact(Math.round(center.x+gaussPosx)), Math.toIntExact(Math.round(center.y+gaussPosy)));
	}


	/**
	 * generates the vector that connects the given locations
	 * @param target
	 * @param position
	 * @return
	 */
	public Vector direct(Vector target, Vector origin) {
		return new Vector(target.x - origin.x, target.y - origin.y);
	}


	/**
	 * euclidean distance between position
	 * @param a
	 * @param b
	 * @return
	 */
	public double distance(Vector a, Vector b) {
		double x = Math.pow((double)a.x-(double)b.x,2);
		double y = Math.pow((double)a.y-(double)b.y,2);
		
		return  Math.sqrt(x + y);
	}
	

}

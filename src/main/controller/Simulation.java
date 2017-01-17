package main.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import main.controller.exceptions.BadParameterException;
import main.data.Information;
import main.data.Member;
import main.data.Net;
import main.data.Vector;
import main.view.View;


public class Simulation {
	
	/**
	 * the net
	 */
	Net net;
	
	/**
	 * utils for random position and vector generation
	 */
	private CoordinateUtils utils;

	/**
	 * only one target
	 */
	private Member target;
	
	/**
	 * tick delay in ms
	 */
	static final int DELAY = 1000;
	
	/**
	 * fixed window size x
	 */
	static final int X_AXIS = 950;
	
	/**
	 * fixed window size y
	 */
	static final int Y_AXIS = 450;
	
	/**
	 * max. raduis s.t. connection is stable
	 */
	static final int RADIUS = 20;
	
	/**
	 * connection error rate
	 */
	static final double CONNECTION_ERROR_RATE = 0.1; 
	
	/**
	 * number members
	 */
	static final int SIZE_MEMBERS = 1000;
	
	/**
	 * number of gaussian centers
	 */
	static final int NUM_GAUSS_CENTERS = 3;
	
	/**
	 * fraction of members that do not head the target
	 */
	static final double NOT_HEADING_TARGET = 0.7;
	
	/**
	 * approximate amount of pixels that a member moves per tick (not exact due to inaccuracies on purpose) 
	 */
	static final int MOVEMENT_SPEED = 5;
	
	/**
	 * fraction of moving members that do not move randomly (approximation of reality)
	 */
	static final double NOT_MOVING_RATE = 0.90;
	
	/**
	 * the movement direction deviates by this rate from the original direction for those members, who are heading to the target (approximation of reality)
	 */
	static final double MOVEMENT_DEVIATION = 0.3; //% movement deviation
	
	/**
	 * Radius of Gauss centers
	 */
	static final int GAUSS_RADIUS = 50;
	
	/**
	 * fraction of those members who do not move (because e.g. they are injured)
	 */
	static final double NOT_MOVING = 0.5;
	
	
	/**
	 * program entry point
	 * @param args
	 * @throws InterruptedException
	 * @throws BadParameterException 
	 */
	public static void main (String[] args) throws InterruptedException, BadParameterException {

		Simulation s = new Simulation();
		
		View v = new View(s.net, X_AXIS+ 50, Y_AXIS+50);	
		v.print();

		/**
		 * only manually break
		 */
		while(true) {
			Thread.sleep(DELAY);
			s.tick();
			v.print();
		}
		

	}
	
	/**
	 * inizalizes Net according to constants, i.e. distributes members and sets member parameters
	 * @return
	 * @throws BadParameterException 
	 */
	private  void initNet() throws BadParameterException {
		Member[] members = new Member[SIZE_MEMBERS];
		
		
		//target
		Information in = new Information(0);
		target = new Member(utils.generateRandom(X_AXIS, Y_AXIS));
		target.isTarget = true;
		target.movable = false;
		members[0] = target;
		
		int m = Math.round(SIZE_MEMBERS/(2*NUM_GAUSS_CENTERS));
		//unique information is assigned to first moving instance
		boolean informationSet = false;
		
		//not in gaussian centers
		for (int i = 1; i < SIZE_MEMBERS-(NUM_GAUSS_CENTERS*m); i++) {
			members[i] = new Member(utils.generateRandom(X_AXIS, Y_AXIS));

			//moving?
			double r = Math.random();
			if (r < NOT_MOVING) {
				if (! informationSet) {
					// information carrier
					members[i].info = Optional.of(in);
					informationSet = true;
				}
				members[i].movable = false;
				
			//check if only randomly moving, or heading target
			} else if (r > NOT_HEADING_TARGET)
				members[i].headsTarget = true;
		}
		
		//in gaussian centers
		for (int j = 0; j < NUM_GAUSS_CENTERS; j++) {
			Vector center = new Vector(Math.toIntExact(Math.round(Math.random() * X_AXIS)), Math.toIntExact(Math.round(Math.random() * Y_AXIS)));
			for (int i = SIZE_MEMBERS-(NUM_GAUSS_CENTERS*m)+j*m; i<SIZE_MEMBERS-(NUM_GAUSS_CENTERS*m)+(j+1)*m; i++) {
				
				// rounding problems
				if (i < SIZE_MEMBERS) {
					members[i] = new Member(utils.makeGaussPosition(center, GAUSS_RADIUS));
					double r = Math.random();
					if (r < NOT_MOVING) {
					
						members[i].movable = false; 
					} else if (r > NOT_HEADING_TARGET)
						members[i].headsTarget = true;
				}
			}
		}
		
		net = new Net(members);
	}
	

	public Simulation() throws BadParameterException {
		this.utils = new CoordinateUtils();
		initNet();
	}
	
	
	/**
	 * generate random value and check if it meets moving rate threshold
	 * @return
	 */
	private static boolean moves() {
		return Math.random() <= NOT_MOVING_RATE;
	}
	
	/**
	 * generate random value and check if it meets error threshold
	 * @return
	 */
	private static boolean connectionSucceeds() {
		return Math.random() >= CONNECTION_ERROR_RATE;
	}
	
	
	/**
	 * perform one movement step
	 * @throws BadParameterException 
	 */
	public void tick() throws BadParameterException {
		
		//movement
		net.forAllMembers((ThrowingConsumer<Member>)m -> { 
			if (m.movable) {
				// check if moving
				if (moves()) {
					if (!m.headsTarget) {
					// any direction
						m.move(utils.generateRandom(MOVEMENT_SPEED));
					} else {
						// direction of target with small deviation
						m.move(utils.generateDeviating(utils.direct(target.position,m.position), MOVEMENT_SPEED, MOVEMENT_DEVIATION));
					}
				}
			}
		});
		
		//connections
		List<Member> rad = new ArrayList<>();
		List<Member> assigned = new ArrayList<>();
		
		//generate neighborhood. Worst Case quadratic in net size, but it's okay for small instances (<= 1k, depending on constants)
		//of course could do way better (e.g. init with some clustering), but keeping it simple since it's only for demonstration issues
		for (int i = 0; i < net.size(); i++) {
			if (! assigned.contains(net.getMember(i))) 
			for (int j = 0; j < net.size(); j++) {
				// no self connection
				if (i == j) break;
				// connections alternate
				if ( (net.getMember(i).connection.isPresent()) && (net.getMember(i)).connection.get() == net.getMember(j)) break;
				if (! assigned.contains(net.getMember(j))) {
					//check radius
					if (utils.distance(net.getMember(i).position, net.getMember(j).position)<=RADIUS) {
						rad.add(net.getMember(j));
				}
				}
			}
			
			//apply connections and exchange information
			net.getMember(i).connection = Optional.empty();
			if (rad.size() > 0) {
				// generate candidate from neighborhood
				int candidate = (int) Math.floor(Math.random()*rad.size());
				if (connectionSucceeds()) {
					net.getMember(i).connection = Optional.of(rad.get(candidate));
					assigned.add(net.getMember(i));
					assigned.add(rad.get(candidate));
					//exchange information
					Member m = net.getMember(i);
					net.getMember(i).info.ifPresent(in -> rad.get(candidate).info = Optional.of(in));
					rad.get(candidate).info.ifPresent(in -> m.info = Optional.of(in));
				}
			}
			rad.clear();
			
		}
	}
	

}

package main.data;

import java.util.Optional;

/**
 * Net Member, has position in map and is a potential information carrier
 * @author Nico
 *
 */
public class Member {
	
	/**
	 * counts all members
	 */
	public static int count = 0;	
	
	/**
	 * position
	 */
	public Vector position;
	
	/**
	 * unique identifier
	 */
	public int id ;
	
	/**
	 * information to be spread
	 */
	public Optional<Information> info;
	
	/**
	 * potential connection to other member in radius
	 */
	public Optional<Member> connection;
	
	/**
	 * indicates if this member is still (i.e. injured) or moves
	 */
	public boolean movable;
	
	/**
	 * indicates if this member moves to dedicated target or moves randomly
	 */
	public boolean headsTarget;
	
	/**
	 * indicates if this member is a target
	 */
	public boolean isTarget;
	
	
	public Member(Vector pos) {
		id = count++;
		position = pos;
		
		//default values
		info = Optional.empty();
		movable = true;
		headsTarget = false;
		isTarget = false;
		connection = Optional.empty();
	}
	
	/**
	 * moves this instance by given direction
	 * @param v
	 */
	public void move(Vector v) {
		position.add(v);
	}
	
	@Override
	public String toString() {
		return position.toString();
	}
	

}

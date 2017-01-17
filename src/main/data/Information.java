package main.data;

/**
 * Information that is carryable by members
 * @author Nico
 *
 */
public class Information {

	/**
	 * unique identifier
	 */
	private final int id;
	
	public Information(int id) {
		this.id = id;
	}
	
	public int getId() {
		return id;
	}
	
}

package main.data;



public class Vector extends Tuple<Integer, Integer>{
	

	
	public Vector(int x, int y) {
		super(x,y);
	}

	/**
	 * pointwise add
	 * @param v
	 */
	public void add(Vector v) {
		x += v.x;
		y += v.y;
	}
	
	@Override
	public String toString() {
		return ""+x+" "+y;
	}
	
}

package main.data;

import java.util.function.Consumer;

public class Net {
	

	
	/**
	 * all Members
	 */
	private Member[] members;
	
	/**
	 * constructor which sets member array directly
	 * @param members
	 */
	public Net(Member[] members) {
		this.members = members;
	}
	
	/**
	 * Constructor that generates an empty member set of given size
	 * @param length
	 */
	public Net(int length) {
		this.members = new Member[length];
	}
	
	/**
	 * 
	 * @param i 
	 * @param m
	 * @return true if entry was set successfully, false if i is greater than net size 
	 */
	public boolean setMember(int i, Member m) {
		if (i <= members.length) {
			members[i] = m;
			return true;
		}
		return false;
	}
	
	
	/**
	 * 
	 * @param i
	 * @return Member at given entry if i is not greater than net size. else null value
	 */
	public Member getMember(int i) {
		if (i <= members.length) {
			return members[i];
		} else {
			return null;
		}
	}
	
	/**
	 * 
	 * @return number of members
	 */
	public int size() {
		return members.length;
	}
	
	/**
	 * apply function on each member
	 */
	public void forAllMembers(Consumer<? super Member> action) {
		for (Member m : members) {
			action.accept(m);
		}
	}
	
	


}

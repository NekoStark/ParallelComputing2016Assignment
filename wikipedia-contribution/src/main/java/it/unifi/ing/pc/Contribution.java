package it.unifi.ing.pc;

public class Contribution {

	protected String timestamp;
	protected Integer id;
	protected Integer contributorId;
	protected String username;
	
	public Contribution(String input) {
		String[] split = input.split(" ");
		this.timestamp = split[0];
		this.id = Integer.valueOf( split[1] );
		this.contributorId = Integer.valueOf( split[2] );
		this.username = split[3];
	}
	
}

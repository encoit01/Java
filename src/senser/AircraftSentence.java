package senser;

public class AircraftSentence
{
	String aircraft;

	//TODO: Create constructor
	public AircraftSentence(String naircraft)
	{
		aircraft = naircraft;
	}

	//TODO: Create relevant getter methods

	public String getAircraft() {
		return aircraft;
	}


	//TODO: Overwrite toString() method to print our relevant fields
	public String toString() {
		return this.aircraft;
	}
}

package messer;

public class Coordinate {
	private double latitude;
	private double longitude;
	
	//TODO: Constructor, Getter/Setter and toString()
	Coordinate(double latitude, double longitude)
	{
		this.latitude = latitude;
		this.longitude = longitude;
	}

	//Setter
	public void setLatitude(double longitude)
	{
		this.longitude = longitude;
	}

	public void setLongitude(double longitude)
	{
		this.longitude = longitude;
	}

	//Getter
	public double getLatitude()
	{
		return this.latitude;
	}

	public double getLongitude()
	{
		return this.longitude;
	}

	//To-String
	public String toString()
	{

		return (String.valueOf(longitude)+ "," +String.valueOf(latitude));
	}
}
package messer;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;

public class BasicAircraft {
	private String icao;
	private String operator;
	private Date posTime;
	private Coordinate coordinate;
	private Double speed;
	private Double trak;

	
	//TODO: Create constructor
	BasicAircraft(String icao, String operator, Date postTime, Coordinate coordinate, Double speed, Double trak)
	{
		this.icao = icao;
		this.operator = operator;
		this.posTime = postTime;
		this.coordinate = coordinate;
		this.speed = speed;
		this.trak = trak;
	}

	//TODO: Create relevant getter methods
	public Coordinate getCoordinate() {
		return coordinate;
	}

	public Date getPosTime() {
		return posTime;
	}

	public Double getSpeed() {
		return speed;
	}

	public Double getTrak() {
		return trak;
	}

	public String getIcao() {
		return icao;
	}

	public String getOperator() {
		return operator;
	}


	//TODO: Lab 4-6 return attribute names and values for table
	public static ArrayList<String> getAttributesNames()
	{
		ArrayList<String> attributes = new ArrayList<String>();

		// get titles of columns
		attributes.add("icao");
		attributes.add("operator");
		attributes.add("posTime");
		attributes.add("coordinate");
		attributes.add("speed");
		attributes.add("trak");
		
		return attributes;
	}

	public static ArrayList<Object> getAttributesValues(BasicAircraft ac)
	{
		ArrayList<Object> attributes = new ArrayList<Object>();
		
		// get values of one plane
		attributes.add(ac.getIcao());
		attributes.add(ac.getOperator());
		attributes.add(ac.getPosTime());
		attributes.add(ac.getCoordinate());
		attributes.add(ac.getSpeed());
		attributes.add(ac.getTrak());

		return attributes;
	}

	@Override
	public String toString()
	{
		return (icao + "," +operator+ "," +String.valueOf(posTime)+ "," +String.valueOf(coordinate)+ "," +String.valueOf(speed)+ "," +String.valueOf(trak));
	}

	//TODO: Overwrite toString() method to print fields
}

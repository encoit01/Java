package senser;

import kotlin.collections.ArraysKt;
import kotlin.coroutines.experimental.CoroutineContext;

import java.util.ArrayList;

public class AircraftSentenceFactory
{
	public ArrayList<AircraftSentence> fromAircraftJson(String jsonAircraftList)
	{	
		ArrayList<AircraftSentence> aircraftList = new ArrayList<AircraftSentence>();
		
		//TODO: Get distinct aircrafts from the jsonAircraftList string
		String[] list = jsonAircraftList.split("\\],\\[|\\]|\\[");

		//add to Arraylist
		for(String element : list)
		{
			aircraftList.add(new AircraftSentence(element));
		}
		aircraftList.remove(0);
		return aircraftList;
	}
}

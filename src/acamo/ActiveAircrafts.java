package acamo;

import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;
import java.util.ArrayList;
import messer.*;

//TODO: create hash map and complete all operations
public class ActiveAircrafts implements Observer
{
	private HashMap<String, BasicAircraft> activeAircrafts;    	// store the basic aircraft and use its Icao as key
												// replace K and V with the correct class names

	public ActiveAircrafts () {

		activeAircrafts = new HashMap<String, BasicAircraft>();
	}

	public synchronized void store(String icao, BasicAircraft ac) {
		activeAircrafts.put(icao, ac);
	}

	public synchronized void clear() {
		activeAircrafts.clear();
	}

	public synchronized BasicAircraft retrieve(String icao) {
		BasicAircraft ac = activeAircrafts.get(icao);

		if(ac == null){
			return null;
		}
		return ac;
	}

	public synchronized ArrayList<BasicAircraft> values () {
		ArrayList<BasicAircraft> list = new ArrayList<BasicAircraft>(activeAircrafts.values());
		return list;
	}

	public String toString () {
		return activeAircrafts.toString();
	}

	@Override
	// TODO: store arg in hashmap using the method above
	public void update(Observable o, Object arg) {
		BasicAircraft nAircraft = (BasicAircraft) arg;

		activeAircrafts.put(nAircraft.getIcao(), nAircraft);

	}
}
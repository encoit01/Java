package messer;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONObject;
import senser.AircraftSentence;

public class AircraftFactory {

	public BasicAircraft fromAircraftSentence ( AircraftSentence sentence ) {
		//List of Matches
		List<String> matches = new ArrayList<String>();
		JSONObject js = new JSONObject();

		String icao = null;
		String operator = null;
		Date posTime = null;
		double longitude = 0;
		double latitude = 0;
		double speed = 0;
		double trak = 0;

		// ["3c6dc8", "EWG87U  ", "Germany", 1560500800, 1560500800, 9.6884,   48.8508,
		//   icao      operator              posTime                 longitude latitude
		//   3558.54, false, 197.81, 50.8, 13.98, null, 3695.7, "7716", false, 0]
		//                   speed   trak

		// TODO: Extract attributes from sentence using regex only (no String methods)
		Pattern pattern = Pattern.compile("[a-zA-Z0-9. ]+");
		Matcher matcher = pattern.matcher(sentence.toString());

		while(matcher.find())
		{
			int i = 0;
			matches.add(matcher.group());

		}

		//Keys and values
		js.put("icao", matches.get(0));
		js.put("operator", matches.get(1));
		js.put("postTime", Long.parseLong(matches.get(3))*1000);
		js.put("longitude", Double.parseDouble(matches.get(5)));
		js.put("latitude", Double.parseDouble(matches.get(6)));
		js.put("speed", Double.parseDouble(matches.get(9)));
		js.put("trak", Double.parseDouble(matches.get(10)));

		//icao = matches.get(0);
		//operator = matches.get(1);
		//posTime = new Date(Long.parseLong(matches.get(3))*1000);
		//longitude = Double.parseDouble(matches.get(5));
		//latitude = Double.parseDouble(matches.get(6));
		//speed = Double.parseDouble(matches.get(9));
		//trak = Double.parseDouble(matches.get(10));

		//Get values
		icao = js.getString("icao");
		operator = js.getString("operator");
		long pos = js.getLong("postTime");
		posTime = new Date(pos);
		longitude = js.getDouble("longitude");
		latitude = js.getDouble("latitude");
		speed = js.getDouble("speed");
		trak = js.getDouble("trak");

		BasicAircraft msg = new BasicAircraft(icao, operator, posTime, new Coordinate(latitude, longitude), speed, trak);
		
		return msg;
	}
}

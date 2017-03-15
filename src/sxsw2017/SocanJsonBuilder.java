package sxsw2017;

import java.util.HashMap;
import java.util.Map;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonBuilderFactory;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonValue;

public class SocanJsonBuilder {
	public static void main (String [] cheese){
		System.out.println("Hello, World!");
		
		String marie_no = "9999999";
		String canadian_performance = "C";
		String type_of_program = "SC";
		String artist_name = "John Doe";
		
		Map<String, Object> config = new HashMap<String, Object>();
        //if you need pretty printing
        config.put("javax.json.stream.JsonGenerator.prettyPrinting", Boolean.valueOf(true));
        	
		JsonBuilderFactory factory = Json.createBuilderFactory(null);
		JsonObject value = factory.createObjectBuilder()
				.add("MARIE_NO", marie_no)
				.add("CANADIAN_PERFORMANCE", canadian_performance)
				.add("TYPE_OF_PROGRAM", type_of_program)
				.add("ARTIST_NAME", artist_name)		
				.add("PROOF_TYPE", "contract")
				.add("DATE_OF_PROGRAM", "2017/12/10")
				.add("VENUE", "SOCAN")
				.add("VENUETYPE", "cfs")
				.add("VENUECAPACITY", "l5")
				.add("STREET1", "41 Valleybrook")
				.add("CITY", "Toronto")
				.add("PROVINCE", "ON")
				.add("POSTAL_CODE", "m3b 2s6")
				.add("COUNTRY", "can")
				.add("VENUE_PHONE", "4164458700")
				.add("VENUE_WEBSITE", "www.socan.ca")
				.add("PERFORMANCE_TIME", "1 : 30 pm")
				.add("PROMOTER", "SOCAN")
				.add("PROMOTER_STREET1", "41 Valleybrook")
				.add("PROMOTER_CITY", "Toronto")
				.add("PROMOTER_PROVINCE", "ON")
				.add("PROMOTER_POSTAL_CODE", "M3B2S6")
				.add("PROMOTER_COUNTRY", "CAN")
				.add("PROMOTER_TELEPHONE", "4164458700")
				.add("compositions", factory.createArrayBuilder()
				      .add(factory.createObjectBuilder()
				    		  .add("ORIGINAL_TITLE", "SOCAN")
				    		  .add("COMPOSER","John Doe"))
				      .add(factory.createObjectBuilder()
				    		  .add("ORIGINAL_TITLE", "HACKATHON")
				    		  .add("COMPOSER", "John Doe")))			    		  
				.build();
		
		System.out.println(value.toString());
	}
}

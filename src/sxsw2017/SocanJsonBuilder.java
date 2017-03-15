package sxsw2017;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonBuilderFactory;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import javax.json.JsonValue;


class Composition{
	String original_title = "";
	String composer = "";

	public Composition(String ot, String comp){
		original_title = ot;
		composer = comp;
	}
}

public class SocanJsonBuilder {
	public static void main (String [] cheese){
		System.out.println("Hello, World!");

		//TODO: Populate these!//
		String marie_no = "9999999";
		String canadian_performance = "C";
		String type_of_program = "SC";
		String artist_name = "John Doe";
		String proof_type = "contract";
		String date_of_program = "2017/12/10";
		String venue = "SOCAN";
		String venuetype = "cfs";
		String venuecapacity = "15";
		String street1 = "41 Valleybrook";
		String city = "Toronto";
		String province = "ON";
		String postal_code = "m3b 2s6";
		String country = "can";
		String venue_phone = "4164458700";
		String venue_website = "www.socan.ca";
		String performance_time=  "1 : 30 pm";
		String promoter= "SOCAN";
		String promoter_street1 = "41 Valleybrook";
		String promoter_city= "Toronto";
		String promoter_province = "ON";
		String promoter_postal_code = "M3B2S6";
		String promoter_country =  "CAN";
		String promoter_telephone = "4164458700";
		Set <Composition> allCompositions = new HashSet<Composition>();		
		allCompositions.add(new Composition("SOCAN", "John Doe"));
		allCompositions.add(new Composition("HACKATHON", "John Doe"));

		JsonBuilderFactory factory = Json.createBuilderFactory(null);
		JsonObjectBuilder compBuilder = factory.createObjectBuilder();
		JsonArrayBuilder arrayBuilder = factory.createArrayBuilder();

		for (Composition comp : allCompositions){
			arrayBuilder.add(compBuilder.add("ORIGINAL_TITLE", comp.original_title)
					.add("COMPOSER", comp.composer));
		}

		Map<String, Object> config = new HashMap<String, Object>();
		//if you need pretty printing
		config.put("javax.json.stream.JsonGenerator.prettyPrinting", Boolean.valueOf(true));

		JsonObject value = factory.createObjectBuilder()
				.add("MARIE_NO", marie_no)
				.add("CANADIAN_PERFORMANCE", canadian_performance)
				.add("TYPE_OF_PROGRAM", type_of_program)
				.add("ARTIST_NAME", artist_name)		
				.add("PROOF_TYPE", proof_type)
				.add("DATE_OF_PROGRAM", date_of_program)
				.add("VENUE", venue)
				.add("VENUETYPE", venuetype)
				.add("VENUECAPACITY", venuecapacity)
				.add("STREET1", street1)
				.add("CITY", city)
				.add("PROVINCE", province)
				.add("POSTAL_CODE", postal_code)
				.add("COUNTRY", country)
				.add("VENUE_PHONE", venue_phone)
				.add("VENUE_WEBSITE", venue_website)
				.add("PERFORMANCE_TIME", performance_time)
				.add("PROMOTER", promoter)
				.add("PROMOTER_STREET1", promoter_street1)
				.add("PROMOTER_CITY", promoter_city)
				.add("PROMOTER_PROVINCE", promoter_province)
				.add("PROMOTER_POSTAL_CODE", promoter_postal_code)
				.add("PROMOTER_COUNTRY", promoter_country)
				.add("PROMOTER_TELEPHONE", promoter_telephone)
				.add("compositions", arrayBuilder)		    		  
				.build();

		System.out.println(value.toString());
	}
}

package sxsw2017;

import java.util.*;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

import java.util.logging.*;
import java.io.FileReader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.pubnub.api.PNConfiguration;
import com.pubnub.api.PubNub;
import com.pubnub.api.callbacks.PNCallback;
import com.pubnub.api.callbacks.SubscribeCallback;
import com.pubnub.api.enums.PNLogVerbosity;
import com.pubnub.api.models.consumer.PNPublishResult;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.pubsub.PNMessageResult;
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult;

import java.net.*;
import java.io.*;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonBuilderFactory;
import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class Concert {
	/*	final static AccessToken accessToken = new AccessToken("789951371428495360-7aljagVXHXXVySZ94BQNHzG0ZVr2JDI"
			, "dxq40x9zV4gqUHxZflfqT975pEFpUxardfyRinuxi96S7");*/
	//final static Twitter twitter = new TwitterFactory().getInstance(accessToken);
	//final static Twitter twitter = new TwitterFactory().getInstance(accessToken);
	private static String city = "";
	private static String state = "";
	private static String hashtag = "";
	private static String artist = "";
	private static double latitude = 0;
	private static double longitude = 0;
	static ConfigurationBuilder builder = new ConfigurationBuilder();
	static ArrayList<String> setList = new ArrayList<String>();
	static Twitter twitter;
	static boolean addedToList = false;
	Concert(){
		builder =  new ConfigurationBuilder();
		//city = null;
		//state = null;
		latitude = 0;
		longitude = 0;
		//hashtag = null;
		setList = new ArrayList<String>();
	}
	public static void main(String[] args)throws IOException{

		builder.setOAuthConsumerKey("BNvErshlqPIYQUDOPFmvQofPH");
		builder.setOAuthConsumerSecret("3oBEm1MerGR0kJPDOYqhLnoaQlpYFX4pTbjybRo4uIJEjQ0ahJ");
		builder.setOAuthAccessToken("789951371428495360-7aljagVXHXXVySZ94BQNHzG0ZVr2JDI");
		builder.setOAuthAccessTokenSecret("dxq40x9zV4gqUHxZflfqT975pEFpUxardfyRinuxi96S7");
		Configuration configuration = builder.build();
		TwitterFactory factory = new TwitterFactory(configuration);
		twitter = factory.getInstance();


		//	URL url = new URL("http://www.oracle.com/");

		//	makeConnection(url);

		//createJsonString();

		pubNubConnectivity();

		System.out.println(setList.toString());

		//		System.out.println("HEREEEE");

		//		System.out.println(hashtag);



		/*		
        }*/
	}

	static void fetchTwitter(String hashtag, String city, String state) throws JSONException{

		if(hashtag == null)
			throw new IllegalArgumentException("0");

		try {
			//create a query to search twitter with the specific hashtag
			Query query = new Query(hashtag);
			QueryResult result;
			boolean check = false;

			//get results from twitter
			result = twitter.search(query);

			//store the returned tweets in a list
			List<Status> tweets = result.getTweets();

			//TODO send in the state and state to mapquest api
			//TODO set latitude and longitude to the return from the getLat and getLong methods
			addedToList = false;
			for(Status tweet: tweets){
				
				check = false;

				String[] location = tweet.getUser().getLocation().split(", ");
				for(String s : location)
					if(s.equalsIgnoreCase(city) || s.equalsIgnoreCase(state)){
						check = true;
						break;
					}
				String[] text = tweet.getText().split(" ");
				if(!(text[0].equals(hashtag)))
					check = false;

				if(check){
					//System.out.println("@" + tweet.getUser().getScreenName() + " - " + tweet.getText());
					//splitting the text into tokens based on spaces

					MapQuestApi map = new MapQuestApi(city, state);

					latitude = map.getLat();
					longitude = map.getLon();
					artist = text[0].substring(1, text[0].length());

					System.out.println(tweet.getUser().getScreenName());
					System.out.println(tweet.getText());
					System.out.println(tweet.getUser().getLocation());

					System.out.println(latitude);
					System.out.println(longitude + "\n");

					String[] tokens = tweet.getText().split(" ");
					String song = "";
					for(String s: tokens){
						if(s.length() > 0) {
							if(s.charAt(0)!='#')
								song += s + " ";
						}
					}
					song = song.substring(0, song.length()-1);
					if(!setList.contains(song))
					{
						addedToList = true;
						setList.add(song);
					}
					
					if(!setList.isEmpty()){
						if(addedToList){
							sendMessageToServer();
						}
					}
				}
			}
		}
		catch (TwitterException te) {
			te.printStackTrace();
			System.out.println("Failed to search tweets: " + te.getMessage());
			System.exit(-1);
		}

		System.out.println(setList.toString());

			
			//sendMessageToWebsite();
		//	}
		
	}

	private static void sendMessageToServer() throws JSONException {
		// TODO Auto-generated method stub
		PNConfiguration pnConfiguration = new PNConfiguration();
		// subscribeKey from admin panel
		pnConfiguration.setSubscribeKey("sub-c-0a7113d8-095b-11e7-b09b-0619f8945a4f"); // required
		// publishKey from admin panel (only required if publishing)
		pnConfiguration.setPublishKey("pub-c-74cba35d-fcf7-4821-9932-a6a327f0a018");
		pnConfiguration.setSecure(false);

		PubNub pubnub = new PubNub(pnConfiguration);

		JsonObject data = new JsonObject();
		data.addProperty("city", city);
		data.addProperty("artist", artist);
		data.addProperty("setlist", setList.get(setList.size()-1));

		System.out.println("before pub: " + data);
		pubnub.publish()
		.message(data) //is this how this is done???
		.channel("server_sender")
		.async(new PNCallback<PNPublishResult>() {
			public void onResponse(PNPublishResult result, PNStatus status) {
				// handle publish result, status always present, result if successful
				// status.isError to see if error happened
				if(!status.isError()) {
					System.out.println("pub timetoken: " + result.getTimetoken());
				}
				System.out.println("pub status code: " + status.getStatusCode());
			}
		});
	}
	private static void sendMessageToWebsite() {
		// TODO Auto-generated method stub
		TrustManager[] trustAllCerts = new TrustManager[]{
				new X509TrustManager() {
					public java.security.cert.X509Certificate[] getAcceptedIssuers() {
						return null;
					}
					public void checkClientTrusted(
							java.security.cert.X509Certificate[] certs, String authType) {
					}
					public void checkServerTrusted(
							java.security.cert.X509Certificate[] certs, String authType) {
					}
				}};

		try {
			SSLContext sc = SSLContext.getInstance("SSL");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
		} catch (Exception e) {
		}
		String FILE_PATH_NAME = "C:/Users/Sanat/Documents/sharkkitty.jpg";
		JsonObject JSON_POST_REQUEST = buildJson("John Doe", city, state, setList);
		String FILE_NAME = "sharkkitty.jpg";
		String JSON_PATH_NAME = "/Users/Sanat/Documents/Github/sxsw2017/call.json";
		String charset = "UTF-8";
		File uploadFile = new File(FILE_PATH_NAME);
		File uploadJSON = new File(JSON_PATH_NAME);
		String requestURL = "https://api.socan.ca/sandbox/SubmitNLMP?apiKey=l7xx50540a4a671342868a65f8a8f4a71d7a";

		/* Multipart request starting */
		try {
			MultipartUtility multipart = new MultipartUtility(requestURL, charset);

			System.out.println("RIP");

			multipart.addJsonField("nlmp", JSON_POST_REQUEST);
			multipart.addFilePart("file", uploadFile);
			multipart.addFormField("fileName", FILE_NAME);

			System.out.println(JSON_POST_REQUEST);
			List<String> response = multipart.finish();

			System.out.println("SERVER REPLIED:");

			for (String line : response) {
				System.out.println(line);
			}
		} catch (IOException ex) {
			System.err.println(ex);
		}
	}
	private static void pubNubConnectivity() {
		/* Currently we created a general stub...need to update this */
		
		PNConfiguration pnConfiguration = new PNConfiguration();
		// subscribeKey from admin panel
		pnConfiguration.setSubscribeKey("sub-c-0a7113d8-095b-11e7-b09b-0619f8945a4f"); // required
		// publishKey from admin panel (only required if publishing)
		pnConfiguration.setPublishKey("pub-c-74cba35d-fcf7-4821-9932-a6a327f0a018");
		pnConfiguration.setSecure(false);

		PubNub pubnub = new PubNub(pnConfiguration);

		pubnub.addListener(new SubscribeCallback(){

			@Override
			public void message(PubNub arg0, PNMessageResult arg1) {
				// TODO Auto-generated method stub
				JsonElement message = arg1.getMessage();
				state = message.getAsJsonObject().get("state").getAsString().toLowerCase();
				city = message.getAsJsonObject().get("city").getAsString().toLowerCase();
				hashtag = message.getAsJsonObject().get("hashtag").getAsString().toLowerCase();

				try {
					fetchTwitter(hashtag, city, state);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}


			@Override
			public void presence(PubNub arg0, PNPresenceEventResult arg1) {
				// TODO Auto-generated method stub

			}

			@Override
			public void status(PubNub arg0, PNStatus arg1) {
				// TODO Auto-generated method stub

			}

		});

		pubnub.subscribe()
		.channels(Arrays.asList("credit_writer")) // subscribe to channels information
		.execute();
	}
	private static void createJsonString() throws IOException {
		// TODO Auto-generated method stub
		BufferedReader in = new BufferedReader(
				new FileReader("C:\\Users\\Sanat\\Documents\\GitHub\\sxsw2017\\info.json"));
		String input;
		StringBuilder result = new StringBuilder();
		while((input = in.readLine()) !=null)
			result.append(input);

		//System.out.println(result.toString());

		//	parseJson(result.toString());
	}
	public static void makeConnection(URL url) throws IOException{
		URLConnection yc = url.openConnection();
		BufferedReader in = new BufferedReader(new InputStreamReader(
				yc.getInputStream()));
		String inputLine;
		while ((inputLine = in.readLine()) != null) 
			//	System.out.println(inputLine);

			//TODO set state and city based on the form input 

			in.close();
	}	
	public static JsonObject buildJson(String artist_nam, String cit, String stat, ArrayList<String> songs){
		String marie_no = "9999999";
		String canadian_performance = "NC";
		String type_of_program = "SC";
		String artist_name = "AGENT STUFFIN";
		String proof_type = "contract";
		String date_of_program = "2017/12/15";
		String venue = "SOCAN";
		String venuetype = "cfs";
		String venuecapacity = "15";
		String street1 = "41 Valleybrook";
		String city = cit;
		String province = stat;
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
		for (String song : songs){
			allCompositions.add(new Composition(song, artist_nam));	
		}
		//End of populating things ^^^^^//

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

		JsonObject value = new JsonObject();
		value.addProperty("MARIE_NO", marie_no);
		value.addProperty("CANADIAN_PERFORMANCE", canadian_performance);
		value.addProperty("TYPE_OF_PROGRAM", type_of_program);
		value.addProperty("ARTIST_NAME", artist_name);
		value.addProperty("PROOF_TYPE", proof_type);
		value.addProperty("DATE_OF_PROGRAM", date_of_program);
		value.addProperty("VENUE", venue);
		value.addProperty("VENUETYPE", venuetype);
		value.addProperty("VENUECAPACITY", venuecapacity);
		value.addProperty("STREET1", street1);
		value.addProperty("CITY", city);
		value.addProperty("PROVINCE", province);
		value.addProperty("POSTAL_CODE", postal_code);
		value.addProperty("COUNTRY", country);
		value.addProperty("VENUE_PHONE", venue_phone);
		value.addProperty("VENUE_WEBSITE", venue_website);
		value.addProperty("PERFORMANCE_TIME", performance_time);
		value.addProperty("PROMOTER", promoter);
		value.addProperty("PROMOTER_STREET1", promoter_street1);
		value.addProperty("PROMOTER_CITY", promoter_city);
		value.addProperty("PROMOTER_PROVINCE", promoter_province);
		value.addProperty("PROMOTER_POSTAL_CODE", promoter_postal_code);
		value.addProperty("PROMOTER_COUNTRY", promoter_country);
		value.addProperty("PROMOTER_TELEPHONE", promoter_telephone);
		value.addProperty("compositions", "[{\"ORIGINAL_TITLE\":\"SOCAN\",\"COMPOSER\":\"John Doe\"},{\"ORIGINAL_TITLE\":\"HACKATHON\",\"COMPOSER\":\"John Doe\"}]");    		  
		//				value.build();
		return value;
	}
	/*private static String parseJson(String jsonStr)  {
		Logger log = Logger.getLogger(Concert.class.getName());

		if(jsonStr!= null) {
			try{
				JSONObject jsonObj = new JSONObject(jsonStr);

				state = jsonObj.getString("state").toLowerCase();
				city = jsonObj.getString("city").toLowerCase();
				hashtag = jsonObj.getString("hashtag");

				System.out.println("Hashtag: " + hashtag);
				System.out.println("City: " + city);
				System.out.println("State: " + state);


			}catch (final JSONException e) {
				log.setLevel(Level.SEVERE);
				log.log(log.getLevel(), "Json parsing error: " + e.getMessage());
				System.out.println("JSON parsing error: " + e.getMessage());
			}
		}
		return null;
	}*/

}

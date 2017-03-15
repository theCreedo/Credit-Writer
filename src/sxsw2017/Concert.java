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
import com.pubnub.api.PNConfiguration;
import com.pubnub.api.PubNub;
import com.pubnub.api.callbacks.SubscribeCallback;
import com.pubnub.api.enums.PNLogVerbosity;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.pubsub.PNMessageResult;
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult;

import java.net.*;
import java.io.*;

public class Concert {
	/*	final static AccessToken accessToken = new AccessToken("789951371428495360-7aljagVXHXXVySZ94BQNHzG0ZVr2JDI"
			, "dxq40x9zV4gqUHxZflfqT975pEFpUxardfyRinuxi96S7");*/
	//final static Twitter twitter = new TwitterFactory().getInstance(accessToken);
	//final static Twitter twitter = new TwitterFactory().getInstance(accessToken);
	//private static String city = "";
	//private static String state = "";
	//private static String hashtag = "";
	private static double latitude = 0;
	private static double longitude = 0;
	static ConfigurationBuilder builder = new ConfigurationBuilder();
	static ArrayList<String> setList = new ArrayList<String>();
	static Twitter twitter;

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
		
	
//		System.out.println("HEREEEE");
		
//		System.out.println(hashtag);
		
		
				
/*		String FILE_PATH_NAME = "";
    	String JSON_POST_REQUEST = "";
    	String FILE_NAME = "";
        String charset = "UTF-8";
        File uploadFile = new File(FILE_PATH_NAME);
        String requestURL = "https://api.socan.ca/sandbox/SubmitNLMP?apiKey=l7xx50540a4a671342868a65f8a8f4a71d7a";

        try {
            MultipartUtility multipart = new MultipartUtility(requestURL, charset);

            System.out.println("RIP");

            multipart.addJsonField("nlmp", JSON_POST_REQUEST);
            multipart.addFilePart("file", uploadFile);
            multipart.addFormField("fileName", FILE_NAME);

            List<String> response = multipart.finish();

            System.out.println("SERVER REPLIED:");

            for (String line : response) {
                System.out.println(line);
            }
        } catch (IOException ex) {
            System.err.println(ex);
        }*/
	}

	static void fetchTwitter(String hashtag, String city, String state){
		
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
			for(Status tweet: tweets){
			/*	System.out.println(tweet.getUser().getScreenName());
				System.out.println(tweet.getText());
				System.out.println(tweet.getUser().getLocation());*/
				//System.out.println(tweet.getText());
				check = false;
				
				//System.out.println(tweet.getText());
				String[] location = tweet.getUser().getLocation().split(", ");
				for(String s : location)
					if(s.equalsIgnoreCase(city) || s.equalsIgnoreCase(state)){
						check = true;
						break;
					}
						
				if(check){
					//System.out.println("@" + tweet.getUser().getScreenName() + " - " + tweet.getText());
					//splitting the text into tokens based on spaces
					
					MapQuestApi map = new MapQuestApi(city, state);

					latitude = map.getLat();
					longitude = map.getLon();
		
					System.out.println(tweet.getUser().getScreenName());
					System.out.println(tweet.getText());
					System.out.println(tweet.getUser().getLocation());
				
					System.out.println(latitude);
					System.out.println(longitude + "\n");
					
					
					String[] tokens = tweet.getText().split(" ");
					String song = "";
					for(String s: tokens){
						if(s.charAt(0)!='#')
							song += s + " ";
					}
					song = song.substring(0, song.length()-1);
//					System.out.printf("%s\n\n", song);

					//adding song to setlist
					setList.add(song);
				}
			}
		}
		catch (TwitterException te) {
			te.printStackTrace();
			System.out.println("Failed to search tweets: " + te.getMessage());
			System.exit(-1);
		}
		
		System.out.println(setList.toString());

	}
	
	private static void pubNubConnectivity() {
		// TODO Auto-generated method stub
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
				String state = message.getAsJsonObject().get("state").getAsString().toLowerCase();
				String city = message.getAsJsonObject().get("city").getAsString().toLowerCase();
				String hashtag = message.getAsJsonObject().get("hashtag").getAsString().toLowerCase();
				
				fetchTwitter(hashtag, city, state);
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
			System.out.println(inputLine);

		//TODO set state and city based on the form input 
		
		in.close();
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

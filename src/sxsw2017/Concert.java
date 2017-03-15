package sxsw2017;

import java.util.*;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;
import java.util.logging.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.net.*;
import java.io.*;

public class Concert {
	/*	final static AccessToken accessToken = new AccessToken("789951371428495360-7aljagVXHXXVySZ94BQNHzG0ZVr2JDI"
			, "dxq40x9zV4gqUHxZflfqT975pEFpUxardfyRinuxi96S7");*/
	//final static Twitter twitter = new TwitterFactory().getInstance(accessToken);
	//final static Twitter twitter = new TwitterFactory().getInstance(accessToken);
	private static String city;
	private static String state;
	private static String hashtag;
	private static double latitude;
	private static double longitude;
	static ConfigurationBuilder builder;
	static ArrayList<String> setList;

	Concert(){
		builder =  new ConfigurationBuilder();
		city = null;
		state = null;
		latitude = 0;
		longitude = 0;
		hashtag = null;
		setList = new ArrayList<String>();
	}
	public static void main(String[] args)throws IOException{
		builder.setOAuthConsumerKey("BNvErshlqPIYQUDOPFmvQofPH");
		builder.setOAuthConsumerSecret("3oBEm1MerGR0kJPDOYqhLnoaQlpYFX4pTbjybRo4uIJEjQ0ahJ");
		builder.setOAuthAccessToken("789951371428495360-7aljagVXHXXVySZ94BQNHzG0ZVr2JDI");
		builder.setOAuthAccessTokenSecret("dxq40x9zV4gqUHxZflfqT975pEFpUxardfyRinuxi96S7");
		Configuration configuration = builder.build();
		TwitterFactory factory = new TwitterFactory(configuration);
		Twitter twitter = factory.getInstance();
		//		twitter.setOAuthAccessToken(accessToken);

		System.out.println("Working till here");

		URL url = new URL("http://www.oracle.com/");

		makeConnection(url);

		try {
			//create a query to search twitter with the specific hashtag
			Query query = new Query(hashtag);
			QueryResult result;

			//get results from twitter
			result = twitter.search(query);

			//store the returned tweets in a list
			List<Status> tweets = result.getTweets();

			//TODO send in the state and state to mapquest api
			//TODO set latitude and longitude to the return from the getLat and getLong methods

			//looping through the list of tweets to find relevant tweets
			for (Status tweet : tweets) {	
				if(tweet.getUser().getLocation().contains(city) || 
						tweet.getUser().getLocation().contains(state)){
					System.out.println("@" + tweet.getUser().getScreenName() + " - " + tweet.getText());
					//splitting the text into tokens based on spaces
					String[] tokens = tweet.getText().split(" ");
					String song = "";
					for(String s: tokens){
						if(s.charAt(0)!='#')
							song += s + " ";
					}
					song = song.substring(0, song.length()-1);

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
	}

	public static void makeConnection(URL url) throws IOException{
		URLConnection yc = url.openConnection();
		BufferedReader in = new BufferedReader(new InputStreamReader(
				yc.getInputStream()));
		String inputLine;
		while ((inputLine = in.readLine()) != null) 
			System.out.println(inputLine);

		//TODO set state and city based on the form input 
		
		MapQuestApi map = new MapQuestApi(city, state);

		latitude = map.getLat();
		longitude = map.getLon();
		
		in.close();
	}	

	private String parseJson(String jsonStr)  {
		Logger log = Logger.getLogger(Concert.class.getName());

		if(jsonStr!= null) {
			try{
				JSONObject jsonObj = new JSONObject(jsonStr);

				// Getting JSON Array node
				JSONArray lists = jsonObj.getJSONArray("tags");

				// looping through All Contacts
				for (int i = 0; i < lists.length(); i++) {
					JSONObject c = lists.getJSONObject(i);
					
					state = c.getString("state").toLowerCase();
					city = c.getString("city").toLowerCase();
					hashtag = c.getString("hashtag")
							;
				}
				return null;
			}catch (final JSONException e) {
				log.setLevel(Level.SEVERE);
				log.log(log.getLevel(), "Json parsing error: " + e.getMessage());
				System.out.println("JSON parsing error: " + e.getMessage());
			}
		}
		return null;
	}

}

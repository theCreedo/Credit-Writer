package sxsw2017;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

public class MapQuestApi {
	HttpURLConnection connection = null;
	
	private String  city,
					state,
					postalCode,
					country,
					address,
					province;
	private double  lat,
					lon;
	private String mapQuestApi = "http://www.mapquestapi.com/geocoding/v1/address?key=";
	private String key = "iudbujZgPU4WerMcoKL2WAkN9BfQ6dEs";
	private String location = "&location=";
	
	
	public MapQuestApi(String city, String state){
		address = "98 San Jacinto Blvd";
		this.city = city;
		this.state = state;
		province = state;
		postalCode = "78701";
		country = "US";
		setApiUrl();
	}
	
	private void setApiUrl(){
		String tempCity = city.replace(' ','+'); 
		location += tempCity + "," + state;
		mapQuestApi += key + location;
		
		sendRequest();
	}
	
	public void sendRequest(){

		try {
			String url = mapQuestApi;
			String charset = "UTF-8";
			
			URLConnection connection = new URL(url).openConnection();
			connection.setRequestProperty("Accept-Charset", charset);
			
			
			InputStream response = connection.getInputStream();

			try (Scanner scanner = new Scanner(response)) {
			    String responseBody = scanner.useDelimiter("\\A").next();
			    
			    int latIndexBeg = responseBody.indexOf("\"lat\":")+6;
			    int lonIndexBeg = responseBody.indexOf("\"lng\":");
			    int latIndexEnd = lonIndexBeg - 1;
			    lonIndexBeg = lonIndexBeg+6;
			    int lonIndexEnd = responseBody.indexOf("},\"displayLatLng");
			    
			    
			    lat = Double.parseDouble(responseBody.substring(latIndexBeg, latIndexEnd));
			    lon = Double.parseDouble(responseBody.substring(lonIndexBeg, lonIndexEnd));
			    
			//    System.out.println(responseBody+"\n");
			}
		    
		  } catch (Exception e) {
		    e.printStackTrace();
		  } finally {
		    if (connection != null) {
		      connection.disconnect();
		    }
		  }
	}
	
	public String getAddress(){
		return address;
	}
	
	public String getCity(){
		return city;
	}
	
	public String getState(){
		return state;
	}
	
	public String getProvince(){
		return province;
	}
	
	public double getLat(){
		return lat;
	}
	
	public double getLon(){
		return lon;
	}
	
	public String getCountry(){
		return country;
	}
	
	public String getPostalCode(){
		return postalCode;
	}
}

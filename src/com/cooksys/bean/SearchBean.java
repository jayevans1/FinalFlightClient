package com.cooksys.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.cooksys.model.Flight;
import com.cooksys.model.FlightList;
import com.cooksys.model.FlightPathMap;
import com.cooksys.model.Location;
import com.cooksys.model.LocationList;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;

@Component
@Scope("session")
public class SearchBean {

	@Autowired
	LoginBean loginBean;
	
	private Location origin;
	private Location destination;
	private int flightId;
	private List<Flight> nextFlights = new ArrayList<Flight>();
	List<Flight> bookedFlightPath;
	
	boolean foundFlightPath = false;

	
	boolean bookedFlight = false;

	Logger log = LoggerFactory.getLogger(SearchBean.class);

	public void print() {
		log.info("origin is " + origin);
	}

	public void printD() {
		log.info("Destination is " + destination);
	}

	public void printF() {
		log.info("Selected Flight is : " + flightId);
	}

	/**
	 * Get all The possible Flight origins From the 
	 * web service
	 * @return
	 */
	public List<Location> getAllOrigin() {
		Client client = Client.create();
		WebResource webResource2 = client
				.resource("http://localhost:8080/FinalFlightService/getAllFlightOrigin");
		ClientResponse response2 = webResource2.accept(
				MediaType.APPLICATION_XML).get(ClientResponse.class);

		LocationList locationList = response2.getEntity(LocationList.class);

		List<Location> originList = locationList.getLocationList();
		// log.info("Origin size " + originList.size());
		return originList;
	}

	/**
	 * Get all the possible destination locations
	 * from the web service
	 * @return
	 */
	public List<Location> getAllDestination() {
		Client client = Client.create();
		WebResource webResource2 = client
				.resource("http://localhost:8080/FinalFlightService/getAllFlightDestination");
		ClientResponse response2 = webResource2.accept(
				MediaType.APPLICATION_XML).get(ClientResponse.class);

		LocationList locationList = response2.getEntity(LocationList.class);

		List<Location> destinationList = locationList.getLocationList();
		// log.info("Destination size: " + destinationList.size());
		return destinationList;
	}

	/**
	 * Get all the flights leaving from selected Origin
	 * @return
	 */
	public List<Flight> getFlightFromOrigin() {
		Client client = Client.create();
		List<Flight> flightOrigin = null;
		if (origin != null) {
			WebResource webResource2 = client
					.resource("http://localhost:8080/FinalFlightService/getAllFlightFromOrigin");
			ClientResponse response2 = webResource2.accept(
					MediaType.APPLICATION_XML).post(ClientResponse.class,
					origin);

			FlightList flightList = response2.getEntity(FlightList.class);

			flightOrigin = flightList.getFlightList();
			// log.info("Grabbing flights leaving from " + origin);
		}
		return flightOrigin;
	}
	
	/**
	 * Send web service the User's flights that
	 * the User decide to book
	 * @return
	 */
	public String bookFlight(){
		if(nextFlights != null){
			Client client = Client.create();
			WebResource webResource2 = client
					.resource("http://localhost:8080/FinalFlightService/bookFlight");

			FlightList flightList = new FlightList();
			flightList.setFlightList(nextFlights);
			flightList.setUsername(loginBean.getUser().getUsername());

			ClientResponse response2 = webResource2.accept(
					MediaType.APPLICATION_XML).post(ClientResponse.class, flightList);
			
			String message = response2.getEntity(String.class);
			if(message.equals("Booked")){
				bookedFlight = true;
			}
			return message;
		}
		
		return "fail";
	}
	
	/**
	 * Gets the Users Booked Flight Paths
	 */
	public void getBookedFlightPaths(){
		Client client = Client.create();
		if(loginBean.getUser() != null){
			WebResource webResource2 = client
					.resource("http://localhost:8080/FinalFlightService/bookFlightPath");
			ClientResponse response2 = webResource2.accept(
					MediaType.APPLICATION_XML).post(ClientResponse.class, loginBean.getUser());

			FlightList flightList = response2.getEntity(FlightList.class);
			 
//			Map<Integer, FlightList> map2 = map.getMap();
//			
//			Map<Integer, List<Flight>> map3 = new HashMap<Integer, List<Flight>>();
//			
//			log.info("IS IT NULL " + map2.size());
//			
//			map2.keySet().forEach(f->{
//				if(!map3.containsKey(f)){
//					map3.put(f, new ArrayList<Flight>());
//					map3.get(f).addAll(map2.get(f).getFlightList());
//				}
//				else{
//				map3.get(f).addAll(map2.get(f).getFlightList());
//				}
//			});
//			
//			for(Integer i : map3.keySet()){
//				bookedFlightPath.addAll(map3.get(i));
//				break;
//			}
			bookedFlightPath = flightList.getFlightList();
		}
	}

	/**
	 * Gets the Flight Paths of User's selected Flight and User's
	 * selected Destination location
	 * @return
	 */
	public List<Flight> search() {
		Client client = Client.create();
		List<Flight> next = null;
		nextFlights.clear();
		if(Integer.toString(flightId) != null){
			WebResource webResource2 = client
					.resource("http://localhost:8080/FinalFlightService/getFlightPath");

			MultivaluedMap formData = new MultivaluedMapImpl();	
			formData.add("flightId", Integer.toString(flightId));
			formData.add("destination", destination.getCity());

			ClientResponse response2 = webResource2.accept(
					MediaType.APPLICATION_XML).post(ClientResponse.class, formData);
			FlightList flightList = response2.getEntity(FlightList.class);
			
			nextFlights = flightList.getFlightList();
			
			
		}
	
		if(nextFlights != null){
			log.info("FOUND FLIGHT PATH TRUE");
			foundFlightPath = true;
		}else{
			log.info("FOUND FLIGHT PATH FALSE");
			foundFlightPath = false;
		}
		return nextFlights;
	}
	
	/**
	 * Gets the Selected Flights possible flight paths
	 * @return
	 */
	public List<Flight> path() {
		Client client = Client.create();
		List<Flight> next = null;
		if(Integer.toString(flightId) != null){
			WebResource webResource2 = client
					.resource("http://localhost:8080/FinalFlightService/path");

			MultivaluedMap formData = new MultivaluedMapImpl();
			
			formData.add("flightId", Integer.toString(flightId));
			formData.add("destination", destination.getCity());

			
			ClientResponse response2 = webResource2.accept(
					MediaType.APPLICATION_XML).post(ClientResponse.class, formData);

			 FlightList flightList = response2.getEntity(FlightList.class);
			 //nextFlights = flightList.getFlightList();
			 next = flightList.getFlightList();
			 
		}
		return next;
	}
	
//	public List<Flight> test() {
//		Client client = Client.create();
//		List<Flight> next = null;
//		if(Integer.toString(flightId) != null){
//			WebResource webResource2 = client
//					.resource("http://localhost:8080/FinalFlightService/test");
//
//			MultivaluedMap formData = new MultivaluedMapImpl();
//			
//			formData.add("flightId", Integer.toString(flightId));
//
//			
//			ClientResponse response2 = webResource2.accept(
//					MediaType.APPLICATION_XML).post(ClientResponse.class, formData);
//
//			 FlightList flightList = response2.getEntity(FlightList.class);
//			// nextFlights = flightList.getFlightList();
//			 next = flightList.getFlightList();
//		}
//		return next;
//	}

	public Location getOrigin() {
		return origin;
	}

	public void setOrigin(Location origin) {
		this.origin = origin;
	}

	public Location getDestination() {
		return destination;
	}

	public void setDestination(Location destination) {
		this.destination = destination;
	}

	public int getFlightId() {
		return flightId;
	}

	public void setFlightId(int flightId) {
		this.flightId = flightId;
	}

	public List<Flight> getNextFlights() {
		return nextFlights;
	}

	public void setNextFlights(List<Flight> nextFlights) {
		this.nextFlights = nextFlights;
	}

	public boolean isBookedFlight() {
		return bookedFlight;
	}

	public void setBookedFlight(boolean bookedFlight) {
		this.bookedFlight = bookedFlight;
	}

	public boolean isFoundFlightPath() {
		return foundFlightPath;
	}

	public void setFoundFlightPath(boolean foundFlightPath) {
		this.foundFlightPath = foundFlightPath;
	}

	public List<Flight> getBookedFlightPath() {
		return bookedFlightPath;
	}

	public void setBookedFlightPath(List<Flight> bookedFlightPath) {
		this.bookedFlightPath = bookedFlightPath;
	}

}

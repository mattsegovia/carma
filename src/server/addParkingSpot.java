package server;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Date;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Servlet implementation class addParkingSpot
 */
@WebServlet("/addParkingSpot")
public class addParkingSpot extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public addParkingSpot() {
        super();
        // TODO Auto-generated constructor stub
    }

    public void init( ServletConfig config ) throws ServletException
    {
        super.init( config );

        try
        {
            Class.forName( "com.mysql.jdbc.Driver" );
        }
        catch( ClassNotFoundException e )
        {
            throw new ServletException( e );
        }
    }
    
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		return;
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		JSONParser parser = new JSONParser();
        
        String url = "jdbc:mysql://localhost/cs3337group3";
        String username = "cs3337";
        String password = "csula2017";
        
        response.setContentType("application/json");
		PrintWriter out = response.getWriter();
        
        int userID = 0, userCar = 0, spotID = -1;
        String location = "";
        String GPSLat = "";
        String GPSLong = "";
        String timeSwap = "Now";
        String comment = "";
        String level = "";
        
        
        try {
			JSONObject data = (JSONObject) parser.parse(request.getReader());
			location = (String) data.get("location");
			level = (String) data.get("level");
			//timeSwap = (String) data.get("timeSwap");
			comment = (String) data.get("comment");
			GPSLat = (String) data.get("latitude");
			GPSLong = (String) data.get("longitude");
			
			//TODO: double check json for spotID
			String temp = (String) data.get("spotId");
			if(temp !=null && !temp.isEmpty())
				spotID = Integer.parseInt(temp);
			
			//check cookie for user id and car id
			Cookie[] cookies = request.getCookies();
			if(cookies!=null) {
				for(Cookie current: cookies) {
					if(current.getName().equals("ID")) {
						userID = Integer.parseInt(current.getValue());
					} else if(current.getName().equals("CARID")) {
						userCar = Integer.parseInt(current.getValue());
					}
				}
			}
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Connection c = null;
		PreparedStatement insertSpot = null, findSpotId = null;
		ResultSet spotIDResults = null;
	    try {
	        c = DriverManager
	                .getConnection( url, username, password );
	        
	        insertSpot = c.prepareStatement(
	                "insert into Spots(Lister_ID, Lister_Car, Location, GPS_Lat, GPS_Long, Time_Listed, Time_Swap, Comment)  values(?,?,?,?,?,?,?,?)");
	        
	        insertSpot.setInt(1, userID);
	        insertSpot.setInt(2, userCar);

	        if(level.equals("default"))
	        	insertSpot.setString(3, location);
	        else
	        	insertSpot.setString(3,  location + ", " + level);
	        insertSpot.setString(4, GPSLat);
	        insertSpot.setString(5, GPSLat);

	        insertSpot.setString(6, (LocalDateTime.now().toString()));
	        insertSpot.setString(7, timeSwap);
	        insertSpot.setString(8, comment);
	        
	        insertSpot.executeUpdate();
	        
	    }
	    catch( SQLException e )
	    {
	    	throw new ServletException( e );
	    } finally {
			try { spotIDResults.close(); } catch (Exception e) { /* ignored */ }
			try { findSpotId.close(); } catch (Exception e) { /* ignored */ }
			try { insertSpot.close(); } catch (Exception e) { /* ignored */ }
			try { c.close(); } catch (Exception e) { /* ignored */ }
		}
	}
	
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		JSONParser parser = new JSONParser();
        
        String url = "jdbc:mysql://localhost/cs3337group3";
        String username = "cs3337";
        String password = "csula2017";
        
        response.setContentType("application/json");
		PrintWriter out = response.getWriter();
        
        int spotID = -1;
        int result = -1;
        JSONObject resultJSON = new JSONObject();
        
        try {
        	JSONObject data = (JSONObject) parser.parse(request.getReader());
        	//TODO: double check json for spotID
			String temp = (String) data.get("spotId");
			if(temp !=null && !temp.isEmpty())
				spotID = Integer.parseInt(temp);
			else { //no spotid so do nothing, something bad happened
				resultJSON.put("result", result);
				out.println(resultJSON.toJSONString());
				return;
			}
        } catch (ParseException e) {
			e.printStackTrace();
		}
        
        Connection c = null;
		PreparedStatement deleteSpot = null;
	    try {
	        c = DriverManager
	                .getConnection( url, username, password );
	        
	        deleteSpot = c.prepareStatement(
	                "delete from Spots where ID=?");
	        
	        deleteSpot.setInt(1, spotID);
	               
	        //returns 1 if a row got deleted, 0 otherwise
	        result = deleteSpot.executeUpdate();
	        
	    }
	    catch( SQLException e )
	    {
	    	throw new ServletException( e );
	    } finally {
			try { deleteSpot.close(); } catch (Exception e) { /* ignored */ }
			try { c.close(); } catch (Exception e) { /* ignored */ }
		}
	    
	    //on return make sure result == 1
	    resultJSON.put("result", result);
		out.println(resultJSON.toJSONString());
	    
	}
	
}


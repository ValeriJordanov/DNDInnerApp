package app.inner.drinkanddrivebrothers.utility;

import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import java.io.ByteArrayOutputStream;


/**
 * Created by plame_000 on 15-Nov-17.
 */

public final class Util {

    private Util() {

    }

    public static DatabaseReference getFirebaseReference() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference();
        return ref;
    }

    public String getTime() {
        try{
            //Make the Http connection so we can retrieve the time
            HttpClient httpclient = new DefaultHttpClient();
            // I am using yahoos api to get the time
            HttpResponse response = httpclient.execute(new
                    HttpGet("http://developer.yahooapis.com/TimeService/V1/getTime?appid=YahooDemo"));
            StatusLine statusLine = response.getStatusLine();
            if(statusLine.getStatusCode() == HttpStatus.SC_OK){
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                response.getEntity().writeTo(out);
                out.close();
                // The response is an xml file and i have stored it in a string
                String responseString = out.toString();
                Log.d("Response", responseString);
                //We have to parse the xml file using any parser, but since i have to
                //take just one value i have deviced a shortcut to retrieve it
                int x = responseString.indexOf("<Timestamp>");
                int y = responseString.indexOf("</Timestamp>");
                //I am using the x + "<Timestamp>" because x alone gives only the start value
                Log.d("Response", responseString.substring(x + "<Timestamp>".length(),y) );
                String timestamp =  responseString.substring(x + "<Timestamp>".length(),y);
                // The time returned is in UNIX format so i need to multiply it by 1000 to use it
                Date d = new Date(Long.parseLong(timestamp) * 1000);
                Log.d("Response", d.toString() );
                return d.toString() ;
            } else{
                //Closes the connection.
                response.getEntity().getContent().close();
                throw new IOException(statusLine.getReasonPhrase());
            }
        }catch (ClientProtocolException e) {
            Log.d("Response", e.getMessage());
        }catch (IOException e) {
            Log.d("Response", e.getMessage());
        }
        return null;
    }

}
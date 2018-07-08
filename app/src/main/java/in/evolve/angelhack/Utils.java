package in.evolve.angelhack;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by Rahul Yadav on 30/1/16.
 */


/*This class will contain methods to be used through out the project*/


public class Utils {

    public static JSONObject getJSONFromUrl(String completeurl){
        InputStream is = null;
        JSONObject jsonObject=null;
        String jsonstring="";
        try {
            URL url = new URL(completeurl);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setReadTimeout(15000);
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);

            is = new BufferedInputStream(urlConnection.getInputStream());
            Scanner s = new Scanner(is).useDelimiter("\\A");
            if(s.hasNext()){
                jsonstring= s.next();
            }
            urlConnection.disconnect();
        } catch (MalformedURLException e) {
            Log.d("error", "error in getjsonfromurl MalformedUrlexception");
        } catch (IOException e) {
            Log.d("error", "error in getjsonfromurl Ioexception");
        }
        catch (Exception e){
            e.printStackTrace();
        }

        try {
            jsonObject = new JSONObject(jsonstring);
        } catch (JSONException e) {
            Log.d("error", "Json exception in get JSONFRomURL ");
            return null;

        }
        return jsonObject;
    }


    public static void popAllFragments(FragmentManager manager){

        for (int i=0;i<manager.getBackStackEntryCount()-1;i++){
            manager.popBackStack();
        }
    }



    public static String getStringFromUrl(String completeurl){
        InputStream is = null;
        JSONObject jsonObject=null;
        String jsonstring="";
        try {
            URL url = new URL(completeurl);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setReadTimeout(5000);
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);

            is = new BufferedInputStream(urlConnection.getInputStream());
            Scanner s = new Scanner(is).useDelimiter("\\A");
            if(s.hasNext()){
                jsonstring= s.next();
            }
            urlConnection.disconnect();
        } catch (MalformedURLException e) {
            Log.d("error", "error in getjsonfromurl MalformedUrlexception");
        } catch (IOException e) {
            Log.d("error", "error in getjsonfromurl Ioexception");
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return jsonstring;
    }

    public static boolean isNetworkAvailable(Context context)
    {
        ConnectivityManager connectivityManager= (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();
        return (networkInfo!=null && networkInfo.isConnected());
    }

    public static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static void toastS(Context context , String message){
        Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
    }

    public static void toastL(Context context , String message){


        Toast.makeText(context,message,Toast.LENGTH_LONG).show();
    }


    public static JSONObject postJSONObject(String completeUrl,JSONObject jsonObject)
    {
        DataOutputStream dataOutputStream;
        InputStream is;
        String jsonstring1 ="";
        JSONObject jsonObject1= null;

        JSONObject j = new JSONObject();



        try{
            String jsonstring = jsonObject.toString();
            URL url = new URL(completeUrl);
            HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setConnectTimeout(15000);
            httpURLConnection.setReadTimeout(15000);
            httpURLConnection.setRequestProperty("Content-Type", "application/json;charset=utf-8");
            httpURLConnection.setRequestProperty("X-Requested-With", "XMLHttpRequest");

            dataOutputStream = new DataOutputStream(httpURLConnection.getOutputStream());
            dataOutputStream.write(jsonstring.getBytes());
            dataOutputStream.flush();
            dataOutputStream.close();

            int httpResult = httpURLConnection.getResponseCode();
            if(httpResult==HttpURLConnection.HTTP_OK) {
                is = new BufferedInputStream(httpURLConnection.getInputStream());
                Scanner s = new Scanner(is).useDelimiter("\\A");
                if (s.hasNext()) {
                    jsonstring1 = s.next();
                }
            }

        }catch(MalformedURLException e){
            Log.d("error","malformedUrl in Post");
        }catch (IOException e){
            Log.d("error","IOException in Post");
        }catch(Exception e){
            Log.d("error","Exception in Post");
        }


        Log.e("eeroor",jsonstring1);

        try {
            jsonObject1 = new JSONObject(jsonstring1);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject1;
    }

    public static String postObject(String completeUrl,JSONObject jsonObject)
    {
        DataOutputStream dataOutputStream;
        InputStream is;
        String jsonstring1 ="";

        try{
            String jsonstring = jsonObject.toString();
            URL url = new URL(completeUrl);
            HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setConnectTimeout(15000);
            httpURLConnection.setReadTimeout(15000);

            dataOutputStream = new DataOutputStream(httpURLConnection.getOutputStream());
            dataOutputStream.write(jsonstring.getBytes());
            //Log.d("url calling in post",""+dataOutputStream);


            dataOutputStream.flush();
            dataOutputStream.close();

            int httpResult = httpURLConnection.getResponseCode();
            if(httpResult==HttpURLConnection.HTTP_OK) {
                is = new BufferedInputStream(httpURLConnection.getInputStream());
                Scanner s = new Scanner(is).useDelimiter("\\A");
                if (s.hasNext()) {
                    jsonstring1 = s.next();
                }
            }

        }catch(MalformedURLException e){
            Log.d("error","malformedUrl in Post");
        }catch (IOException e){
            e.printStackTrace();
        }catch(Exception e){
            Log.d("error", "Exception in Post");
        }


        return jsonstring1;
    }




    public static ArrayList<String> getStateList(Context context,String country){

        if (country.matches("ALL")){
            return null;
        }
        ArrayList<String> stateList = new ArrayList<>();

        String json = null;
        try {

            InputStream is = context.getAssets().open(country+".json");

            int size = is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();

            json = new String(buffer, "UTF-8");


        } catch (IOException ex) {
            ex.printStackTrace();
        }

        JSONArray countryArray = null;

        try {
            countryArray = new JSONArray(json);
        }
        catch (JSONException ex){
            ex.printStackTrace();
            countryArray = new JSONArray();
        }

        for (int len = 0;len<countryArray.length();len++){

            try{
                stateList.add(countryArray.getJSONObject(len).getString("name"));
            }
            catch (JSONException ex){
                ex.printStackTrace();
            }
        }
        return stateList;
    }
}

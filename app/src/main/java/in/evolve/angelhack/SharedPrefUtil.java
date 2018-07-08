package in.evolve.angelhack;

import android.content.Context;
import android.content.SharedPreferences;



/**
 * Created by Brekkishhh on 16-10-2016.
 */
public class SharedPrefUtil {

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Context context;
    private final String ADHAAR_ID = "adhaar_id";
    private final String FILE_NAME = "shared_pref";
    private final String ORDERS_LIST = "orders_list";

    private String JSON_ITEM_LIST;

    public SharedPrefUtil(Context context) {

        this.context = context;
        this.sharedPreferences = context.getSharedPreferences(FILE_NAME,Context.MODE_PRIVATE);
        this.editor = sharedPreferences.edit();


        if (!sharedPreferences.contains(ORDERS_LIST)){
            editor.putString(ORDERS_LIST,JSON_ITEM_LIST).commit();

        }

    }

    public void setAdhaarID(String AdhaarID){
        editor.putString(ADHAAR_ID,AdhaarID).commit();
    }

    public String getTableId(){
        if (sharedPreferences.contains(ADHAAR_ID)){
            return sharedPreferences.getString(ADHAAR_ID,null);
        }
        return null;
    }


    public void clearData(){
        editor.clear().commit();
    }



}

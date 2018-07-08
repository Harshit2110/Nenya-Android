package in.evolve.angelhack;

import android.content.Context;
import android.nfc.FormatException;
import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.MifareUltralight;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.nfc.tech.NfcA;
import android.nfc.tech.NfcB;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.util.Log;

import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.Charset;

/**
 * Created by Brekkishhh on 13-08-2016.
 */
public class NfcTagUtils {

    private static final String TAG = "NfcTagUtils";


    public static JSONObject readNfcTag(Tag tag){

        JSONObject tagComponents = new JSONObject();
        String [] techList = tag.getTechList();

        return tagComponents;
    }

    public Tag patchTag(Tag oTag)
    {
        if (oTag == null)
            return null;

        String[] sTechList = oTag.getTechList();

        Parcel oParcel, nParcel;

        oParcel = Parcel.obtain();
        oTag.writeToParcel(oParcel, 0);
        oParcel.setDataPosition(0);

        int len = oParcel.readInt();
        byte[] id = null;
        if (len >= 0)
        {
            id = new byte[len];
            oParcel.readByteArray(id);
        }
        int[] oTechList = new int[oParcel.readInt()];
        oParcel.readIntArray(oTechList);
        Bundle[] oTechExtras = oParcel.createTypedArray(Bundle.CREATOR);
        int serviceHandle = oParcel.readInt();
        int isMock = oParcel.readInt();
        IBinder tagService;
        if (isMock == 0)
        {
            tagService = oParcel.readStrongBinder();
        }
        else
        {
            tagService = null;
        }
        oParcel.recycle();

        int nfca_idx=-1;
        int mc_idx=-1;

        for(int idx = 0; idx < sTechList.length; idx++)
        {
            if(sTechList[idx] == NfcA.class.getName())
            {
                nfca_idx = idx;
            }
            else if(sTechList[idx] == MifareClassic.class.getName())
            {
                mc_idx = idx;
            }
        }

        if(nfca_idx>=0&&mc_idx>=0&&oTechExtras[mc_idx]==null)
        {
            oTechExtras[mc_idx] = oTechExtras[nfca_idx];
        }
        else
        {
            return oTag;
        }

        nParcel = Parcel.obtain();
        nParcel.writeInt(id.length);
        nParcel.writeByteArray(id);
        nParcel.writeInt(oTechList.length);
        nParcel.writeIntArray(oTechList);
        nParcel.writeTypedArray(oTechExtras,0);
        nParcel.writeInt(serviceHandle);
        nParcel.writeInt(isMock);
        if(isMock==0)
        {
            nParcel.writeStrongBinder(tagService);
        }
        nParcel.setDataPosition(0);

        Tag nTag = Tag.CREATOR.createFromParcel(nParcel);

        nParcel.recycle();

        return nTag;
    }


    public static String readTag(Tag tag) {
        Ndef mfc = Ndef.get(tag);

        if(mfc == null){
            Log.d("QQQQQQQQQQQQQQ","Null hai");
        }else{
            Log.d("ZZZZZZZZZZZZZZZZZ",String.valueOf(mfc.getType()));
        }

        byte[] data;
        byte[] cardData = null;

        try {       //  5.1) Connect to card
            mfc.connect();
            boolean auth = false;

            // 5.2) and get the number of sectors this card has..and loop thru these sectors
            /*int secCount = mfc.getSectorCount();
            int bCount = 0;
            int bIndex = 0;
            for (int j = 0; j < secCount; j++) {
                // 6.1) authenticate the sector
                auth = mfc.authenticateSectorWithKeyA(j, MifareClassic.KEY_DEFAULT);
                auth = mfc.authenticateSectorWithKeyB(j,MifareClassic.KEY_DEFAULT);

                if (auth) {


                    // 6.2) In each sector - get the block count
                    bCount = mfc.getBlockCountInSector(j);
                    bIndex = 0;
                    for (int i = 0; i < bCount; i++) {
                        bIndex = mfc.sectorToBlock(j);
                        // 6.3) Read the block
                        data = mfc.readBlock(bIndex);

                        if (bIndex==4){
                            cardData = data;
                        }
                        // 7) Convert the data into a string from Hex format.


                      //  Log.i(TAG, "the Bindex is" + bIndex + "   " + new String(data, Charset.forName("UTF-8")));
                    }

                } else { // Authentication failed - Handle it
                    Log.d(TAG, "Unable to authenticate....");
                }


            }*/
        } catch (IOException e) {
            e.printStackTrace();
        }


        /*if (cardData==null){
            return "           No DATA On Card";
        }*/

        /*return new String(cardData);*/
        return /*mfc.getCachedNdefMessage().toString()*/ "Harshit";
    }// End of method


}


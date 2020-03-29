package com.cannontechcreative.morsify;

import android.hardware.camera2.CameraAccessException;
import android.os.Build;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.ArrayList;
import java.util.StringJoiner;

public class MainActivity extends AppCompatActivity {
    EditText userInput;
    TextView displayWords;
    MorseCodeMessage mc;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       displayWords = findViewById(R.id.displayWords);
       userInput = findViewById(R.id.message);
       String usrMsg = userInput.getText().toString();
       displayWords.setText(translateTextToMorse(userInput.getText().toString()));
        mc = new MorseCodeMessage(this, translateTextToMorse(usrMsg));
        try {
            mc.flash(translateTextToMorse(usrMsg).split(" "));
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    //loads json file containing translation table for letter to morse code conversion
    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getAssets().open("morse.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");

        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    //convert a string of text into a string of morse code dits and dahs followed spaces
    public String translateTextToMorse(String message) {
        try {
            JSONObject morseDict = new JSONObject(loadJSONFromAsset());
            CharacterIterator it = new StringCharacterIterator(message);
            ArrayList<String> morseCodeArr = new ArrayList<>();
            String code;
            String ditOrDah;

            for(char ch = it.first(); ch != CharacterIterator.DONE; ch = it.next()) {
                code = morseDict.getString(String.valueOf(ch).toLowerCase());

                CharacterIterator it2 = new StringCharacterIterator(code);
                for(char ch2 = it2.first(); ch2 != CharacterIterator.DONE; ch2 = it2.next()) {
                    ditOrDah = String.valueOf(ch2);
                    morseCodeArr.add(ditOrDah);
                }
            }


            StringJoiner sb = new StringJoiner(" ");

            for(String s : morseCodeArr) {
                sb.add(s);
            }
            return sb.toString();
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}

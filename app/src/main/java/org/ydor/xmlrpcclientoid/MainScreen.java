package org.ydor.xmlrpcclientoid;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;


/**
 * Main activity
 * @author rodolfoap on 7/21/15.
 */

public class MainScreen extends ActionBarActivity implements Command.Listener {
    /** A message that goes on the main screen log */
    String logMessage="";

    /** Some screen elements */
    TextView textView1;
    Spinner sItems;
    Hashtable<String, String> qryResult;
    List<String> spinnerList;

    /**
     * The XML-RPC server's URL
     * Example: "http://192.168.1.31:8080/RPC2";
     */
    public String SERVICE_URL;

    /**
     * The command to be encapsulated into XML
     * Example command: "server.addition\na:1\nb:2: which may add a and b on the server
     */
    public List<Command> commandList;

    /**
     * After any action, reload preferences
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i("XmlRpcClientoid", new Throwable().getStackTrace()[0].toString()); /* RODOLFO */
        loadPreferences();
    }

    /**
     * After any action, reload preferences
     */
    @Override
    public void onResume() {
        super.onResume();
        Log.i("XmlRpcClientoid", new Throwable().getStackTrace()[0].toString()); /* RODOLFO */
        loadPreferences();
    }

    /**
     * After startup, load screen, preferences and the spinner
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen);
        Log.i("XmlRpcClientoid", new Throwable().getStackTrace()[0].toString()); /* RODOLFO */
        textView1 = (TextView) findViewById(R.id.textView1);
        // Scrolling requires this:
        textView1.setMovementMethod(new ScrollingMovementMethod());
        sItems = (Spinner) findViewById(R.id.cmdSpinner);
        this.commandList=new ArrayList<Command>();
        this.spinnerList=new ArrayList<String>();
        loadPreferences();
        loadSpinner();
    }

    /**
     * Inflating the menu
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.i("XmlRpcClientoid", new Throwable().getStackTrace()[0].toString()); /* RODOLFO */
        getMenuInflater().inflate(R.menu.menu_main_screen, menu);
        return true;
    }

    /**
     * After any action, reload preferences
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.i("XmlRpcClientoid", new Throwable().getStackTrace()[0].toString()); /* RODOLFO */
        int id = item.getItemId();
        Intent myIntent = new Intent(MainScreen.this, SettingsActivity.class);
        MainScreen.this.startActivity(myIntent);
        loadPreferences();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * The main log control appender
     */
    public void appendLog(String message){
        Log.i("XmlRpcClientoid", new Throwable().getStackTrace()[0].toString()); /* RODOLFO */
        this.logMessage=this.logMessage+message+"\n";
        textView1.setText(this.logMessage);
    }

    /**
     * Loading preferences method
     */
    private void loadPreferences(){
        Log.i("XmlRpcClientoid", new Throwable().getStackTrace()[0].toString()); /* RODOLFO */
        SharedPreferences mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        this.SERVICE_URL = mySharedPreferences.getString("service_url", "NA");
        this.commandList.clear();
        Command tmpCommand;
        String tmpCommString;
        for(int a=1; a<33; a++) {
            tmpCommString=mySharedPreferences.getString("command_"+a, "");
            if(tmpCommString.length()>0) {
                tmpCommand = new Command(tmpCommString);
                tmpCommand.listeners.add(this);
                this.commandList.add(tmpCommand);
            }
        }
    }

    /**
     * Spinner loading
     */
    private void loadSpinner() {
        Log.i("XmlRpcClientoid", new Throwable().getStackTrace()[0].toString()); /* RODOLFO */
        this.spinnerList.clear();
        for (Command c: commandList) {
            this.spinnerList.add(c.command);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sItems.setAdapter(adapter);
    }

    /**
     * Execute button calls the command execute() method
     */
    public void buttonExecute(View view){
        Log.i("XmlRpcClientoid", new Throwable().getStackTrace()[0].toString()); /* RODOLFO */
        String selected = sItems.getSelectedItem().toString();
        for(Command c: commandList) {
            if (selected.equals(c.command)) {
                c.execute(this.SERVICE_URL);
                this.appendLog("+ " + c.command.toString());
            }
        }
    }

    /**
     * Clearing the log with the button
     */
    public void clearLog(View view){
        Log.i("XmlRpcClientoid", new Throwable().getStackTrace()[0].toString()); /* RODOLFO */
        this.logMessage="";
        textView1.setText(this.logMessage);
    }

    /**
     * App Settings button
     */
    public void appSettings(View view){
        Log.i("XmlRpcClientoid", new Throwable().getStackTrace()[0].toString()); /* RODOLFO */
        Intent myIntent = new Intent(MainScreen.this, SettingsActivity.class);
        MainScreen.this.startActivity(myIntent);
        loadPreferences();
        loadSpinner();
    }

    /**
     * Close app button
     */
    public void closeApp(View view){
        Log.i("XmlRpcClientoid", new Throwable().getStackTrace()[0].toString()); /* RODOLFO */
        finish();
    }
}
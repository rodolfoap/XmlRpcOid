package org.ydor.xmlrpcclientoid;

import android.os.AsyncTask;
import android.util.Log;

import org.xmlrpc.android.XMLRPCClient;
import org.xmlrpc.android.XMLRPCException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * The executed command
 * @author rodolfoap on 7/21/15.
 */
public class Command {

    /** The command that is being executed is on a string */
    public String command;

    /** The parameters are parsed into a map*/
    public Map<String, String> params;

    /** Listeners to append their logs */
    public List<Listener> listeners = new ArrayList<>();

    /** The listeners interface */
    public interface Listener {
        public void appendLog(String msg);
    }

    /**
     * The constructor parses the command,
     * stores the first line into this.command,
     * and the parameters into the map this.params
     * @param cmdText The parseable command string
     */
    public Command(String cmdText){
        String paramArray[];
        String lines[] = cmdText.split("\\r?\\n");

        // this.command
        this.command=lines[0];

        // this.params
        this.params=new HashMap<String, String>();
        for(int a=1; a<lines.length; a++){
            paramArray=lines[a].split(":", 2);
            if(paramArray[0].length()>0) {
                this.params.put(paramArray[0], paramArray[1]);
            }
        }
    }

    /**
     * This is used for proper formatted output
     */
    @Override
    public String toString(){
        String text="{ COMMAND:"+this.command+"; ";
        for(String key: params.keySet()){
            text=text+"PARAM["+key+"]="+this.params.get(key)+"; ";
        }
        text=text+"}";
        return text;
    }

    /**
     * To avoid blocking behavior (for example, if the server does not answer), this is implemented on a thread;
     * uses the XMLRPCClient library by using the .callEx rpc call
     * @param serviceURL The server's XML-RPC service URL
     */
    public void execute(final String serviceURL) {
        Log.i("XmlRpcClientoid", new Throwable().getStackTrace()[0].toString()); /* RODOLFO */
        class MyThread extends AsyncTask<Command, Integer, String> {
            @Override
            protected String doInBackground(Command... commandArray) {
                Log.i("XmlRpcClientoid", new Throwable().getStackTrace()[0].toString()); /* RODOLFO */
                final XMLRPCClient server = new XMLRPCClient(serviceURL);
                HashMap<String, String> qryResult = null;
                try {
                    Log.i("XmlRpcClientoid", "THREADing: "+commandArray[0].toString()); /* RODOLFO */
                    qryResult = (HashMap<String, String>) server.callEx( commandArray[0].command, new Object[]{commandArray[0].params} );
                    return qryResult.get("status");
                } catch (XMLRPCException e) {
                    Log.i("XmlRpcClientoid", "XMLRPCServer call ERROR: "+e.toString());
                }
                return null;
            }

            @Override
            protected void onPostExecute(String result){
                Log.i("XmlRpcClientoid", new Throwable().getStackTrace()[0].toString()); /* RODOLFO */
                if(result!=null) {
                    notifyObservers("> " + result.toString()+"\n");
                } else {
                    notifyObservers("> ERR:NULL> Connection? Implemented? STATUS var?\n");
                }
            }
        }

        MyThread thread = new MyThread();
        thread.execute(new Command[] { this });
    }

    /**
     * Subscription to the observers list
     * @param message The log message that is sent to observer activity (for now, only Main)
     */
    private void notifyObservers(String message) {
        for (Listener listener : listeners) {
            listener.appendLog(message);
        }
    }
}
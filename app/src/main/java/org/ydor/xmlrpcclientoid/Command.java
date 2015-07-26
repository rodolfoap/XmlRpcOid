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
 * Created by rodolfoap on 7/21/15.
 */
public class Command {
    public String command;
    public Map<String, String> params;
    public List<Listener> listeners = new ArrayList<>();
    
    public interface Listener {
        public void appendLog(String msg);
    }

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
    @Override
    public String toString(){
        String text="{ COMMAND:"+this.command+"; ";
        for(String key: params.keySet()){
            text=text+"PARAM["+key+"]="+this.params.get(key)+"; ";
        }
        text=text+"}";
        return text;
    }

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

    private void notifyObservers(String message) {
        for (Listener listener : listeners) {
            listener.appendLog(message);
        }
    }
}
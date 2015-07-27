# XmlRpcOid

Well, I just needed to control my home debian server from my mobile device. There you have a simple app to do it, using xml-rpc, java, perl on the server side -despite you can use any XML-RPC server/language you want on the server side-. This uses plain xml over http, so the connection is plain, non-encripted. I don't need it because this runs inside a VPN, so I run it without encryption. Don't run this over the open web!

**Warning:** This application does not encrypt data. If you don't know what you are doing, buy a Windows box and a Windows phone and write bill.gates@microsoft.com to provide you of an app like this. You can open a door to hackers by writing open XML-RPC services. You've been warned, now it's your full responsibility.

## What is XML-RPC?

XML RPC is just a communications protocol that uses **XML** to execute remote procedures (**RPC** stands for Remote Procedure Call). You can send two numbers to the server and it can answer with the sum, or you can ask a server to reboot.

This is a simple example: asking the uptime. A os.uptime RPC call is executed, it requires no parameters.

    <?xml version='1.0'?>
    <methodCall>
            <methodName>os.uptime</methodName>
            <params><param><value>
                            <struct/>
                            </value>
                    </param>
            </params>
    </methodCall>

So, the server answers something like:

    <?xml.version="1.0"?>
    <methodResponse>
            <params> <param> <value> <struct> 
                                            <member>
                                                    <name>status</name>
                                                    <value>
                                                            <string>11:56:24 up 9 days, 1 2:01,  2 users,   load average: 0.02, 0.02, 0.05. </string>
                                                    </value>
                                            </member>
                                    </struct>
                            </value>
                    </param>
            </params>
    </methodResponse>

---

Now, an example of using two parameters. The rpc.ProductTest RPC call is executed with two parameters: var1=5 and var2=12.

    <?xml.version='1.0'.?>
    <methodCall>
	    <methodName>
		    rpc.ProductTest
	    </methodName>
	    <params> <param> <value> <struct>
					    <member>
						    <name>var1</name>
						    <value>
							    <string>5</string>
						    </value>
					    </member>
					    <member>
						    <name>var 2</name>
						    <value>
							    <string>12</string>
						    </value>
					    </member>
				    </struct>
			    </value>
		    </param>
	    </params>
    </methodCall>

So, the server answers with the product: 5 * 12 = 60.

    <?xml.version="1.0"?>
    <methodResponse>
	    <params> <param> <value> <struct>
					    <member>
						    <name>status</name>
						    <value>
							    <string>60</string>
						    </value>
					    </member>
				    </struct>
			    </value>
		    </param>
	    </params>
    </methodResponse>

---

## Server side

You can use any XML-RPC server. This example uses a perl daemon to test the two previous XML dumps.

Paste this on a file named xmlrpcserver.pl:

---

    use Frontier::Daemon;
    use Data::Dumper;
    
    sub rpcTest {
    
	    %params = %{@_[0]};
	    $var1=$params{'var1'};
	    $var2=$params{'var2'};
	    $prod=$var1*$var2;
    
	    # Always return the 'status' variable, it is used to show the result on android screen.
	    return {'status' => "$prod"};
    }
    
    sub osUptime {
	    return {'status' => `uptime`};
    }
    
    $methods = {
	    'rpc.ProductTest' 	=> \&rpcTest,
	    'os.uptime' 		=> \&osUptime
    };
    Frontier::Daemon->new(LocalPort => 8080, methods => $methods, ReuseAddr => 1);

---

Execute it:

    # perl xmlrpcserver.pl


You can find a bigger example on the /lib directory of this repository. You can replace the *uptime* command with *shutdown*, a process *kill*, a *service start*, a *useradd*, a *grep log*, whatever you can imagine. The only drawback is your security.

## Client side

1. Install the apk on your phone (find it on the /apk dir of this repository). Better if you can compile it yourself with Android Studio.

2. Start the appplication.

3. Go to SETTINGS.

	- On Service URL, enter the URL of your server. The Perl Example uses something like

    http://your.server.address:8080/RPC2

	![Service URL example](https://github.com/rodolfoap/XmlRpcOid/blob/master/web/ServiceURL.png "Service URL example")

	- On Command 7, write this:
	
    os.uptime

	- On Command 8, write this:

    rpc.ProductTest
    var1:5
    var2:12

	![Command example](https://github.com/rodolfoap/XmlRpcOid/blob/master/web/Command8.png "Command entry example")

4. Explanation: 

* The first line is the xml-rpc command that will be called (see the **$methods** variable on the perl server). Be sure to choose a clear name, the RPC command name is shown on the main screen combo box.

* The second and further lines are interpreted as parameters. They should have the format VARIABLE:VALUE (word, colon, value, which is transmitted as a string, always, so you can interpret it as you like on the server).

* If the Command entry is empty, it will not be shown on the main screen commands combo box.

* More lines can be added by using the ENTER key on the android keyboard.

5. Test the application:

	- Choose a command from the combo box
	- Press the "Execute" button.

	![Execution example](https://github.com/rodolfoap/XmlRpcOid/blob/master/web/Screenshot1.png "Execution example")

## Credits

* Background: http://www.xanadu-fx.com/tiles/fractals.php

* Android Xml-Rpc lib: http://code.google.com/p/android-xmlrpc

#!/usr/bin/perl
use Frontier::Daemon;
use Data::Dumper;
use Log::Log4perl qw(:easy);
Log::Log4perl::init("/etc/log4perl.conf");
$logger = Log::Log4perl->get_logger();

# Simplest test.

sub rpcTest {
	print "All parameters are received as the first element of the array:\n", Dumper(@_[0]);

	%params = %{@_[0]};
	print "We can dereference the array into a hash:\n", Dumper(\%params);

	print "Now, we can extract a variable...\n", Dumper($params{'var1'}), Dumper($params{'var2'});
	$var1=$params{'var1'};
	$var2=$params{'var2'};

	$prod=$var1*$var2;
	print "...and calculate some output.\n", Dumper($prod);
	
	print "Always return the 'status' variable=$prod, it is used to show the result on android screen.\n";
	return {'status' => "RPC.TEST [$var1, $var2] -> [$prod] OK."};
}
#---------------------------------------------------------------------------

# Uptime example

sub osUptime {
	$status=`uptime`;
	return {'status' => "$status"};
}

#---------------------------------------------------------------------------

# My miniDLNA server control

sub mdStart {
	$logger->info('XmlRpc call to md.start');
	system('/etc/init.d/minidlna', 'start' );
	return {'status' => "Minidlna service started, system returned: $?"};
}
sub mdStop {
	$logger->info('XmlRpc call to md.stop');
	system('/etc/init.d/minidlna', 'stop' );
	return {'status' => "Minidlna service stopped, system returned: $?"};
}
sub mdScan {
	$logger->info('XmlRpc call to md.scan');
	system('/etc/init.d/minidlna', 'force-reload' );
	return {'status' => "Minidlna service scanning, system returned: $?"};
}

#---------------------------------------------------------------------------

# A very dangerous command: launch whatever you want as a parameter. 

#sub osCommand {
#	$cmd=${@_[0]}{'cmd'};
#	$logger->info('XmlRpc call to os.command');
#	$status=`$cmd`;
#	return {'status' => "$status"};
#}

#---------------------------------------------------------------------------
$methods = {
	'rpc.ProductTest' 	=> \&rpcTest,
#	'os.command' 		=> \&osCommand,
	'os.uptime' 		=> \&osUptime,
	'md.start' 		=> \&mdStart,
	'md.stop' 		=> \&mdStop,
	'md.scan' 		=> \&mdScan
};
Frontier::Daemon->new(LocalPort => 8080, methods => $methods, ReuseAddr => 1);

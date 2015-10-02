ServerQueryExtender
=========

![](logo.png)

ServerQueryExtender is a simple server-only mod that'll extend the ServerQuery.
This allows you to fetch more data about your server without logging in!


## Links ##
Curse: [http://minecraft.curseforge.com/mc-mods/236296-serverqueryextender](http://minecraft.curseforge.com/mc-mods/236296-serverqueryextender)

Required mod: [K4Lib](http://minecraft.curseforge.com/mc-mods/224740-k4lib)

## License ##
This mod is released under the MMPLv2

## Pull requests ##
Yes, you can make pull requests for new variables, if implemented properly i'll merge them.

## Bugs ##
Please report bugs to the issues page. Put crashlogs in a pastebin or gist!

## API ##
There is no API as of yet, however i am planning on adding one, so mods can add their own data.

## Documentation ##
The mod works with standard [Minecraft UDP Query protocol](http://wiki.vg/Query). Make note that you do need to enable the query in the server settings for this mod to work.
For the special data to work, you need to send a payload of integer value 8.
After that, you request data by sending a json list of what you want.
(Keep in mind that the entire handshake packet does not go longer than 10 bytes!
 
### Sending ###
This list is in the format of either just strings, or objects with a key and args. For example, to request the time, you would send this:

	["time"]

If you want to request the time in a certain dimension:

	[{"key": "time", "args": 1}]

Keys are not caps sensitive, however i do recommend you send them in lowercase.

### Receiving ###
If we send the top json to the server, we would get a return message:

	{
		"TIME": {
			"1": "13:37"
		}
	}

Ofcourse, if you request more variables, you'll get more in the return object.


### Variables ###
These are the variables that can be requested, along with arguments:

- time - Gives the time in the overworld if used without arguments
	- dimension id
- players - Gives the players on the server. Argument is optional
	- "latestdeath" - Received the players and their latest death
- daynight - Returns a boolean whether or not it's daytime in the overworld if used without arguments
	- dimension id
- dimensions - Returns all the available dimensions, including their id
- uptime - How long the server has been running
- deaths - If used without argument, this will return all the players who have died more than once
	- playername - Returns a list of all the deaths this played had since the mod was installed.

Note that this list is not yet complete and i aim to add more stuff to it!

### Examples ###
I used a python file for testing this. I've uploaded it to pastebin: [http://pastebin.com/g2zbGHHs](http://pastebin.com/g2zbGHHs)

Usage:

	#!/usr/bin/python
	import mcquery
	import time
	
	print 'Ctrl-C to exit'
	
	host = ""
	port=''
	
	if host == '':
	    host = 'localhost'
	if port == '': 
	    port = 25565
	else: 
	    port = int(port)
	
	
	print "Connecting..."
	q = mcquery.MCQuery(host, port)
	print "Connected."
	
	print q.extra_stat("time", {"key":"players", "args":"latestdeath"}, "uptime", "dimensions", {"key":"time", "args":1}, {"key":"time", "args":-1}, "deaths", {"key":"deaths", "args":"K4Unl"})

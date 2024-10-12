Client-Server-Fileserver Project
Nick Newman
5295926
10/6/2024
EEL 4781 Computer Communication Networks

Within this folder are client-folder and server-folder. You will want to open
two sepperate terminal windows. One for the client and one for the server.
Once you have these terminals open and have navigated to the appropriate
folders for each terminal please run the following in the appropriate terminals:

javac Client.java

and 

javac Server.java

*************************************************************************************************************

Once you have done this you are ready to run the file server. 

format:
--COMMAND = info regarding the command



In the client terminal the following are the usages:

--java Client = This will show you this usage if you need a reminder

--java Client <server-ip> <filename> = This will transfer a full file

--java Client <server-ip> <filename> [-s] START_BYTE = This will start at some specified point
                                                     in the document to the end of the document

--java Client <server-ip> <filename> [-e] END_BYTE = This will start at the beginning of the document
                                                   and end at some specified point

--java Client <server-ip> <filename> [-s] START_BYTE [-e] END_BYTE = This will send some chunk of data
                                                                     from a specified point in to the beginning
                                                                     of the document to some specified end point


the server-ip and filename must be input in the order shown. Putting them 
in any other order will fault the program. Your additional options can be put
in any order but the ones listesd are the most logicl and reccomended.

***************************************************************************************************************

In the server terminal the following are the usages:

--java Server = This will run the server. There will be no output reflected in the terminal

--java Server DEBUG=1 = This will output information such as: what file is being sent to what ip, the percentage completed 
                                                              sending in increments pf 10%, and lastly that the file has
                                                              completed sending to what ip.

Setup ALMA
==========

1. Setup Android SDK with support for Google Map application
2. Setup Google Map key API for your own class name for example com.you.alma similar to com.zulridzwan.alma
3. Clone the Alma, almatest, google-play-services_lib and library into your Github Account.
4. Setup your eclipse with Egit plugin to point to your Github account.
5. Alternatively, download Alma, almatest, google-play-services_lib and library as archive file and import them 
into eclipse using File->Import->Existing project into workspace.
6. Alternatively for google-play-services_lib and library you can import from your SDK folder 
under adt-bundle...->sdk->extras->google->google_play_services->libproject->google-play-services_lib. For "library" 
you may search for Google Map v2 clustering example / tutorial and download the sample code from there. 
7.Make sure for google-play-services_lib and library project property is set to "is library" as they are library projects.
8.Under "library" folder properties->Android choose google-play-services_lib as reference.
9.Under Alma folder properties choose both google-play-services_lib and library as reference.
10.Replace all classnames com.zulridzwan.alma with your own classname (as in number 2).
11.Edit the strings.xml file under Alma folder->res->values->strings.xml and replace the apikey value with your own Google Map API key.
12.Setup XAMPP or WAMP and create a folder called "asnaflocator" under the htdocs or www folder.
13.Copy the file asnaflocator.zip under the phpwebfiles under the Alma folder->phpwebfiles and extract the files into the 
asnaflocator folder in your web server.
14.Edit strings.xml under(in 11) and change the IP addresses for the read_url,update_url,insert_url and webroot with your 
server's IP address/domain
15.Create a Mysql database named asnaflocator and import the tables and view using the script asnaflocator.sql provided 
under asnaflocator folder in your web server
16.Try to execute the read_url.php from your browser and mobile phone / emulator to ensure your firewall is not blocking access 
to the web server.
17.If you still have problem, you may contact me at zulridzwan@gmail.com.
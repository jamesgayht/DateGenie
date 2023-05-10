# Restaurant & Attractions Searcher
### Search and save restaurants and attractions. Sample itineraries can also be generated. 

## Features / How to use:
1. Login Page: 
Testing account username: admin
Testing account password: asdf1234
Stores user's login credentials in MySQL with password encryption.

2. Sign Up Page: 
Username cannot be repeated, non-case sensitive. 
Email address cannot be repeated. 
Password and Retype Password must match before form can be submitted. 
Upon successful registration, an email will be sent to the user's email address to simulate verification process. 
User's data will be added to the database and can be used for login. 

3. Home Page
Background Image Panning Animation utilising CSS keyframe animations ()
Navbar and display cards desgined utilising Angular Material 

4. Search Restaurants / Attractions Page
Implemented keyword randomization upon entering the page to avoid showing empty page
Search keywords by in placing desired input into textbox 
Total amount of search results shown in pagination navbar
Click more details to get reviews, address, etc. 
Click favourites button to add to favourites 

5. Favourites Page
Showcases all added favourites stored in MySQL database
Click more details to get reviews, address, etc. 
Click Sample Itinerary to get model based AI generated itinerary 
Click Delete button to remove stored restaurant/attraction

* if no images are found for a restaurant / attraction, will default to golden retriever image

## Technologies: 
1. Java SpringBoot for the server 
2. AngularJS for the client 
3. MySQL, AWS S3 Bucket for the databases

## APIS: 
1. Singapore Tourism Board - Tourism Info Hub API
2. Google Maps API
3. OpenAI Chat API gpt-3.5-turbo
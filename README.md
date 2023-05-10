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

## Technologies: 
1. Java SpringBoot for the server 
2. AngularJS for the client 
3. MySQL, AWS S3 Bucket for the databases

## APIS: 
1. Singapore Tourism Board - Tourism Info Hub API
2. Google Maps API
3. OpenAI Chat API gpt-3.5-turbo
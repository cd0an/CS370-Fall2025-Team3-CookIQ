CookIQ – Personalized Meal Recommendation App

CookIQ is a Java-based desktop application that provides personalized meal recommendations based on user preferences, dietary restrictions, and recipe feedback. The system allows both guest users and registered users to explore recipes, like or dislike suggestions, and receive improved recommendations over time.

Features
* User registration and login
* Save and load user preferences
* Personalized recipe recommendations
* Like/Dislike swipe-style interface
* View previously liked recipes
* Request new recipe suggestions
* Google API integration for recipe images
* MongoDB integration for storing user data and preferences

Technologies Used
* Java Swing – User interface
* MongoDB – Stores user accounts, preferences, and liked recipes
* Google Custom Search API – Fetches recipe images
* Maven – Dependency management and project structure
* GitHub – Version control and team collaboration


How to Run CookIQ
1) Download or Clone the Repository: https://github.com/cd0an/CS370-Fall2025-Team3-CookIQ.git
2) Open the Project in Your IDE (e.g., IntelliJ, VS Code).
3) Install Java. CookIQ requires Java 17 or higher.
4) Install Maven for Java.
5) Locate the ImageService.java file under the Services folder.
6) Verify you have a Google API Key. If you already have one, skip to Step 9. Otherwise, continue. 
7) Copy the following API Key: AIzaSyCHa3fu4NsDuWU8WejIpNnFfHQm9_11p2w and paste it in the ImageService.java where indicated. 
9) Navigate to CookIQ.java in your IDE and click “Run” to launch the application.
10) If running from the IDE does not work, do the following steps: (a) Open a terminal in the project folder. (b) Compile the project: mvn clean compile (c) Run the application: mvn exec:java -Dexec.mainClass="cookiq.CookIQ"
11) The app will automatically start, connect to MongoDB, and load recipe images. 

Team Members
* Cindy – UI Design, Integration
* Zhuo – MongoDB & Data Management
* Mitchell – Recommendation Engine & Ranking Logic
* Miguel – Login System & Authentication

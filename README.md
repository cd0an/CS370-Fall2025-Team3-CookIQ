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
2) Obtain a Google API Key and Custom Search Engine ID. Use one of the following API keys:
   a) Key 1: AIzaSyCHa3fu4NsDuWU8WejIpNnFfHQm9_11p2w 
   b) Key 2: AIzaSyCVMTrJh4g2aRVmaystoHoPsSbJkJsMWDA
4) Insert one of the keys in the ImageService.java file where indicated, if not already
5) Launch the Application: Open the project in your IDE (IntelliJ, VS Code, Eclipse, etc.)
6) Navigate to: cookiq/CookIQ.java
7) Click Run. The app will automatically start, connect to MongoDB, and load recipe images. 

Team Members
* Cindy – UI Design, Integration
* Zhuo – MongoDB & Data Management
* Mitchell – Recommendation Engine & Ranking Logic
* Miguel – Login System & Authentication

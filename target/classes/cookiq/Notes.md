**Roles:**
- Cindy (UI Design)
- Zhuo (MongoDB Setup & Data Management)
- Mitchell (Recommendation Engine & Ranking Logic)
- Miguel (Login Setup)

**Changes:**
- Remove FeedbackService.java entirely and move the logic into RecommendationService.java 
- Instead of displaying real numbers for nutritional facts, display as low-calorie, high-calorie, low-protein, and high-protein 

**Remember:**
- Guest users will see duplicate liked recipes when resuggesting for more recipes (temporary session not implemented-no time)
- Logged-in users should not be resuggested recipes they already liked and disliked across sessions 
- Google API calls limits to 100 images per day
# Laws-of-Development
Android application that provides short, relevant personal-development articles that users can go through quickly and make the most out of. It utilizes a swiplable cards design to improve user interaction and simplify the use of the application. Swiping right saves the topic to your saved topics container, swiping left saves it to the disliked topics container and clicking on a topic displays a brief summary of it aimed to help the user decide whether to read the article or no.

![login_reg choice](https://user-images.githubusercontent.com/42480955/57981320-c3745500-79ea-11e9-8139-ed8c8443c7a8.PNG)

![reg page](https://user-images.githubusercontent.com/42480955/57981322-c3745500-79ea-11e9-885e-966064da94b6.PNG)

![choosePic](https://user-images.githubusercontent.com/42480955/57981355-2ebe2700-79eb-11e9-9e58-b06eaec98902.PNG)

![login page](https://user-images.githubusercontent.com/42480955/57981323-c40ceb80-79ea-11e9-804b-03ce496a5e13.PNG)

![main](https://user-images.githubusercontent.com/42480955/73044981-edef9b00-3e20-11ea-8f9f-2b0425a83ebe.PNG)

![main3](https://user-images.githubusercontent.com/42480955/73045001-fea01100-3e20-11ea-9816-ab43905d4aca.PNG)

![side menu](https://user-images.githubusercontent.com/42480955/57981346-07675a00-79eb-11e9-9a02-95b4b5f89b36.PNG)

![saved](https://user-images.githubusercontent.com/42480955/57981369-675e0080-79eb-11e9-9348-7361b40839a4.PNG)

![disliked](https://user-images.githubusercontent.com/42480955/57981379-7ba1fd80-79eb-11e9-9956-f5b91f899574.PNG)

![topic display](https://user-images.githubusercontent.com/42480955/73045072-3f982580-3e21-11ea-8c96-2cfa23842bac.PNG)

# Developed Using:
  - Android Studio
  - Firebase 
  - Swipecards (https://github.com/Diolor/Swipecards)
  - Glide (https://github.com/bumptech/glide)
  
# Running the Application:
  1. Register and connect app to firebase 
    - The DB schema should be as such:
    ![db schema](https://user-images.githubusercontent.com/42480955/57959620-89effc80-78b9-11e9-94a8-c75e0395c4d5.PNG)
    
  2. Copy the files in the code folder to the following location: app/src/main/java/-/-/-/-
    - The dashes represent your package name which will be different from mine
    
  3. Copy the AndroidManifest file and make sure to add your package name in the package name field
  
  3. Copy the drawable, menu, values and layout folders to the res folder created when a new application is creatd 
  
  4. Run the application
  
# Note
   - The Application currently doesnt display actual articles. It is still a work in progress.


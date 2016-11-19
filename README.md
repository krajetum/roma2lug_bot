# **Roma2LugBot**
This program controls a bot that manages the assistance for the Roma2Lug Organization.

## Required Files
There are some missing files that you shall provide to this code to work.

- ### client_id.json
  Is the key to access gmail account.
  You can make and download it from the Google Api Console.
  You have to put this file under ``` src/resources ``` .
  
- ### properties.properties 

  Located under ``` /config/properties.properties``` 

  This file contains 4 properties:
    - **USERNAME**: the name of the bot (currently not used)
    - **TOKEN**: the token to access the telegram bot api
    - **LUG_TEST_ID**: the id of the the test chat (currently the main) 
    - **LUG_ID**: the id of the main chat (currently not used)
    
- ### lug_members.json
  
  Located under ``` /data/lug_members.json``` 

  This file has this structure:
    ```javascript
    [
      {
        "name":"value"
        "active":[true/false]
      },
      ...
    ]
    ``` 
    
## Project Tree

```
├───.idea
│   ├───copyright
│   └───libraries
├───config
├───data
├───src
│   └───main
│       ├───java
│       │   └───krajetum
│       │       └───LTB
│       │           ├───configs
│       │           └───utils
│       └───resources
├───stickers
│   └───quarantennitristi
└───tmp
```

- ###### .idea/ 

  Intellj IDE files
  
- ###### config/
  
  Contains only the file properties.properties
  
- ###### data/ 
  
  Contains only the file lug_member.json

- ###### src/main/java/krajetum/LTB/
  
  It's the main package, it contains the main classes that manage the bot
  
- ###### src/main/java/krajetum/LTB/configs/
  
  Some config classes
  
- ###### src/main/java/krajetum/LTB/utils/
  
  Utility Classes

- ###### src/resources/
  
  Contains only the client_id.json file
  
- ###### stickers/
  
  Contains all the sticker used by the bot
  
- ###### tmp/

  Usually empty, used to download the inline images in the parsed email

# **Credits**
This project is made by @krajetum and the Roma2Lug organization.
Ensure to give credits if you reuse this code

![alt text](http://www.lffl.org/wp-content/uploads/2016/06/open-source-software.jpg "Opensource")

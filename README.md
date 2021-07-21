# Hudl_Project

This repository houses the UI test cases written around the validation of [Login Page](https://www.hudl.com/login).

I've used `Java + Selenium` to achieve automation around the login page

The repo includes the following files:
- HudlLoginTest.java - All tests cases reside in this file
- Helper_Selectors.java - PageOject class for all UI selectors 
- configurations.properties - valid username/password to be used to login into the hudl site
- build.gradle - basic dependencies declared that will be used to run the tests
- chromedriver.exe - driver to use google chrome to run the tests.

## Pre-Setup and Running using IntelliJ
- Clone the repo
- Open the project in IntelliJ or preferred IDE
- wait for gradle tasks to complete(downloading dependenices and building project)
- Go to IntelliJ `Settings > Build,Execution,Deployment > Build Tools > Gradle`
- Make sure to change the setting for "Run tests using" from `Gradle` to `IntelliJ IDEA`
- Navigate to HudlLoginTest.java and right click on it and hit `Run HudlLoginTest`
- Make sure the test runs as a Junit configuration

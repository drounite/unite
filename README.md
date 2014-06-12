unite
=====

Unite is an open-source HTTP request library for Android,<br>built to significantly facilitate the work and coding time, and makes it easy to create and work with HTTP requests.

###Importing the Unite library into project

There are two ways for you to import the library into your Android project:
#### The JAR file way
1. Create a folder called ```libs``` (if not already exists) in your project's root folder.
2. Copy [unite.jar](https://github.com/drounite/unite/raw/master/bin/unite.jar) to the libs folder.
3. Right click on the JAR file and then select Build Path > Add to Build Path, which will create a folder called 'Referenced Libraries' within your project.

#### The Library Project way
1. Download and unzip [unite library project](https://github.com/drounite/unite/archive/master.zip)
2. Go to your Android project and slect File > New > Other
3. Select Android > Android Project from existing source
4. Click "Next" button
5. Click "Browse..." button and navigate to unzipped Unite project
6. Click Finish (Now Unite project is in your workspace)
7. Right-click on your project > Properties
8. In Android > Library section click Add
9. select recently added Unite project > Ok

###Usage

Unite contains Interface definition for a callback to be invoked when HTTP Response is received. So just implement ```OnResponseListener``` and add the unimplemented method ```onResponseReceived```:
```java
public class MainActivity extends Activity implements OnResponseListener {
  
  .
  .
  .
  
  @Override
	public void onResponseReceived(Response response) {
		// Handle your response here
  }
  
}
```

Create a Unite Client instance:
```java
Client client = new Client();
```

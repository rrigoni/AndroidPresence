# AndroidPresence
Simple presence library for android to identify when the app became "online" and "offline". When the user is seeing a screen, the app is considered as online, when the app is in background, the app is considered as offline.  Its a library I build to use in a chat app based on the  work of  @steveliles. 



Add it in your root build.gradle at the end of repositories:

	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
  
  
  Step 2. Add the dependency

	dependencies {
	        compile 'com.github.rrigoni:AndroidPresence:1.0.0'
	}
  
  
  Init the library in your Application's file like this:
  
  	Presence.init(this, new Presence.PresenceChangeListener() {
		    @Override
		    public void onBecameOnline() {
			Toast.makeText(SampleApp.this, "We are Online", Toast.LENGTH_SHORT).show();
		    }

		    @Override
		    public void onBecameOffline() {
			Toast.makeText(SampleApp.this, "We are Offline", Toast.LENGTH_SHORT).show();
		    }
        });
        
        
   Or like this:
   
   
   	public class SampleApp extends Application implements Presence.PresenceChangeListener {

		    @Override
		    public void onCreate() {
			super.onCreate();
			Presence.init(this);
		    }

		    @Override
		    public void onBecameOnline() {
			Toast.makeText(this, "We are Online", Toast.LENGTH_SHORT).show();
		    }

		    @Override
		    public void onBecameOffline() {
			Toast.makeText(this, "We are Offline", Toast.LENGTH_SHORT).show();
		    }
		}

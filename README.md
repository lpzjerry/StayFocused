# StayFocused
<p>An android app to improve working efficiency.</p>

[Download APK](https://github.com/lpzjerry/StayFocused/raw/master/StayFocused.apk)

[Project Page](https://home.cs.dartmouth.edu/~pengze/stayfocused/)

[Demo Video](https://drive.google.com/file/d/1WkFBHc92XS_Vd6gbQXKYVTEBVcokjeWZ/view?usp=sharing)

### Who Did What
- Xiangxin Kong:
	- UI Desgin
	- Timer Service
- Pengze Liu:
	- Everything related to focusing page (wheel picker, block quit buttons, notification)
	- Splash page
- Peixuan Wang
	- Login(Email, Google login) and Signup
	- Cloud backend using firebase
	- Record page
	- Sharing (APP, Record)
- Yinyin Zheng
	- Everything related to to-do page
	- Room database

### Dependencies
- Android Studio 3.6.1
- Android 8.0 or above
```
dependencies {
    implementation fileTree(dir: 'libs', include: ['*.jar'])
    implementation 'androidx.appcompat:appcompat:1.1.0'
    implementation 'androidx.legacy:legacy-support-v4:1.0.0'
    implementation 'androidx.constraintlayout:constraintlayout:1.1.3'
    implementation 'androidx.navigation:navigation-fragment:2.0.0'
    implementation 'androidx.navigation:navigation-ui:2.0.0'
    implementation 'androidx.lifecycle:lifecycle-extensions:2.0.0'
    implementation "androidx.lifecycle:lifecycle-extensions:$rootProject.archLifecycleVersion"
    implementation 'androidx.annotation:annotation:1.0.2'
    implementation "androidx.room:room-runtime:$rootProject.roomVersion"
    implementation 'com.google.firebase:firebase-analytics:17.2.2'
    implementation 'com.google.firebase:firebase-database:19.2.1'
    implementation 'com.google.firebase:firebase-auth:19.2.0'
    implementation 'com.google.android.gms:play-services-auth:17.0.0'
    implementation 'com.google.android.material:material:1.1.0'
    implementation "com.google.android.material:material:$rootProject.materialVersion"
    implementation 'com.contrarywind:Android-PickerView:4.1.9'
    implementation 'com.contrarywind:wheelview:4.1.0'
    implementation 'com.github.dhimant1990:TimerTextView:v1.0'
    
    annotationProcessor "androidx.room:room-compiler:$rootProject.roomVersion"
    annotationProcessor "androidx.lifecycle:lifecycle-compiler:$rootProject.archLifecycleVersion"
    
    testImplementation 'junit:junit:4.12'
    
    androidTestImplementation "androidx.arch.core:core-testing:$rootProject.coreTestingVersion"
    androidTestImplementation "androidx.room:room-testing:$rootProject.roomVersion"
    androidTestImplementation 'androidx.test.ext:junit:1.1.1'
    androidTestImplementation 'androidx.test.espresso:espresso-core:3.2.0'
}
```

### References
- OFFICIAL DOCUMENTS:
	- https://developers.google.com/identity/sign-in/android/start-integrating
	- https://developer.android.com/jetpack/docs/guide
	- https://developer.android.com/training/sharing/send
	- https://developer.android.com/topic/libraries/architecture/index.html
	- https://codelabs.developers.google.com/codelabs/android-room-with-a-view/?from=singlemessage&isappinstalled=0#0
- COURSE WEBSITE:
	- https://www.cs.dartmouth.edu/~xingdong/Teaching/CS65/lab3/lab3.html
	- https://www.cs.dartmouth.edu/~xingdong/Teaching/CS65/lecture19/lecture19.html
	- https://www.cs.dartmouth.edu/~xingdong/Teaching/CS65/lecture-firebase/firebase-lecture.htm
- STACKOVERFLOW AND OTHER ONLINE SOURCES:
	- https://stackoverflow.com/questions/33705720/how-to-hide-status-bar/33705740
	- https://stackoverflow.com/questions/8881951/detect-home-button-press-in-android
	- https://stackoverflow.com/questions/28937106/how-to-make-custom-dialog-with-rounded-corners-in-android
	- https://medium.com/@zackcosborn/step-by-step-recyclerview-swipe-to-delete-and-undo-7bbae1fce27e
- UI AND ICONS:
	- https://material.io/resources/icons/?style=baseline
	- https://colorhunt.co/palette/171304
	- https://www.flaticon.com/authors/freepik
	- https://www.flaticon.com/authors/those-icons
	- https://www.flaticon.com/authors/smartline
	- https://www.flaticon.com/authors/pixel-perfect

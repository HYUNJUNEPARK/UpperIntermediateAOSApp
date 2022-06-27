Github Auth
로그인 -> 프로필 이미지 -> Developer settings -> OAuth Apps -> 정보 등록 -> Client ID/Client secrets 복사

gradle.properties(Project Properties)
하단에 GITHUB_CLIENT_ID/GITHUB_CLIENT_SECRET 붙여넣기

```
GITHUB_CLIENT_ID=""
GITHUB_CLIENT_SECRET=""
```

build.gradle(:app)
defaultConfig 블럭에 GITHUB_CLIENT_ID/GITHUB_CLIENT_SECRET 등록

```
buildConfigField "String", "GITHUB_CLIENT_ID", project.properties["GITHUB_CLIENT_ID"]
buildConfigField "String", "GITHUB_CLIENT_SECRET", project.properties["GITHUB_CLIENT_SECRET"]
```


/////
Chrome Custom Tabs

-기존 웹 페이지를 보여주는 방식 : 1)외부 브라우저(ex.크롬앱) 2)internal WebView
1)외부 브라우저(ex.크롬앱) : Context switch 성격이 강하고 커스텀이 어려움
2)internal WebView  : Context switch 성격이 약하고 커스텀이 쉽지만 state share 불가, standard 를 맞춰 support 하기 어려운 단점이 있음
**-> 외부 브라우저 만큼 호환성을 갖고 있고 internal WebView 처럼 쓸 수 있는 support library**
**-> 1)2) 보다 2배 빠른 성능**
-자신이 제공하는 컨텐츠를 보여줄 때는 WebView 를 쓰는 것도 좋은 방법이지만 자신이 관리하는 내용이 아닌 외부 link 로 연결하는 경우에는 Chrome Custom Tab 을 추천

```kotlin
//build.gradle
implementation 'androidx.browser:browser:1.4.0'

//실행
CustomTabsIntent.Builder().build().also { customTabsIntent ->
    customTabsIntent.launchUrl(Context, Uri)
}
```
/////




/////

android:launchMode="singleTask"
이거 해줘야 토큰을 갖고 온 상태에서 앱이 안죽음 
/////





/////
override fun onNewIntent(intent: Intent?) {

/////

//TODO study
https://developer.android.com/topic/libraries/architecture/datastore?hl=ko

PreferenceManager getDefaultSharedPreferences deprecated in Android Q</br>
https://stackoverflow.com/questions/56833657/preferencemanager-getdefaultsharedpreferences-deprecated-in-android-q</br>
***implementation "androidx.preference:preference-ktx:1.1.1"***

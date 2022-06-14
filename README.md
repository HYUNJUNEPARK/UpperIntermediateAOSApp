# Music Player

<img src="https://github.com/HYUNJUNEPARK/ImageRepository/blob/master/UpperIntermediateApp/Music%20Player1.jpg" height="400"/>
<img src="https://github.com/HYUNJUNEPARK/ImageRepository/blob/master/UpperIntermediateApp/Music%20Player2.jpg" height="400"/>
<img src="https://github.com/HYUNJUNEPARK/ImageRepository/blob/master/UpperIntermediateApp/Music%20Player3.jpg" height="400"/>

---
1. <a href = "#content1">androidx.constraintlayout.widget.Group</a></br>
2. <a href = "#content2">mapIndexed</a></br>
3. <a href = "#content3">postDelay</a></br>
4. <a href = "#content4">RemoteViews</a></br>
* <a href = "#ref">참고링크</a>
---
><a id = "content1">**1. androidx.constraintlayout.widget.Group**</a></br>

-ConstraintLayout 은 Group 위젯을 제공함</br>
-분산되어 있는 컴포넌트들을 한번에 제어하기 편리해짐</br>
ex)특정 컴포넌트들을 묶어 한번에 visible/invisible 처리</br>

<br></br>
<br></br>

><a id = "content2">**2. mapIndexed**</a></br>

-iterable 을 대상으로 map 연산 시 원소 뿐 아니라 해당 원소가 갖고 있는 인덱스를 함께 묶어 매핑해주는 함수</br>
-two-argument 함수를 인자로 받는 합성 함수</br>

```kotlin
val names = listOf("a", "b")
names.mapIndexed{ index, string ->
    userPairingFun(index, string)
}
//(0, a)
//(1, b)
```

<br></br>
<br></br>

><a id = "content3">**3. postDelay**</a></br>

-앞의 과정이 약간의 시간이 필요하거나 바로 어떤 명령을 실행하지 않고 잠시 딜레이를 갖고 실행이 필요할 때 사용</br>

```java
new Handler().postDelayed(new Runnable() {
    @Override
    public void run()
    {
        //딜레이 후 시작할 코드 작성
    }
}, 600); // 0.6초 정도 딜레이를 준 후 시작
```

```kotlin
private val updateSeekRunnable = Runnable {
    updateSeekBar()
}

view?.postDelayed(updateSeekRunnable, 1000)
```
<br></br>
<br></br>

><a id = "content4">**4. RemoteViews**</a></br>

-Playing state 가 바뀔 때, Fragment 컨트롤에서 음원을 컨트롤할 때 `notifyNotification()` 가 실행되며 notification 을 띄워줌</br>
-리스이클러 뷰에서 아이템 클릭 시 `Service` 가 실행되며 notification 을 띄워줌</br>
-Notification ui 를 커스텀 하기 위해서는 `RemoteViews` 를 사용함</br>

```kotlin
mContentView = RemoteViews(context.packageName, R.layout.notification)

GlideApp.with(context)
    .asBitmap()
    .load(coverURL)
    .into(object : CustomTarget<Bitmap>() {
        //load 에 명시한 이미지를 불러왔을 때 자동으로 호출됨
        override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
            mContentView.setImageViewBitmap(R.id.coverImage, resource)
        }
        override fun onLoadCleared(placeholder: Drawable?) {  }
    })
mContentView.setTextViewText(R.id.title, "$title")
mContentView.setTextViewText(R.id.artist, "$artist")
```

Notification Ui 버튼 내부 동작에는 `PendingIntent` 를 사용함</br>

```kotlin
private fun skipPrevImageViewClicked() {
    val skipNextIntent = Intent(context, ForegroundService::class.java)
    skipNextIntent.action = SKIP_PREV

    val pendingIntent = PendingIntent.getService(
        context,
        SKIP_PREV_REQ_CODE,
        skipNextIntent,
        PendingIntent.FLAG_IMMUTABLE
    )
    mContentView.setOnClickPendingIntent(R.id.skipPrevImageView, pendingIntent)
}
```

<br></br>
<br></br>
---

><a id = "ref">**참고링크**</a></br>

Android Notification Load Image from Url Using Glide In Kotlin</br>
https://www.tutorialsbuzz.com/2021/02/android-notification-image-url-glide-setLargeIcon-bigPicture-kotlin.html</br>

android:usesCleartextTraffic="true"</br>
https://developside.tistory.com/85</br>

음원 출처 : ncs.io/music</br>
크롬 우측 상단 더보기 -> 도구 더보기 -> 개발자 도구 -> 왼쪽 상단 'Select an element in the inspect it' -> 원하는 음악을 클릭하면 data-url 을 확인할 수 있음 </br>
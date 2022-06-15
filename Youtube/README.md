<img src="https://github.com/HYUNJUNEPARK/ImageRepository/blob/master/UpperIntermediateApp/Youtube1.jpg"  width="200" height="400"/>
<img src="https://github.com/HYUNJUNEPARK/ImageRepository/blob/master/UpperIntermediateApp/Youtube2.jpg"  width="200" height="400"/>

1. <a href = "#content1">MotionLayout</a></br>
2<a href = "#content2">ExoPlayer</a></br>
3<a href = "#content3">Mock</a></br>
* <a href = "#ref">참고링크</a>
---
><a id = "content1">**1. MotionLayout**</a></br>

-ConstrainLayout 라이브러리의 일부</br>
-레이아웃 전환 UI 이동, 크기 조절 및 애니메이션에 사용</br>
-ConstrainLayout 을 MotionLayout 으로 바꾸기</br>
오른쪽 상단 Design -> Component Tree 레이아웃 우클릭 -> Convert to MotionLayout</br>


<br></br>
<br></br>

><a id = "content2">**2. ExoPlayer**</a></br>


`implementation 'com.google.android.exoplayer:exoplayer:2.13.3'`</br>
-오디오 및 동영상 재상 가능</br>
-오디오 및 동영상 재생 관련 강력한 기능들 포함(YouTube 에서 사용)</br>
-사용자 네트워크 상태에 적용해서 스트리밍 해주는 기술로 동영상 컨텐츠를 다양한 해상도로 인코딩해서 저장하고 이를 잘게 쪼개 저장해놨다가 사용자의 네트워크 상황에 따라서 최적의 동영상 콘텐츠 조각을 가져와 스트리밍</br>
(결과적으로 통신 상황에 따라 자동으로 해상도가 달라지면서 사용자는 끊기지 않고 동영상 시청 가능)</br>
-해당 앱은 ExoPlayer 의 기능만 따로 빼둔 BaseExoPlayerFragment 클래스를 만들고 이를 다른 클래스에 상속시켰음</br>
(ExoPlayer 기능만 중점으로 보려면 **BaseExoPlayerFragment** 클래스를 볼 것)</br>


<br></br>
**주요 컴포넌트**</br>
**1. PlayerView** : 비디오를 불러와서 실제 UI 에 뿌려줄 때 사용하는 UI 요소로 xml 에 선언을 해두고 SimpleExoPlayer 와 바인딩해 사용</br>
**2. ExoPlayer** : 비디오를 화면에 뿌려주는 가장 중요한 역할을 하는 컴포넌트</br>
**3. DataSourceFactory** : MediaSource 를 생성할 때 DataSourceFactory 를 넣어줘야 하는데, DataSource 는 URI 리소스로부터 데이터를 읽는데 사용</br>
**4. MediaItem** : Media 를 재생하기 위한 가장 작은 항목으로  Uri 를 기반으로 MediaItem 을 생성할 수 있으며 재생목록은  MediaItem 을 기반으로 생성됨</br>
**5. MediaSource** : ExoPlayer 에서 재생을 하기 위해 MediaSource 가 필요한데 MediaSource 는 MediaItem 을 이용하여 생성한 뒤에 ExoPlayer 와 연결해 사용 ex) ProgressiveMediaSource : 일반 미디어 파일 형식 재생</br>
**6. PlayerControlView** : 재생을 도와주는 UI 컴포넌트로 별다른 설정하지 않아도 기본적인 ControlView 를 제공해줌</br>
커스텀을 원할 때는 PlayerView 의 controller_layout_id 부분에 연결을 해주면 커스텀된 PlayerControlView 를 사용할 수 있음</br>


```kotlin
//
var player: SimpleExoPlayer? = null
player = SimpleExoPlayer.Builder(context).build()

//
binding.playerView.player = player

//onIsPlayingChanged : play 여부가 바뀔 때 마다 호출 됨
player?.addListener(object: Player.EventListener {
    override fun onIsPlayingChanged(isPlaying: Boolean) {
        super.onIsPlayingChanged(isPlaying)
    }
})
//
val dataSourceFactory = DefaultDataSourceFactory(context)
val progressiveMediaSource = ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(MediaItem.fromUri(Uri.parse(url)))
player?.setMediaSource(progressiveMediaSource)
player?.prepare() //데이터 가져옴
player?.play()

//
player.play()
player.pause()
```

<br></br>
<br></br>

><a id = "content3">**3. Mock**</a></br>

실제 객체를 만들기에는 비용과 시간이 많이 들거나 의존성이 크게 걸쳐져 있어서 테스트 시 제대로 구현하기 어려울 경우 가짜 객체를 만들어서 사용하는 기술</br>


<br></br>
<br></br>
---

><a id = "ref">**참고링크**</a></br>

의외로 잘 모르는 Fragment 의 Lifecycle</br>
https://readystory.tistory.com/199</br>

exoplayer</br>
https://developer.android.com/guide/topics/media/exoplayer?hl=ko</br>
https://exoplayer.dev/hello-world.html</br>
ExoPlayer 정리</br>
https://jungwoon.github.io/android/library/2020/11/06/ExoPlayer.html</br>

Mock</br>
https://www.mocky.io/</br>
sample video url</br>
https://gist.github.com/deepakpk009/99fd994da714996b296f11c3c371d5ee</br>
# 미세먼지 앱

<img src="이미지 주소" height="400"/>

* <a href = "#ref">참고링크</a>
---

dataBinding / Retrofit2 / coroutine / FusedLocationProviderClient / AppWidget

**1. API 호출 구조**</br>
Android(경도/위도) -> Kakao developers(TM 좌표 변환) -> 공공데이터포털(측정소명) -> 공공데이터포털(대기오염정보) -> Android</br>


**2. JSON -> Kotlin Data Class**</br>
plugins 에서 JSON to Kotlin Class 설치</br>
코틀린 데이터 클래스를 생성하기 원하는 패키지 우클릭</br>
New -> Kotlin data class File from Json</br>
서버에서 오는 response 복사 후 필드에 붙여넣기</br>
Advanced -> [Property] val, Nullable 세팅으로 되어 있는지 확인 // [Annotation] Gson 확인</br>


**3. 텍스트뷰에 이미지 포함하기**</br>
텍스트와 이미지가 함께 구성하는 경우가 발생하는데 별도의 ImageView 를 구성하지 않고 TextView 의 속성을 이용하면 쉽게 처리할 수 있음</br>

root layout 태그에 `xmlns:app="http://schemas.android.com/apk/res-auto"` 를 추가</br>
`app:drawableEndCompat` 은 이미지를 오른쪽에 배치</br>
`app:drawableStartCompat` 은 이미지를 왼쪽에 배치</br>
`app:drawablePadding` 은 텍스트와 이미지 사이 간격을 조절</br>


**4. 위젯**</br>

AppWidgetProviderInfo</br>
-앱 위젯에 대한 정보를 담은 XML 파일</br>
-레이아웃 사이즈, 업데이트 주기 등</br>
-res 우클릭 -> Android Resource Directory -> Directory name, Resource type 'xml' -> xml resource file 클릭 후 provider info xml 생성</br>

AppWidgetProvider</br>
-실제로 위젯을 제공하는 클래스</br>
-앱 갱신, 삭제, 활성화, 비활성화 등 이벤트를 핸들링할 수 있음</br>

Layout</br>
-remote view</br>
-사용할 수 있는 컴포넌트의 제약이 있음</br>

위젯 사이즈 추정</br>
셀 개수  |  사용가능한 크기(dp)</br>
(열/행) | (minWidth/minHeight)</br>
================================</br>
1      |  40   dp</br>
2      |  110  dp</br>
3      |  180  dp</br>
n      |  70*n - 30 dp</br>

프로바이더를 리시버로 AndroidManifest 에 등록하는 작업이 필요함



<br></br>
<br></br>

---

><a id = "ref">**참고링크**</a></br>

에어코리아 대기오염 정보</br>
https://www.data.go.kr/data/15073861/openapi.do</br>

에어코리아 측정소 정보</br>
https://www.data.go.kr/data/15073877/openapi.do</br>

에어코리아 데이터 주의 사항</br>
->반드시 자료의 출처 (환경부/한국환경공단)표기 의무를 준수해야함</br>
->자료 오류 및 표출방식에 따라 값이 다를 수 있음을 명시해야함</br>

카카오톡 좌표 변환 api</br>
https://developers.kakao.com/docs/latest/ko/local/dev-guide#trans-coord</br>

aop-part4-chapter06</br>
https://github.com/Fastcampus-Android-Lecture-Project-2021/aop-part4-chapter06</br>

위치 정보 액세스 권한 요청</br>
https://developer.android.com/training/location/permissions</br>

Google Play services document</br>
https://developers.google.com/android/reference/packages</br>

AppWidgetProviderInfo</br>
https://developer.android.com/reference/android/appwidget/AppWidgetProviderInfo</br>

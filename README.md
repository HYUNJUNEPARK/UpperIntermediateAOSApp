# 미세먼지 앱

<img src="이미지 주소" height="400"/>

* <a href = "#ref">참고링크</a>
---

dataBinding / Retrofit2 / coroutine / FusedLocationProviderClient / App Widgets

**API 호출 구조**</br>
Android(경도/위도) -> Kakao developers(TM 좌표 변환) -> 공공데이터포털(측정소명) -> 공공데이터포털(대기오염정보) -> Android</br>


**JSON -> Kotlin Data Class**</br>
1. plugins 에서 JSON to Kotlin Class 설치</br>
2. 코틀린 데이터 클래스를 생성하기 원하는 패키지 우클릭</br>
3. New -> Kotlin data class File from Json</br>
4. 서버에서 오는 response 복사 후 필드에 붙여넣기</br>
5. Advanced -> [Property] val, Nullable 세팅으로 되어 있는지 확인 // [Annotation] Gson 확인</br>

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


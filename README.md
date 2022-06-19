확장 함수
-내가 만든 클래스의 경우 새로운 함수가 필요할 때 쉽게 추가가 가능하지만 외부 라이브러리를 사용할 때는 함수 추가하기가 어렵다
->코틀린에서 제공하는 호가장 함수 기능을 통해 클래스를 확장하여 원하는 새로운 함수들을 만들 수 있다
->기존 클래스를 뼈대로 새로운 함수나 프로퍼티를 붙여서 클래스를 키우는 작업


```kotlin
fun Float.dpToPx(context: Context): Float =
    TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this, context.resources.displayMetrics)
```

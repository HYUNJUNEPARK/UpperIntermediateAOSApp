음원 출처 : ncs.io/music

크롬 우측 상단 더보기 -> 도구 더보기 -> 개발자 도구 -> 왼쪽 상단 'Select an element in the inspect it' -> 원하는 음악을 클릭하면 data-url 을 확인할 수 있음 

ConstraintLayout 은 Group 위젯을 제공함
분산되어 있는 컴포넌트들을 한번에 제어하기 편리해짐
ex)특정 컴포넌트들을 묶어 한번에 visible/invisible 처리
`androidx.constraintlayout.widget.Group`

mapIndexed
iterable 을 대상으로 map 연산 시 원소 뿐 아니라 해당 원소가 갖고 있는 인덱스를 함께 묶어 매핑해주는 함수
two-argument 함수를 인자로 받는 합성 함수

```kotlin
val names = listOf("a", "b")
names.mapIndexed{ index, string ->
    userPairingFun(index, string)
}
//(0, a)
//(1, b)
```
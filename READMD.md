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



GitRepositoryApp://github-auth

# Sk-Sunny
SK-Sunny 프로젝트 

## 누나들을 위한 깃 메뉴얼
---

1. 팀원의 작업 내용이 반영된 main 브랜치의 최신 내용을 가져온다.

```bash
$ git pull origin main (내 브랜치)
```
2. 개발을 한다.

3. 변경된 내용을 add 한다.
```bash
$ git add [내가 저장소에 반영할 내용]
```

4. 내가 작업한 내용을 커밋한다. **그냥 하루종일 계속 개발하다가 커밋하지 말고 했던 업무별로 커밋 나누기**
예) 액티비티 추가, 리팩토링, 리니어레이아웃을 라디오그룹으로 변경

5. 내가 작업한 내용을 원격 저장소에 올린다.

누가 내가 작업 중인 브랜치에서 개발 중일 수 있으므로 **반드시** pull 받고 나서 push.
```
$ git pull origin [작업 중인 브랜치]
$ git push origin [작업 중인 브랜치] # upstream 설정을 했다면 그냥 git push
```
6. 원격 저장소의 내 브랜치로 올린 커밋들을 origin/main에 반영

- 내가 한 내용을 설명하는 제목으로 pull request
- 충돌을 해겷하고 merge, 내가 해결하기 어려운 문제가 있다면 팀원에게 merge 요청


### 그 외에 알아야 할 것들 

브랜치 바꾸기
```bash
$ git checkout [이동하고 싶은 브랜치]
```

## 숙녀분들을 위한 api 사용법
---

android에서 http 통신을 목적으로 사용하는 Volley라는 라이브러리가 있습니다.

JsonObjectRequest 객체로 요청을 만들고
  JsonObjectRequest([HTTP 메소드, [요청을 보낼 url], [요청 body], [응답 리스너], [에러 리스너]) 형태로 구현
  
RequestQueue.add()를 통해 요청을 보낼 수 있습니다.

- api 키와 같이 민감한 정보는 local.properties에 추가하여 외부에 노출시키지 않고 사용할 수 있습니다.

```groovy
# local.properties
api_key=[api 키]
```

- local.properties에 추가한 값을 gradle에서 buildConfigField에 추가

```gradle
// build.gradle(:app)
Properties properties = new Properties()
properties.load(project.rootProject.file('local.properties').newDataInputStream())

defaultConfig{
    buildConfigField "String", "API_KEY", properties["api_key"]
  }
```

- **build 후에** 다음과 같이 사용

```java
final String API_KEY = BuildConfig.API_KEY;
```

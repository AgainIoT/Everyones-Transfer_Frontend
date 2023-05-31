## 테스트 환경
- OS : Windows 10, Ubuntu 22.04
- 기기 : Galaxy Note 10 (API 31 : Android 12.0), Galaxy S21+ (API 30 : Android 10.0)

## Minimum SDK

API 21 : Android 5.0 (Lollipop)

## api 사용법
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

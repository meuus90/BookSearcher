# BookSearcher
Android application for search books with daum API.

  * [소스 코드 링크 GitHub](https://github.com/meuus90/BookSearcher)

    * 빌드 타입을 debug로 빌드 시 제약 없이 빌드 가능. (devDebug, prodDebug)

    * 빌드 타입을 release로 빌드 시 임의의 keystore 생성 후 keystore.properties 내용 변경 필요. (devRelease, prodRelease)


  * Product APK 받기

    * [APK 링크 GitHub](https://github.com/meuus90/BookSearcher/blob/master/BookSearcher-prodRelease-1.0.0.apk)

    * [APK 링크 Google Drive](https://drive.google.com/file/d/1W3Vma3zOEoniKW37YxZlmaXLKA3DX-g6/view?usp=sharing)


  * 실행 영상 YouTube 아래 이미지 클릭

    [![실행 영상](http://img.youtube.com/vi/BalUdEynUrk/0.jpg)](https://youtu.be/BalUdEynUrk?t=0s)


## 목차

- [개발 환경](#개발-환경)
- [프로젝트 구성](#프로젝트-구성)
    - [1. Directory](#1-directory)
    - [2. Architecture Design Pattern and Paging](#2-architecture-design-pattern-and-paging)
    - [3. Dependency Injection](#3-dependency-injection)
    - [4. CI](#4-ci)
- [작업 계획](#작업-계획)
- [License](#license)


## 개발 환경

  * 기본 환경
    * Kotlin : 1.4.10
    * Gradle : 4.0.1
    * JVM target : Java-1.8

  * AndroidX 라이브러리
    * Core 1.3.2
    * Coroutine 1.4.1
    * MultiDex 2.0.1
    * Lifecycle 2.2.0
    * Room 2.3.0-alpha03
    * Paging 3.0.0-alpha09

  * 기타 라이브러리
    * Dagger 2.29.1     // Dependency Injection Tool
    * Retrofit 2.9.0    // REST API Tool
    * OkHttp 4.9.0      // HTTP Client Tool
    * Glide 4.11.0      // Image Loading Tool
    * Timber 4.7.1      // Logging Tool
    * Logger 2.2.0      // Logging Tool


## 프로젝트 구성

### 1. Directory

```
/com/meuus90/booksearcher
├── base            ---------> # base package
│   ├── arch/util        ---------> # architecture util source
│   ├── common/util      ---------> # common util source
│   ├── constant         ---------> # constant source
│   └── view             ---------> # custom view source
│
├── di              ---------> # dependency injection modules
├── model           ---------> # medel sources
│   ├── data/source
│   │   ├── api         ----------> # remote server api
│   │   ├── local       ----------> # local room dao
│   │   └── repository  ----------> # repository source
│   ├── paging      ---------> # paging util
│   └── schema      ---------> # schema data
├── view            ---------> # view sources
│   ├── activity    ---------> # activity source
│   ├── dialog      ---------> # dialog source
│   └── fragment    ---------> # fragment source
├── viewmodel       ---------> # viewmodel sources
└── BookSearcher    ---------> # main application class
```

### 2. Architecture Design Pattern and Paging

  * 아키텍쳐 디자인 패턴은 MVVM 패턴을 적용하였다.

    * 각 컴포넌트들은 필요 시 다른 컴포넌트를 Inject하여 사용하였다. [3. Dependency Injection](#3-dependency-injection)

  * BookListFragment의 RecyclerView에 페이징 기능을 적용하였다.

    * AndroidX Paging 3 라이브러리를 사용하였다.

    * Paging 처리 방식은 'Network Storage -> Local Storage -> Repository -> Adapter'로 구성하였다.


### 3. Dependency Injection

#### 각 컴포넌트들을 모듈화 하여 컴포넌트간 종속성을 제거하였다.
#### 이를 통해 개발 퍼포먼스가 향상되었고 유닛 테스트를 수행하기 쉬워졌으며 코드 재사용성이 늘어났다.

  * Fragment를 각각 모듈화 하였고, Activity도 각각 모듈화하여 사용할 Fragment들을 서브모듈로 등록하였다.
    [MainActivityModule](app/src/main/java/com/meuus90/booksearcher/di/module/activity/MainActivityModule.kt)

  * AppModule에서는 Application Context, 네트워크 API, Room Database 등을 모듈화하였다.
    [AppModule](app/src/main/java/com/meuus90/booksearcher/di/module/AppModule.kt)

  * 생성된 컴포넌트 모듈들은 AppComponent로 등록하여 AppInjector를 통해 Application에 주입하였다.
    [AppComponent](app/src/main/java/com/meuus90/booksearcher/di/component/AppComponent.kt)


### 4. CI

  * Github Actions Workflow를 이용해 테스트 자동화를 등록하였다. [Github Actions](https://github.com/meuus90/BookSearcher/actions)

  * 주요 기능

    * develop branch에서 commit push 완료 시 실행

    * JDK 1.8 테스트 환경 셋업

    * Kotlin linter 체크

    * Android linter 체크

    * Test code Unit test 실시


## 작업 계획
- [x] 프로젝트 세팅
- [x] 스키마 디자인
- [x] Model 세팅
    - [x] Repository 세팅
    - [x] Room 세팅
    - [x] Paging Data 세팅
- [x] ViewModel 세팅
- [x] Unit Test 테스트코드 작성
- [x] UI 디자인
- [x] API 에러 타입 별 대응
- [x] 애니메이션 등 UX 설정
- [x] 디바이스 퍼포먼스 체크
- [x] UI 테스트 및 기타 버그 픽스
- [x] Release


## License

Completely free (MIT)! See [LICENSE.md](LICENSE.md) for more.

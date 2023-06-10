# 2023 캡스톤 "시각장애인을 위한 길안내 및 위험탐지 앱"

## Intro

방치되거나 사라지는 점자 보도 블럭, 흰지팡이가 모든 위험물을 방지할 수 없는 현실적인 문제속에서

안전한 보행을 보장하면서 시각장애인의 흰지팡이를 대체 할 수 있는 스마트 디바이스를 만들 순 없을까? 

거리센서, 카메라인식 을 통해 앱으로 물리적 정보를 전달하면 위험물을 탐지하여 음성 안내를 통해 안전한 보행을 도울 수 있고,

GPS정보와 방위정보를 이용하여 시각장애인이 원하는 목적지 까지의 안전한 경로로 안내해줄 수 있는 앱을 만들고자 하였다.

## Libraries

 - Compose
 - Kotlin.Coroutines.Flow
 - Navigation ( Single Activity )
 - Room
 - DataStore
 - Hilt
 - Retrofit2& OKHttpClient3
 - FusedLocationProviderClient
 - TMapSdk
 - Tensorflow lite
 - TextToSpeech
 - SpeechRecognizer
 - Bluetooth Classic
 - AudioFocusRequest & AudioManager

## Architecture ( Clean + MVVM )

![architecture](https://github.com/2023-DeuCapstone-VisuallyImpairedGloves/Front-App/assets/75519689/45525498-04f8-488c-b937-55a7626c5b04)

## DI grpah

![di_graph](https://github.com/2023-DeuCapstone-VisuallyImpairedGloves/Front-App/assets/75519689/242f3ceb-fd30-4915-bb7f-77e136e77c27)

## Main Point

 > TmapApi로 받은 데이터로 경로 안내 제공

 - JSON 데이터를 DTO로 받아 알맞게 가공한뒤, 현재 위치의 변경 주기에 따라 위치의 이동폭 과 방위정보로 올바른 경로인지 확인 후 올바른 경로가 아닐 경우 경로 재요청

![navigation_algorithm](https://github.com/2023-DeuCapstone-VisuallyImpairedGloves/Front-App/assets/75519689/d1d7f5f5-585c-4d9f-9299-b476b3b29020)

 > 전방의 위험물 인식 후 안내 제공

 - esp32에 부착된 cam module로 bluetooth 통신하여 사진을 전달받아 tensorflow - lite로 학습된 모델로 위험물 식별한뒤 esp32 에 부착된 진동모듈 및 TextToSpeech로 진동 + 음성안내

 > 타 음악플레이어 앱으로 음악을 들으면서 끊기지 않게 해야할 필요 요구

 - AudioFocusRequest, AudioManager, AudioAttribute 로 TextToSpeech 발생시 Audio Focusing을 ducking 처리


## Desired Point

 > SpeechRecognizer 로 음성인식 할 경우 외부 음악플레이어의 pausing 해결 불가

 - SpeechRecognizer가 Service로 백그라운드에서 무한루프로 돌면서 음성 인식 대기를 하도록 구현했기 때문에 외부의 audio가 pausing되는데 그렇지 않게 할수 없었고, 해결 필요함.

 > 사용자의 위치가 부정확한 문제 (gps가 튀는 문제)

 - LocationManager -> FusedLocationProviderClient 로 바꿧음에도 계속적으로 gps가 통통 튀는 문제를 해결하지 못함. -> 보행자도로 좌표를 알수있다면 가까운 좌표로 보정 혹은 다른방법으로 해결 필요함.

## Period

#### 2023.04.25~ 2023.05.30

## Member

> **황진호 ([jowunnal](https://github.com/jowunnal "github link"))** 그외 구현

> **공경일 ([kyungil9](https://github.com/kyungil9 "github link"))** esp모듈과 블루투스간 데이터 송수신 파트와 tensorflow-lite 파트 구현



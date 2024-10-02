### 정리
Flux, Mono와 같은 Publisher의 구현체를 사용해서 리액티브 프로그래밍할 때 처리 흐름
- Publisher를 통해 데이터를 생성하고, subscribe를 호출하는 시점에 Publisher의 데이터는 구독하고 Publisher내부의 schedule가 해당 데이터 스트림을 연산자에 따라서 비동기 논블로킹(콜백 메커니즘)으로 처리한다.
- 이정도만 이해하고 나머지reactor 연산자들은 경험적으로 사용하면서 익히는 것이 좋을 것 같다. 
- 스케줄러는 내부적으로 Schedulers 전략에 따라서 다르겠지만, 어쨌든 ExecutorService를 사용해서 비동기 처리를 한다.
- 스케줄러의 ExecutorService는 내부 계산 로직같은 경우 기본적으로 forkJoinPool을 사용한다.
- 외부 I/O 작업 같은 경우는 다른 전략을 사용하여 비동기/논블로킹으로 동작한다. (논블로킹은 OS 레벨에서 지원해야 한다.)
  - webflux에서 reactor 연산자를 사용해서 외부 I/O 작업을 수행할 때는 netty의 이벤트 기반 모델을 사용한다.
참고: https://d2.naver.com/helloworld/2771091

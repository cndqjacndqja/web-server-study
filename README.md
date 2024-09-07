netty
- DEFAULT_EVENT_LOOP_THREADS: 기본적으로 생성될 이벤트 루프 스레드의 수를 의미한다. 만약 프로퍼티 io.netty.eventLoopThreads가 설정되어 있다면 해당 값이 사용된다. 만약 없으면 가용한 프로세서(코어)의 수에 2를 곱한 값을 사용한다. 단 최소 1이상이어야 한다.
- ThreadPerTaskExecutor: netty에서 제공하는 실행자(Executor)구현체 중 하나로, 기본 동작은 다음과 같다.
  - 제출된 각 작업(task)에 대해 새로운 스레드를 생성한다.
  - 즉, execute() 메서드가 호출될 때마다 새로운 스레드가 생성된다.
  - 용도: 주로 netty의 eventLoopGroup을 초기화할 때 사용된다.
    - 새로운 이벤트 루프(eventLoop)를 생성할 때 이 실행자를 사용해서 각 이벤트 루프에 대한 새 스레드를 만든다.
    - 하지만 작업 수에 비례하여 스레드가 생성되므로 주의가 필요하다.
    - `EventLoopGroup group = new NioEventLoopGroup(4, new ThreadPerTaskExecutor(new DefaultThreadFactory("netty-server")));` -> 예를 들어 이 코드는 4개의 이벤트 루프를 가진 그룹을 생성하여, 각 이벤트 루프는 ThreadPerTaskExecutor에 의해 생성된 별도의 스레드에서 실행된다.

- MultithreadEventExecutorGroup: netty의 중요한 추상 클래스 중 하나로, 여러 개의 이벤트 실행기(EventExecutor)를 관리하는 그룹을 나타낸다. 
  - 여러 개의 이벤트 실행기를 포함하고 관리하고, 각 이벤트 실행기는 별도의 스레드에서 생성된다.
  - 주요 역할은 여러 이벤트 실행기 간의 작업 분배, 이벤트 실행기의 생명주기 관리, 그룹 전체의 종료 처리 등등을 한다.
  - NioEventLoopGroup이 이 클래스의 대표적인 구현체이다. 서버와 클라이언트의 연결 처리, I/O 작업 실행 등에 사용된다.
  
  > 내가 만든 예시에서는 NioEventLoopGroup(1)을 했으니, 이벤트 루프가 1개이고, 이 이벤트 루프는 ThreadPerTaskExecutor에 의해 생성된 별도의 스레드에서 실행된다.
  > 그 이벤트 루프는 하나의 채널에 할당되어 socket통신을 한다.
  > 이벤트 그룹에서 이벤트를 생성할 때, selector를 생성하고, 이벤트 루프를 생성하고, 이벤트 루프에 selector를 등록한다.
![스크린샷 2024-09-07 오후 10.51.06.png](..%2F..%2F..%2F..%2F..%2Fvar%2Ffolders%2F_w%2Fh0_zlkpn045gsrtnz617knfm0000gn%2FT%2FTemporaryItems%2FNSIRD_screencaptureui_vR0Ie1%2F%EC%8A%A4%ED%81%AC%EB%A6%B0%EC%83%B7%202024-09-07%20%EC%98%A4%ED%9B%84%2010.51.06.png)
![스크린샷 2024-09-07 오후 10.51.21.png](..%2F..%2F..%2F..%2F..%2Fvar%2Ffolders%2F_w%2Fh0_zlkpn045gsrtnz617knfm0000gn%2FT%2FTemporaryItems%2FNSIRD_screencaptureui_eMd4Fw%2F%EC%8A%A4%ED%81%AC%EB%A6%B0%EC%83%B7%202024-09-07%20%EC%98%A4%ED%9B%84%2010.51.21.png)


----
다시 정리
> 보통 bossGroup EvnetGroup은 1로 해도 충분하다. 이벤트 루프는 하나의 채널에 할당되어 socket통신을 하는데, 소켓 통신 하는 부분도 OS 자원을 활용해 비동기로 통신하고, 요청 받으면 처리 과정은 어차피 새로운 스레드에 위임하기 때문에 굳이 Selector -> channel -> 스레드로 위임 이 과정을 멀티 스레드로 처리할 필요는 없다. 블로킹 되는 부분도 없고,,
> bossGroup의 이벤트 루프가 클라이언트 요청 받으면, NioSocketChannel을 생성하고, workerGroup에 등록한다. workerGroup은 이벤트 루프가 코어 개수 * 20개이고(난 디폴트로 설정했고, 내 노트북 CPU 코어 개수가 10개이니,,), 이 이벤트 루프는 ThreadPerTaskExecutor에 의해 생성된 별도의 스레드에서 실행된다.
> 

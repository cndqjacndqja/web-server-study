import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

public class ReactorCoreDebugExample {
    public static void main(String[] args) throws InterruptedException {
        // 1. 중단점: Flux 생성
        Flux<Integer> flux = Flux.range(1, 5)
            .map(i -> {
                // 2. 중단점: map 연산
                System.out.println("Mapping " + i + " on thread " + Thread.currentThread().getName());
                return i * 2;
            })
            .subscribeOn(Schedulers.parallel());

        // 3. 중단점: 구독
        flux.subscribe(
            value -> {
                // 4. 중단점: onNext
                System.out.println("Received " + value + " on thread " + Thread.currentThread().getName());
            },
            error -> {
                // 5. 중단점: onError
                System.err.println("Error: " + error);
            },
            () -> {
                // 6. 중단점: onComplete
                System.out.println("Completed");
            }
        );

        // 비동기 작업이 완료될 때까지 대기
        Thread.sleep(1000);
    }
}

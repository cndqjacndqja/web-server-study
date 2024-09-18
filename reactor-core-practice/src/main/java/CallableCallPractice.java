import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class CallableCallPractice {
    public static void main(String[] args) {
        // ExecutorService 생성
        ExecutorService executor = Executors.newFixedThreadPool(2);

        // Callable 작업 정의
        Callable<Integer> task = () -> {
            Thread.sleep(2000); // 2초 대기
            return 123; // 결과 반환
        };

        // Callable 작업을 비동기로 제출
        Future<Integer> future = executor.submit(task);

        // 비동기 작업이 완료될 때까지 기다림
        try {
            System.out.println("Result: " + future.isDone());
            Integer result = future.get(); // 작업이 완료될 때까지 블로킹
            System.out.println("Result: " + result);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        } finally {
            // ExecutorService 종료
            executor.shutdown();
        }
    }
}

/**
 * Callable 함수형 인터페이스를 사용하여 ExecutorService를 통해 비동기 작업을 수행하고, Future타입을 반환받는다.
 * Future타입에서 해당 로직이 완료되었는지 여부를 가지고 있고, 이를 통해 비동기/논블로킹을 구현할 수 있다.
 * 하지만 스레드 풀 입장에서는 해당 스레드는 작업이 완료될때까지 블로킹되어 있기 때문에, 스레드 자원 자체를 효율적으로 사용하는건 아니라 생각한다.
 * Reactor에서는 이러한 문제를 해결하기 위해 Mono, Flux를 제공한다. -> 이제 reactor-core에서는 어떻게 해결했는지 확인해보자.
 * */

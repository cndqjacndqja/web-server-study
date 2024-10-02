import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class CompletableFutureExample {
    public static void main(String[] args) {
        // 비동기 작업 1: 숫자를 가져옴
        CompletableFuture<Integer> future1 = CompletableFuture.supplyAsync(() -> {
            sleep(1);
            return 10;
        });

        // 비동기 작업 2: 다른 숫자를 가져옴
        CompletableFuture<Integer> future2 = CompletableFuture.supplyAsync(() -> {
            sleep(2);
            return 20;
        });

        // 두 작업의 결과를 조합
        CompletableFuture<Integer> combinedFuture = future1
            .thenCombine(future2, (result1, result2) -> result1 + result2)
            .thenApply(sum -> sum * 2)
            .exceptionally(ex -> {
                System.out.println("An error occurred: " + ex.getMessage());
                return 0;
            });

        // 최종 결과 처리
        combinedFuture.thenAccept(result ->
            System.out.println("Final result: " + result)
        );

        // 모든 작업이 완료될 때까지 대기
        CompletableFuture.allOf(combinedFuture).join();

        System.out.println("All tasks completed.");
    }

    private static void sleep(int seconds) {
        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

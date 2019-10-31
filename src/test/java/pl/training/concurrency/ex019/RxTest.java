package pl.training.concurrency.ex019;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.TestObserver;
import org.junit.After;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.junit.Assert.assertThat;

public class RxTest {

    private Disposable disposable;
    private final List<String> results = new ArrayList<>();
    private final Observable<String> letters = Observable.fromIterable(List.of("A", "B", "C", "D", "E"));
    private final Observable<Integer> numbers = Observable.range(1, Integer.MAX_VALUE);
    private final Observable<String> observable = letters.zipWith(numbers, (letter, number) -> letter + "-" + number);

    @Test
    public void rxTest() {
        disposable = observable.subscribe(results::add);
        assertThat(results, hasItems("A-1", "B-2", "C-3", "D-4", "E-5"));
    }

    @Test
    public void rx2Test() {
        TestObserver<String> testObserver = new TestObserver<>();
        observable.subscribe(testObserver);
        //testObserver.assertError(IOException.class);
        testObserver.assertComplete();
    }

    @After
    public void destroy() {
        if (disposable != null) {
            disposable.dispose();
        }
    }

}

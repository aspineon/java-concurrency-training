package pl.training.concurrency.ex018;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.PublishSubject;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Application {

    public static void main(String[] args) throws InterruptedException {
        CompositeDisposable compositeDisposable = new CompositeDisposable();

        compositeDisposable.add(Observable.just("Java", "PHP", "JavaScript", "JavaActionScript")
                .map(String::toLowerCase)
                .filter(text -> text.contains("java"))
                .buffer(2)
                .subscribe(System.out::println));

        compositeDisposable.add(Observable.fromCallable(new Multiply(2, 8))
                .subscribe(System.out::println));

        Observable<Integer> observable = Observable.create(emitter -> {
            Random random = new Random();
            for (int index = 0; index < 5; index++) {
                System.out.println("Generating value " + Thread.currentThread().getName());
                emitter.onNext(random.nextInt(101) + 1);
                Thread.sleep(500);
            }
            emitter.onComplete();
        });

        compositeDisposable.add(observable.subscribe(System.out::println));

        PublishSubject<Integer> publishSubject = PublishSubject.create();

        compositeDisposable.add(publishSubject.subscribe(System.out::println));
        publishSubject.onNext(-1);

        BehaviorSubject<Integer> behaviorSubject = BehaviorSubject.create();
        behaviorSubject.onNext(-1);
        behaviorSubject.onNext(-2);
        compositeDisposable.add(behaviorSubject.subscribe(System.out::println));

        ExecutorService executorService = Executors.newFixedThreadPool(1);
        compositeDisposable.add(observable
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread())
                .subscribe(value -> System.out.println("#### Value: " + value + " " + Thread.currentThread().getName())));

        Thread.sleep(5_000);
        compositeDisposable.dispose();
        executorService.shutdownNow();
    }

}

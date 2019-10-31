package pl.training.concurrency.ex017;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Data
public class Product {

    @NonNull
    private volatile long price;

    public void increasePrice(long changeValue) {
        price += changeValue;
    }

}

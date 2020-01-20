package pl.training.concurrency.ex020;

public class Date {

    private int year;
    private int month;
    private volatile int day;

    public void uodate(int day, int month, int year) {
        this.day = day;
        this.month = month;
        this.year = year;
    }

    @Override
    public String toString() {
        return "Date{" +
                "year=" + year +
                ", month=" + month +
                ", day=" + day +
                '}';
    }

    /*
        - zmiana wrtości zmiennej days, wymusza/powoduje zaktualizowanie stanu zmiennych
        month i year w pamięci głównej - RAM
        - jeżli czytamu zmienną day to będzie ona odczytana z pamięci głównej, ale jednocześnie
        - wymuszamy odczytanie/synchronizację zmiennych month i year
        zapis/odczyt do zmiennych nie może być przeniesiony tak aby następował po zapisie do
        zmiennej volatile jeśli wcześniej występował przed nim (co nie wyklucza sytuacji odwrotnej)
        - zapis/odczyt do zmiennych nie może być przeniesiony tak aby następował przed odczytem zmiennej
        volatile jeśli wcześniej występował po nim (co nie wyklucza sytuacji odwrotnej)
     */

}

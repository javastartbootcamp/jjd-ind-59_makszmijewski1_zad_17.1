package pl.javastart.streamsexercise;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.time.ZonedDateTime;
import java.time.chrono.ChronoZonedDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class PaymentService {

    private PaymentRepository paymentRepository;
    private DateTimeProvider dateTimeProvider;

    PaymentService(PaymentRepository paymentRepository, DateTimeProvider dateTimeProvider) {
        this.paymentRepository = paymentRepository;
        this.dateTimeProvider = dateTimeProvider;
    }

    /*
    Znajdź i zwróć płatności posortowane po dacie malejąco
     */
    List<Payment> findPaymentsSortedByDateDesc() {
        return paymentRepository
                .findAll()
                .stream()
                .sorted()
                .toList();
    }

    /*
    Znajdź i zwróć płatności dla aktualnego miesiąca
     */
    List<Payment> findPaymentsForCurrentMonth() {
        return findPaymentsForGivenMonth(YearMonth.now());
    }

    /*
    Znajdź i zwróć płatności dla wskazanego miesiącca
     */
    List<Payment> findPaymentsForGivenMonth(YearMonth yearMonth) {
        return paymentRepository
                .findAll()
                .stream()
                .filter(payment -> YearMonth.from(payment.getPaymentDate()).equals(dateTimeProvider.yearMonthNow()))
                .toList();
    }

    /*
    Znajdź i zwróć płatności dla ostatnich X dzni
     */
    List<Payment> findPaymentsForGivenLastDays(int days) {
        return paymentRepository
                .findAll()
                .stream()
                .filter(payment -> payment.getPaymentDate().isAfter(dateTimeProvider.zonedDateTimeNow().minusDays(days)))
                .collect(Collectors.toList());
    }

    /*
    Znajdź i zwróć płatności z jednym elementem
     */
    Set<Payment> findPaymentsWithOnePaymentItem() {
        return paymentRepository
                .findAll()
                .stream()
                .filter(payment -> payment.getPaymentItems().size() == 1)
                .collect(Collectors.toSet());
    }

    /*
    Znajdź i zwróć nazwy produktów sprzedanych w aktualnym miesiącu
     */
    Set<String> findProductsSoldInCurrentMonth() {
        return findPaymentsForCurrentMonth()
                .stream()
                .map(Payment::getPaymentItems)
                .flatMap(List::stream)
                .map(PaymentItem::getName)
                .collect(Collectors.toSet());
    }

    /*
    Policz i zwróć sumę sprzedaży dla wskazanego miesiąca
     */
    BigDecimal sumTotalForGivenMonth(YearMonth yearMonth) {
        Integer sum = findPaymentsForGivenMonth(yearMonth)
                .stream()
                .map(Payment::countFinalPrice)
                .reduce(0, Integer::sum);

        return BigDecimal.valueOf(sum);
    }

    /*
    Policz i zwróć sumę przeyznanaych rabatów dla wskazanego miesiąca
     */
    BigDecimal sumDiscountForGivenMonth(YearMonth yearMonth) {
        return findPaymentsForGivenMonth(yearMonth)
                .stream()
                .map(Payment::countDiscount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /*
    Znajdź i zwróć płatności dla użytkownika z podanym mailem
     */
    List<PaymentItem> getPaymentsForUserWithEmail(String userEmail) {
        return paymentRepository
                .findAll()
                .stream()
                .filter(payment -> payment.getUser().getEmail().equals(userEmail))
                .map(Payment::getPaymentItems)
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    /*
    Znajdź i zwróć płatności, których wartość przekracza wskazaną granicę
     */
    Set<Payment> findPaymentsWithValueOver(int value) {
        return paymentRepository
                .findAll()
                .stream()
                .filter(payment -> payment.countFinalPrice() > value)
                .collect(Collectors.toSet());
    }

}

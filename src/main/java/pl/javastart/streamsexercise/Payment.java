package pl.javastart.streamsexercise;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;

public class Payment implements Comparable<Payment> {

    private User user;
    private ZonedDateTime paymentDate;
    private List<PaymentItem> paymentItems;

    public Payment(User user, ZonedDateTime paymentDate, List<PaymentItem> paymentItems) {
        this.user = user;
        this.paymentDate = paymentDate;
        this.paymentItems = paymentItems;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ZonedDateTime getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(ZonedDateTime paymentDate) {
        this.paymentDate = paymentDate;
    }

    public List<PaymentItem> getPaymentItems() {
        return paymentItems;
    }

    public void setPaymentItems(List<PaymentItem> paymentItems) {
        this.paymentItems = paymentItems;
    }

    @Override
    public int compareTo(Payment o) {
        return -paymentDate.compareTo(o.paymentDate);
    }

    public int countFinalPrice() {
        BigDecimal finalPrice = BigDecimal.ZERO;
        for (PaymentItem paymentItem : paymentItems) {
            finalPrice = finalPrice.add(paymentItem.getFinalPrice());
        }
        return finalPrice.intValue();
    }

    public BigDecimal countDiscount() {
        BigDecimal discount = BigDecimal.ZERO;
        for (PaymentItem paymentItem : paymentItems) {
            BigDecimal difference = paymentItem.getRegularPrice().subtract(paymentItem.getFinalPrice());
            discount = discount.add(difference);
        }
        return discount;
    }
}

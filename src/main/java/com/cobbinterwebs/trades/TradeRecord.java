package com.cobbinterwebs.trades;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.text.StringTokenizer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.cobbinterwebs.locale.DisplayKeys;

/**
 * Data for a single trade.
 */
public abstract class TradeRecord implements Comparable<TradeRecord>, ITradeRecord {
    private static final Logger log = LogManager.getLogger("com.cobbinterwebs.trades.TradeRecord");

    protected MathContext mathCtx = new MathContext(Integer.MAX_VALUE, RoundingMode.HALF_UP);
    
    protected String timeStr;
    protected BigDecimal price = BigDecimal.ZERO;
    protected BigDecimal size = BigDecimal.ZERO;
    protected BigDecimal bid = BigDecimal.ZERO;
    protected BigDecimal ask = BigDecimal.ZERO;
    protected Boolean tTrade = Boolean.FALSE;
    
    protected List<String> rawTokens = new ArrayList<>();

    public TradeRecord(String pData, char delim) {
        log.trace(DisplayKeys.get(DisplayKeys.LOG_PARSING), pData);
        StringTokenizer strtok = new StringTokenizer(pData,delim);
        while(strtok.hasNext()) {
            rawTokens.add(strtok.next().replaceAll("\"",""));
        }
    }


    /**
     * Needed for unit tests.
     * @param pTimeString the time
     * @param pPrice the price
     * @param pSize the size
     * @param pBid the bid
     * @param pAsk the ask
     */
    @SuppressWarnings("unused")
    public TradeRecord(String pTimeString, BigDecimal pPrice, BigDecimal pSize, BigDecimal pBid, BigDecimal pAsk) {
        this.timeStr = pTimeString;
        this.price = pPrice;
        this.size = pSize;
        this.bid = pBid;
        this.ask = pAsk;
    }
    
    /**
     * Using the bid and the ask price this function calculates
     * a sentiment. If the trade is executed between the bid/ask then
     * the sentiment is unknown. However if the trade is executed at the
     * bid or at the ask then the trade is reported as a sell or a buy, respectively.
     * @return enum BuySell
     */
    @Override
    public BuySell sentiment() {
        if(bid.equals(BigDecimal.ZERO) && ask.equals(BigDecimal.ZERO)) {
            return BuySell.UNKNOWN;
        }

        if(bid.equals(ask)) {
            return BuySell.UNKNOWN;
        }

        if(price.compareTo(bid) <= 0) {
            return BuySell.SELL;
        }
        
        if(price.compareTo(ask) >= 0) {
            return BuySell.BUY;
        }
        
        return BuySell.UNKNOWN;
    }

    @Override
    public BigDecimal getDollarVolume() {
        return this.price.multiply(this.size);
    }

    @Override
    public BigDecimal getPrice() {
        return price;
    }

    @Override
    public BigDecimal getSize() {
        return size;
    }

    @Override
    public Boolean isTeeTrade() {
        return tTrade;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TradeRecord that = (TradeRecord) o;
        return timeStr.equals(that.timeStr);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    /**
     * Compares this object with the specified object for order.  Returns a
     * negative integer, zero, or a positive integer as this object is less
     * than, equal to, or greater than the specified object.
     *
     * <p>The implementor must ensure
     * {@code sgn(x.compareTo(y)) == -sgn(y.compareTo(x))}
     * for all {@code x} and {@code y}.  (This
     * implies that {@code x.compareTo(y)} must throw an exception iff
     * {@code y.compareTo(x)} throws an exception.)
     *
     * <p>The implementor must also ensure that the relation is transitive:
     * {@code (x.compareTo(y) > 0 && y.compareTo(z) > 0)} implies
     * {@code x.compareTo(z) > 0}.
     *
     * <p>Finally, the implementor must ensure that {@code x.compareTo(y)==0}
     * implies that {@code sgn(x.compareTo(z)) == sgn(y.compareTo(z))}, for
     * all {@code z}.
     *
     * <p>It is strongly recommended, but <i>not</i> strictly required that
     * {@code (x.compareTo(y)==0) == (x.equals(y))}.  Generally speaking, any
     * class that implements the {@code Comparable} interface and violates
     * this condition should clearly indicate this fact.  The recommended
     * language is "Note: this class has a natural ordering that is
     * inconsistent with equals."
     *
     * <p>In the foregoing description, the notation
     * {@code sgn(}<i>expression</i>{@code )} designates the mathematical
     * <i>signum</i> function, which is defined to return one of {@code -1},
     * {@code 0}, or {@code 1} according to whether the value of
     * <i>expression</i> is negative, zero, or positive, respectively.
     *
     * @param o the object to be compared.
     * @return a negative integer, zero, or a positive integer as this object
     * is less than, equal to, or greater than the specified object.
     * @throws NullPointerException if the specified object is null
     * @throws ClassCastException   if the specified object's type prevents it
     *                              from being compared to this object.
     */
    public int compareTo(TradeRecord o) {
        if(o.timeStr == null || o.timeStr.trim().length() == 0) {
            throw new NullPointerException("argument cannot be null.");
        }
        return this.timeStr.compareTo(o.timeStr);
    }

    @Override
    public String toString() {
        return timeStr + "," +
                sentiment() + "," +
                price + "," +
                bid + "," +
                ask + "," +
                getDollarVolume();
    }
}

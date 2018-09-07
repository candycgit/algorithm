package cc.leetcode;

public class Solution29 {

    public int divide(int dividend, int divisor) {
        if (divisor == 0 || (divisor == -1 && dividend == Integer.MIN_VALUE)) {
            return Integer.MAX_VALUE;
        }
        if (Math.abs(divisor) == 1) {
            return (divisor > 0) ? dividend : -dividend;
        }

        boolean neg = (dividend < 0 && divisor > 0) || (dividend > 0 && divisor < 0);
        if (neg) {
            divisor = (divisor > 0) ? -divisor : divisor;
            dividend = (dividend > 0) ? -dividend : dividend;
        }

        int count = 0;
        int quotient = 0;
        int acc = divisor;
        int c = 1;
        while (allowToAdd(quotient, divisor, dividend)) {
            while (allowToAdd(quotient, acc, dividend)) {
                count += c;
                quotient += acc;
                if (!overflow(acc, acc << 1)) {
                    acc = acc << 1;
                    c = c << 1;
                }
            }
            // prepare to the next iteration:
            if (acc != divisor) {
                acc = acc >> 1;
                c = c >> 1;
            }
        }

        return (neg) ? -count : count;
    }

    private boolean allowToAdd(int a, int b, int limit) {
        int r = a + b;
        if (overflow(a, b, r)) {
            return false;
        }
        return (limit > 0) ? (r <= limit) : (r >= limit);
    }

    private boolean overflow(int a, int r) {
        return (((a & ~r) | (~a & r)) < 0);
    }

    private boolean overflow(int a, int b, int r) {
        return (((a & b & ~r) | (~a & ~b & r)) < 0);
    }

}

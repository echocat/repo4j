package org.echocat.repo4j.util;

import org.echocat.repo4j.range.Range;

import javax.annotation.Nonnull;
import java.util.Random;

public class RandomUtils {

    private static final double DOUBLE_UNIT = 0x1.0p-53;  // 1.0  / (1L << 53)
    private static final float FLOAT_UNIT = 0x1.0p-24f; // 1.0f / (1 << 24)

    public static short nextShortFor(@Nonnull Random random, @Nonnull Range<Short> range) {
        return (short) nextIntegerFor(random, range.from().orElse(Short.MIN_VALUE), range.to().orElse(Short.MAX_VALUE));
    }

    public static int nextIntegerFor(@Nonnull Random random, @Nonnull Range<Integer> range) {
        return nextIntegerFor(random, range.from().orElse(Integer.MIN_VALUE), range.to().orElse(Integer.MAX_VALUE));
    }

    public static long nextLongFor(@Nonnull Random random, @Nonnull Range<Long> range) {
        return nextLongFor(random, range.from().orElse(Long.MIN_VALUE), range.to().orElse(Long.MAX_VALUE));
    }

    public static float nextFloatFor(@Nonnull Random random, @Nonnull Range<Float> range) {
        return nextFloatFor(random, range.from().orElse(Float.MIN_VALUE), range.to().orElse(Float.MAX_VALUE));
    }

    public static double nextDoubleFor(@Nonnull Random random, @Nonnull Range<Double> range) {
        return nextDoubleFor(random, range.from().orElse(Double.MIN_VALUE), range.to().orElse(Double.MAX_VALUE));
    }

    static int nextIntegerFor(@Nonnull Random random, int origin, int bound) {
        int r = random.nextInt();
        if (origin < bound) {
            final int n = bound - origin;
            final int m = n - 1;
            if ((n & m) == 0) {
                r = (r & m) + origin;
            } else if (n > 0) {
                //noinspection StatementWithEmptyBody
                for (int u = r >>> 1; u + m - (r = u % n) < 0; u = random.nextInt() >>> 1) { }
                r += origin;
            } else {
                while (r < origin || r >= bound) {
                    r = random.nextInt();
                }
            }
        }
        return r;
    }

    static long nextLongFor(@Nonnull Random random, long origin, long bound) {
        long r = random.nextLong();
        if (origin < bound) {
            final long n = bound - origin;
            final long m = n - 1;
            if ((n & m) == 0L) {
                r = (r & m) + origin;
            } else if (n > 0L) {
                //noinspection StatementWithEmptyBody
                for (long u = r >>> 1; u + m - (r = u % n) < 0L; u = random.nextLong() >>> 1) {}
                r += origin;
            } else {
                while (r < origin || r >= bound) {
                    r = random.nextLong();
                }
            }
        }
        return r;
    }

    static float nextFloatFor(@Nonnull Random random, float origin, float bound) {
        float r = (random.nextInt() >>> 11) * FLOAT_UNIT;
        if (origin < bound) {
            r = r * (bound - origin) + origin;
            if (r >= bound) {
                r = Float.intBitsToFloat(Float.floatToIntBits(bound) - 1);
            }
        }
        return r;
    }


    static double nextDoubleFor(@Nonnull Random random, double origin, double bound) {
        double r = (random.nextLong() >>> 11) * DOUBLE_UNIT;
        if (origin < bound) {
            r = r * (bound - origin) + origin;
            if (r >= bound) {
                r = Double.longBitsToDouble(Double.doubleToLongBits(bound) - 1);
            }
        }
        return r;
    }

}

package hzg.wpn;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * @author Igor Khokhriakov <igor.khokhriakov@hzg.de>
 * @since 24.11.2015
 */
public class UnsafeSupport {
    public static final Unsafe UNSAFE;
    static {
        try {
            Field f = Unsafe.class.getDeclaredField("theUnsafe"); //Internal reference
            f.setAccessible(true);
            UNSAFE = (Unsafe) f.get(null);
        } catch (NoSuchFieldException|IllegalAccessException e) {
            throw new AssertionError("Should not happen!", e);
        }
    }
}

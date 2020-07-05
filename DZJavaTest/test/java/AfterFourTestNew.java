import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class AfterFourTestNew {
    private static AfterFour afterFour;

    @BeforeClass
    public static void init() {
        afterFour = new AfterFour();
    }

    @Test
    public void testAfterFour1() {
        Assert.assertArrayEquals(afterFour.afterLastFour(
                new int[] {1, 2, 3, 4, 5, 6, 7, 8, 9}),
                new int[] {5, 6, 7, 8, 9});
    }
    @Test
    public void testAfterFour2() {
        Assert.assertArrayEquals(afterFour.afterLastFour(
                new int[] {4, 21, -3, 41, 5, -4, 4, -58, 29}),
                new int[] {-58, 29});
    }
    @Test
    public void testAfterFour3() {
        Assert.assertArrayEquals(afterFour.afterLastFour(
                new int[] {4, 4, 4}),
                new int[] {});
    }
    @Test(expected = RuntimeException.class)
    public void testAfterFourException() {
        Assert.assertArrayEquals(afterFour.afterLastFour(new int[] {1, 2, 3, 5, 6, 7, 8, 9}), new int[] {});
    }
}


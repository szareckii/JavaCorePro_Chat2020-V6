import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class AfterFourTest {
    private int[] oldArr;
    private int[] newArr;
    AfterFour afterFour;

    public AfterFourTest(int[] oldArr, int[] newArr) {
        this.oldArr = oldArr;
        this.newArr = newArr;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {new int[] {1, 2, 3, 4, 5, 6, 7, 8, 9}, new int[] {5, 6, 7, 8, 9}},
                {new int[] {4, 21, -3, 41, 5, -4, 4, -58, 29}, new int[] {-58, 29}},
//                {new int[] {1, 2, 3, 5, 6, 7, 8, 9}, new int[] {}},
                {new int[] {4, 4, 4}, new int[] {}},
        });
    }

    @Before
    public void init() {
        afterFour = new AfterFour();
    }

    @Test
    public void testAfterFour() {
        Assert.assertArrayEquals(afterFour.afterLastFour(oldArr), newArr);
    }

    @Test(expected = RuntimeException.class)
    public void testAfterFourException() {
        Assert.assertArrayEquals(afterFour.afterLastFour(new int[] {1, 2, 3, 5, 6, 7, 8, 9}), new int[] {});
    }



}

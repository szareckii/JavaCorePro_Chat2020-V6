import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class OneAndFourTest {
    private int[] arr;
    private boolean check;
    OneAndFour oneAndFour;

    public OneAndFourTest(int[] arr, boolean check) {
        this.arr = arr;
        this.check = check;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {new int[]{1, 1, 1, 4, 4, 1, 4, 4}, true},
                {new int[]{1, 1, 1, 1, 1, 1}, false},
                {new int[]{4, 4, 4, 4}, false},
                {new int[]{1, 4, 4, 1, 1, 4, 3}, false},
        });
    }

    @Before
    public void init() {
        oneAndFour = new OneAndFour();
    }

    @Test
    public void testCheckOneAndFour() {
        Assert.assertEquals(oneAndFour.oneAndFourInArr(arr), check);
    }

}
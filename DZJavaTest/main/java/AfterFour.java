public class AfterFour {

    public static void main(String[] args) {
//        int[] arr1 = {1, 2, 3, 4, 5, 6, 7, 8, 9};
//        int[] arr2 = {4, 21, -3, 41, 5, -4, 4, -58, 29};
//        int[] arr3 = {1, 2, 3, 5, 6, 7, 8, 9};
//        int[] arr4 = {4, 4, 4};
//
//        int[] newArr1 = afterLastFour(arr1);
//        int[] newArr2 = afterLastFour(arr2);
//        int[] newArr3 = afterLastFour(arr3);
//        int[] newArr4 = afterLastFour(arr4);

    }

    public static int[] afterLastFour(int[] arr) throws RuntimeException {
        int indexLastFour = -1;
        for (int i = arr.length - 1; i >= 0; i--) {
            if (arr[i] == 4) {
                indexLastFour = i + 1;
                break;
            }
        }

        if (indexLastFour != -1) {
            int newLenght = arr.length - indexLastFour;
            int[] newArr = new int[newLenght];
            System.arraycopy(arr, indexLastFour, newArr, 0, newLenght);
            return newArr;
        } else {
            throw new RuntimeException("В исходном массиве нет цифры 4");
        }
    }
}

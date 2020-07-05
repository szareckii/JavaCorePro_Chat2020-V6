public class OneAndFour {
    public static void main(String[] args) {
//        int[] arr1 = {1, 1, 1, 4, 4, 1, 4, 4};
//        int[] arr2 = {1, 1, 1, 1, 1, 1};
//        int[] arr3 = {4, 4, 4, 4};
//        int[] arr4 = {1, 4, 4, 1, 1, 4, 3};
//
//        System.out.println(oneAndFourInArr(arr1));
//        System.out.println(oneAndFourInArr(arr2));
//        System.out.println(oneAndFourInArr(arr3));
//        System.out.println(oneAndFourInArr(arr4));
    }

    public static boolean oneAndFourInArr(int[] arr) {
        boolean check = true;
        int sum = 0;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] != 1 && arr[i] != 4) {
                check = false;
                break;
            }
            sum += arr[i];
        }
        if (sum == arr.length || sum ==arr.length * 4) {
            check = false;
        }

        return check;
    }
}

import java.util.Random;

public class Main {
	static long quick = 0;
	static long quickThread = 0;
	
	public static void main(String[] args) throws InterruptedException {
		
		int largo = 10000;
		int max = 100;
		int N = 10;
		long startQuickThread,endQuickThread,startQuick,endQuick;
		for(int i=0;i<N;i++){
			Integer[] list = generateRandomArray(largo, max);
			Integer[] list1 = new Integer[largo];
			for(int j=0;j<largo;j++) list1[j] = list[j];
			startQuickThread = System.nanoTime();
			quickSortThread(list, 0, largo - 1);
			endQuickThread = System.nanoTime();
			quickThread += endQuickThread-startQuickThread;
			startQuick = System.nanoTime();
			quickSort(list1, 0, largo - 1);
			endQuick = System.nanoTime();
			quick += endQuick-startQuick;
		}
		
		System.out.println("QuickSort with threads: "+quickThread);
		System.out.println("QuickSort without threads: "+quick);
	}
	
	public static Integer[] generateRandomArray(final Integer largo, final Integer max) {
		Integer[] list = new Integer[largo];
		for(int i = 0; i < largo; i++) {
			Random randomGenerator = new Random();
			list[i] =  randomGenerator.nextInt(max);
		}
		return list;
	}
	
	public static void printAnArray(Integer[] array) {
		int largo = array.length;
		System.out.print("[");
		for(int i = 0; i < largo - 1; i++) {
			System.out.print(array[i] +", ");
		}
		System.out.print(array[largo - 1]);
		System.out.print("]\n");
	}
	
	public static void quickSort(final Integer[] array, final int left, final int right) throws InterruptedException {
		int pivot = array[left];
		int i = left;
		int j = right;
		int aux;
		
		while(i < j) {
			while(pivot >= array[i] && i < j)
				i++;
			while(array[j] > pivot)
				j--;
			if(i < j) {
				aux = array[i];
				array[i] = array[j];
				array[j] = aux;
			}
		}
		array[left] = array[j];
		array[j] = pivot;
		
		final int leftLimit = j - 1;
		final int rightLimit = j + 1;
		if(left < leftLimit) quickSort(array, left, leftLimit);
		if(rightLimit < right) quickSort(array, rightLimit, right);
	}
	
	public static void quickSortThread(final Integer[] array, final int left, final int right) throws InterruptedException {
		int pivot = array[left];
		int i = left;
		int j = right;
		int aux;
		
		while(i < j) {
			while(pivot >= array[i] && i < j)
				i++;
			while(array[j] > pivot)
				j--;
			if(i < j) {
				aux = array[i];
				array[i] = array[j];
				array[j] = aux;
			}
		}
		array[left] = array[j];
		array[j] = pivot;
		//si esto no se hace, no se pueden usar dentro de los Threads
		final int leftLimit = j - 1;
		final int rightLimit = j + 1;
		if(left < leftLimit) {
			Thread leftBoucle = new Thread() {
				public void run() {
					try {
						quickSortThread(array, left, leftLimit);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			};
			leftBoucle.start();
			leftBoucle.join();
		}
		if(rightLimit < right) {
			Thread rightBoucle = new Thread() {
				public void run() {
					try {
						quickSortThread(array, rightLimit, right);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			};
			rightBoucle.start();
			rightBoucle.join();
		}	
	}

}

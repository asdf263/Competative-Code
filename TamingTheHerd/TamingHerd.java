import java.io.*;
import java.util.*;
import java.util.function.Consumer;

public class TamingHerd {
    static class InputReader {
        BufferedReader reader;
        StringTokenizer tokenizer;

        public InputReader(InputStream stream) {
            reader = new BufferedReader(new InputStreamReader(stream), 32768);
            tokenizer = null;
        }

        public InputReader(FileReader fr) {
            reader = new BufferedReader(fr);
            tokenizer = null;
        }

        String next() { // reads in the next string
            while (tokenizer == null || !tokenizer.hasMoreTokens()) {
                try {
                    tokenizer = new StringTokenizer(reader.readLine());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            return tokenizer.nextToken();
        }

        public int nextInt() { // reads in the next int
            return Integer.parseInt(next());

        }

        public long nextLong() { // reads in the next long
            return Long.parseLong(next());
        }

        public double nextDouble() { // reads in the next double
            return Double.parseDouble(next());
        }

        public Character nextChar() { // reads in the next double
            return next().charAt(0);
        }
    }

    static class CombinationIterator<T> implements Iterable<List<T>>, Iterator<List<T>> {
        protected List<T> items;
        protected int choose;
        protected boolean finished;
        protected int[] current;
        protected int[] previous;

        public CombinationIterator(List<T> items, int choose) {
            if (items == null || items.size() == 0) {
                throw new IllegalArgumentException("items");
            }
            if (choose <= 0 || choose > items.size()) {
                throw new IllegalArgumentException("choose");
            }
            this.items = items;
            this.choose = choose;
            this.finished = false;
        }

        public Iterator<List<T>> iterator() {
            return this;
        }

        public boolean hasNext() {
            return !finished;
        }

        public List<T> next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            if (current == null) {
                current = new int[choose];
                previous = new int[choose];
                for (int i = 0; i < choose; i++) {
                    current[i] = i;
                    previous[i] = i;
                }
            } else {
                for (int i = 0; i < choose; i++) {
                    previous[i] = current[i];
                }           
            }


            List<T> result = itemsFromIndices(current);

            int n = items.size();
            finished = true;
            for (int i = choose - 1; i >= 0; i--) {
                if (current[i] < n - choose + i) {
                    current[i]++;
                    for (int j = i + 1; j < choose; j++) {
                        current[j] = current[i] - i + j;
                    }
                    finished = false;
                    break;
                }
            }

            return result;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        // Permuation related
        public int[] previousIndices() {
            return this.previous;
        }
        public List<T> itemsFromIndices(int[] indices){
            List<T> result = new ArrayList<T>(choose);
            for (int i = 0; i < choose; i++) {
                result.add(items.get(indices[i]));
            }
            return result;
        }
        public void generatePermutations(int[] arr, int k, Consumer<int[]> callBack){
            if(k == 1){
                callBack.accept(arr); // check the current permutation for validity
            } else {
                generatePermutations(arr, k-1, callBack);
                for(int i = 0; i < k-1; i++){
                    swap(arr, k % 2 == 0 ? i : 0, k-1);
                    generatePermutations(arr, k-1, callBack);
                }
            }
        }
        public void LogItems(List<T> items, String lead) {
            System.out.print(lead);
            System.out.print("[");
            for(int i = 0; i < items.size(); i++) {
                System.out.print(items.get(i));
                if(i != items.size() - 1)
                    System.out.print(", ");
            }
            System.out.print("]");
            System.out.println("");
        }
        private void swap(int[] arr, int i, int j) {
            int temp = arr[i];
            arr[i] = arr[j];
            arr[j] = temp;
        }           
    }

    public static void main(String[] args) throws IOException{
        // Step 1, read input, parse input into your data structure
        InputReader input = new InputReader(new FileReader("taming.in"));
        PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter("taming.out")));
        
        int N = input.nextInt();
        int[] logs = new int[N];
        int[] rlogs = new int[N];
        
        for(int i = 0; i < N; i++){
            logs[i] = input.nextInt();
            rlogs[N-i-1] = logs[i];
        }
        int min = 0;
        int max = 0;
        for(int i = 0; i < N; i++){
            if(rlogs[i] != -1){
                
                min++;
                max++;
                
                i = i + rlogs[i];
            } else{
                max++;
                if(i == N - 1){
                    min++;
                }
            }
        }
      




        // Step 3, output
        System.out.println();
        pw.print(min + " " + max);
        pw.close();
    }
}  

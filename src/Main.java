import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) {
        List<Integer> missingList = findMissing(new Integer[]{4, 12, 9, 5, 6, 1}, new Integer[]{4,9,12,6});
        System.out.println(missingList);

        Integer[] rotatedList = rotate(new Integer[]{1,2,3,4,5,6},1);
        System.out.println(Arrays.toString(rotatedList));

        try {
            System.out.println(Arrays.toString(new ArrayList[]{sortBankAccountsAsc()}));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static List<Integer> findMissing(Integer[] firstArray, Integer[] secondArray) {
        List<Integer> missingList = new ArrayList<>(new HashSet<>(Arrays.asList(firstArray)));
        for (Integer num : secondArray) {
            missingList.remove(num);
        }
        return missingList;
    }

    private static Integer[] rotate(Integer[] A, int B){
        Collections.rotate(Arrays.asList(A), B);
        return A;
    }

    private static ArrayList<List<String>> sortBankAccountsAsc() throws Exception {
        List<String> lines = Collections.emptyList();
        String inputFilePath = System.getProperty("user.dir") + File.separator + "accounts.txt";

        try {
            lines = Files.readAllLines(Paths.get(inputFilePath), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Integer numberOfTests = Integer.valueOf(lines.get(0));

        if (numberOfTests <= 0) {
            throw new Exception("number of tests cannot be zero or lower");
        }

        if (numberOfTests > 5) {
            throw new Exception("number of tests cannot be greater 5");
        }

        ArrayList<List<String>> resultList = new ArrayList<>();
        int nextSequenceNumberOfAccountsIndex = 0;

        for (int i = 1; i <= numberOfTests; i++) {
            int numberOfAccounts = Integer.parseInt(lines.get(i+nextSequenceNumberOfAccountsIndex));
            if (numberOfAccounts > 100000) {
                throw new Exception("number of accounts cannot be greater 100.000");
            }

            List<String> list = new ArrayList<>();
            for (int j = i+1+nextSequenceNumberOfAccountsIndex; j <= i+nextSequenceNumberOfAccountsIndex+numberOfAccounts; j++) {
                String accountNumber = lines.get(j);
                if(accountNumber.matches("\\(\\d{2}\\)-\\d{8}-\\d{4}-\\d{4}-\\d{4}-\\d{4}")){
                    throw new Exception("account format is not valid");
                }
                list.add(accountNumber);
            }

            List<String> listWithDuplicates = new ArrayList<>();
            list.forEach(s -> {
                int count = Collections.frequency(list, s);
                listWithDuplicates.add(s + " "+ count);
            });

            Map<String, Long> listWithDuplicatesMap = listWithDuplicates.stream().collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

            ArrayList<String> sortedListWithDuplicates= new ArrayList<>();
            listWithDuplicatesMap.entrySet()
                    .stream()
                    .sorted(Map.Entry.comparingByValue())
                    .forEach(entry -> {
                        for(int k = 1; k <= entry.getValue(); k++)
                            sortedListWithDuplicates.add(entry.getKey());
                    });

            List<String> listAccountsWithoutDuplicates = sortedListWithDuplicates.stream().distinct().collect(Collectors.toList());

            resultList.add(listAccountsWithoutDuplicates);

            //this +1 added for empty line
            nextSequenceNumberOfAccountsIndex = numberOfAccounts+1;
        }

        return resultList;
    }
}

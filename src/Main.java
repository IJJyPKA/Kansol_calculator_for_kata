import java.util.Arrays;
import java.util.Scanner;

public class Main { //Главная часть программы, принимает в консоль задание, передает в метод calc, ответ выводит в консоль (если не выкенет в исключение)

    public static void main(String[] args) {
        String taskStr = ""; //переменная для получения примера

        Scanner in = new Scanner(System.in);                //получаем задание от пользователя
        System.out.print("Пожалуйста, введите ваш пример: ");
        taskStr = in.nextLine();
        System.out.println("Ответ:"+calc(taskStr));
    }

    public static String calc(String input){ //Метод определяет систему счисления и отлавливает некоторые исключения (Должен существовать по заданию)
        Task task1 = new Task();
        int[] num = new int[3];
        String error = "Введен некорректный символ";

        if (task1.checkTheCorrectness(input)) {
            String[] splitExample = task1.splitInput(input); //Разделил пример

            if (!task1.isRoman(splitExample) && !task1.isArabic(splitExample)) {throw new IllegalArgumentException("Операныды должны быть числами " +
                    "римской или орабской системой счисления, меньше 10 и больше 0.");

            }
            if (task1.isArabic(splitExample)) { //Проверяет являются ли числа арабскими
                num = task1.GiveMeTheArabicNumbers(splitExample); //Заполнил массив операндами [0]- первое число, [2] - второе
                if (num[0]<=10 && num[2]<=10 && num[0]>0 && num[2]>0 )   { //Проверка операндов >0 и <11
                    int resultInt = task1.calcToMe(num[0], splitExample[1], num[2]); //первое число провзаимодействовало со вторым числом
                    return String.valueOf(resultInt); //ВОЗВРАЩАЮ ОТВЕТ
                }
                else throw new IllegalArgumentException("Введите операнды больше 0 и меньше либо равные 10.");
            }

            if (task1.isRoman(splitExample)){
                int operand1 = task1.romanToArabic(splitExample[0]);
                int operand2 = task1.romanToArabic(splitExample[2]);
                if (operand1<=operand2 && splitExample[1].equals("-")) //если ответом будет отрицательное число
                {throw new IllegalArgumentException("В римской системе счисления нет отрицательных чисел и нуля");}
                return task1.arabicToRoman(task1.calcToMe(operand1,splitExample[1],operand2)); //ВОЗВРАЩАЮ ОТВЕТ
            }
        }

        return error;
    }

}

class Task {
    //проверка, введены ли доступные символы.
    boolean checkTheCorrectness(String getStr) {
        String symbolStr = "*:/+-0123456789IVX "; //Допустимые символ
        boolean f = false;
        for (char symbol : getStr.toCharArray()) {
            if (!symbolStr.contains(String.valueOf(symbol))) {
                return false;
            }
        }
        return true;
    }

    //укладывает пример в массив отмеряя по пробелам
    String[] splitInput(String getStr) {
        String[] resultStr = getStr.split(" ");
        if (resultStr.length == 3) {
            return resultStr;
        } else throw new IllegalArgumentException("Некорректный формат ввода, ожидалось 2 пробела " +
                "разделяющих оператор и числа. Например \"10 + 7\"");
    }

    //Возвращает результат взаимодействия 2 х чисел;
    int calcToMe(int Num1, String operator, int Num2) {
        switch (operator) {
            case "+":
                return Num1 + Num2;
            case "-":
                return Num1 - Num2;
            case "*":
                return Num1 * Num2;
            case "/", ":":
                return Num1 / Num2;
            default:
                throw new IllegalArgumentException("Некорректный оператор. Пример корректного ввода \"8 : 2\" или \"3 * 3\"");
        }
    }

    //Возвращает массив целых чисел
    int[] GiveMeTheArabicNumbers(String[] numOfString) { //Конвертирую операнды из массива строк в массив целых чисел
        int[] result = new int[3];
        result[0] = Integer.parseInt(numOfString[0]);
        result[2] = Integer.parseInt(numOfString[2]);
        return result;
    }

    boolean isRoman(String[] example) { //Проверяю являются ли операнды римскими числами && <= 10;
        String[] arrayOfRoman = {"I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX", "X"};
        String checkNum1, checkNum2;
        checkNum1 = example[0];
        checkNum2 = example[2];
        if (Arrays.asList(arrayOfRoman).contains(checkNum1) && Arrays.asList(arrayOfRoman).contains(checkNum2))
            return true;
        else if (Arrays.asList(arrayOfRoman).contains(checkNum1) || Arrays.asList(arrayOfRoman).contains(checkNum2)) {
            throw new IllegalArgumentException("Используйте одну систему счисления, также операнды должны быть <= 10. <=X. Напимер X * IX или 7 + 4");
        }
        return false;
    }

    boolean isArabic(String[] example) { //Проверяю являются ли операнды арабскими числами
        try {
            Integer.parseInt(example[0]);
            Integer.parseInt(example[2]);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

String arabicToRoman(int num) {
        String[] romanSymbols = {"I", "IV", "V", "IX", "X", "XL", "L", "XC", "C", "CD", "D", "CM", "M"}; //Набор римских
        int[] values = {1, 4, 5, 9, 10, 40, 50, 90, 100, 400, 500, 900, 1000}; //Соответствующий набор арабских

        StringBuilder romanNumeral = new StringBuilder(); //переменная для формирования римской цифры
        int i = values.length - 1;  //Длинна массива минус 1 чтобы обратиться к последнему эллементу.

        while (num > 0) { //пока число себя не исчерпало
            if (num - values[i] >= 0) {  //по возможности вычесть из числа наибольший эллемент массива
                romanNumeral.append(romanSymbols[i]); //Приписываем в строитель строк
                num -= values[i];
            } else { //сместиться ниже по массиву
                i--;
            }
        }
        return romanNumeral.toString(); //приводим ответ к кассу строки и возвращаем
    }

int romanToArabic(String romanNumber) {
        //2 массива эквивалентных друг другу
        String[] romanSymbols = {"I", "IV", "V", "IX", "X", "XL", "L", "XC", "C", "CD", "D", "CM", "M"};
        int[] values = {1, 4, 5, 9, 10, 40, 50, 90, 100, 400, 500, 900, 1000};

        int result = 0;
        int index = 0;

        for (int i = 0; i < romanNumber.length(); i++) { //Прохожусь по полученной строке
            for (int j = 0; j < romanSymbols.length; j++) { //Сравниваю с массивом римских чисел
//Символ не последний, совпадает ли подстрока из двух символов
                if (i < romanNumber.length() - 1 && romanSymbols[j].equals(romanNumber.substring(i, i + 2))) {
                    result += values[j];
                    i++;
                    break;
//совпадает ли подстрока их одного символа
                } else if (romanSymbols[j].equals(romanNumber.substring(i, i + 1))) {
                    result += values[j];
                    break;
                }
            }
        }
        return result;
    }

}


package main.java;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите выражение, которое хотите вычислить:");
        String input = scanner.nextLine();
        String result;
        try {
            result = calc(input);
        } catch (IOException e) {
            result = e.getMessage();
        }
        System.out.println(result);
    }

    public static String calc(String input) throws IOException {
        String[] expressionElements = input
                .trim()
                .split(" ");
        String validationResult = validateExpression(expressionElements);
        if (validationResult != null) {
            return validationResult;
        }

        return String.valueOf(executeExpression(expressionElements));
    }

    private static int executeExpression(String[] expressionElements) throws IOException {
        int firstOperand = Integer.parseInt(expressionElements[0]);
        ArithmeticOperator operand = ArithmeticOperator.getBySymbol(expressionElements[1]);
        int secondOperand = Integer.parseInt(expressionElements[2]);
        return operand.apply(firstOperand, secondOperand);
    }

    private static String validateExpression(String[] expressionElements) throws IOException {
        if (expressionElements.length != 3) {
            return "Неверный формат выражения. Ожидается формат \"a + b\"";
        }

        String firstOperand = expressionElements[0];
        String validationResult = validateOperand(firstOperand);
        if (validationResult != null) {
            return validationResult;
        }

        String operator = expressionElements[1];
        validationResult = validateOperator(operator);
        if (validationResult != null) {
            return validationResult;
        }

        String secondOperand = expressionElements[2];
        validationResult = validateOperand(secondOperand);
        return validationResult;
    }

    private static String validateOperator(String operator) {
        if (operator.length() != 1 || !ArithmeticOperator.hasSymbol(operator)) {
            return String.format("Оператор должен быть представлен одним из операторов из списка \"%s\"",
                    ArithmeticOperator.getAllSymbols());
        }
        return null;
    }

    private static String validateOperand(String operand) {
        int convertedOperand;
        try {
            convertedOperand = Integer.parseInt(operand);
        } catch (NumberFormatException e) {
            return String.format("Операнд \"%s\" не является целым числом", operand);
        }

        if (convertedOperand < 1 || convertedOperand > 10) {
            return String.format("Значение операнда \"%s\" не попадает в диапазон [1, 10]", operand);
        }
        return null;
    }

    private enum ArithmeticOperator {
        Addition("+") {
            @Override
            public int apply(double firstOperand, double secondOperand) {
                return (int) (firstOperand + secondOperand);
            }
        },
        Subtraction("-") {
            @Override
            public int apply(double firstOperand, double secondOperand) {
                return (int) (firstOperand - secondOperand);
            }
        },
        Multiplication("*") {
            @Override
            public int apply(double firstOperand, double secondOperand) {
                return (int) (firstOperand * secondOperand);
            }
        },
        Division("/") {
            @Override
            public int apply(double firstOperand, double secondOperand) throws IOException {
                if (secondOperand == 0) {
                    throw new IOException("Деление на 0 невозможно!");
                }
                return (int) (firstOperand / secondOperand);
            }
        };

        private final String symbol;

        ArithmeticOperator(String symbol) {
            this.symbol = symbol;
        }

        public abstract int apply(double operand1, double operand2) throws IOException;

        public String getSymbol() {
            return symbol;
        }


        public static ArithmeticOperator getBySymbol(String parameter) {
            for (ArithmeticOperator element : ArithmeticOperator.values()) {
                if (element.getSymbol().equals(parameter)) {
                    return element;
                }
            }

            return null;
        }

        public static boolean hasSymbol(String symbol) {
            boolean result = false;
            for (ArithmeticOperator arithmeticOperator : ArithmeticOperator.values()) {
                if (arithmeticOperator.getSymbol().equals(symbol)) {
                    return true;
                }
            }
            return result;
        }

        public static String getAllSymbols() {
            StringBuilder symbols = new StringBuilder();
            for (ArithmeticOperator arithmeticOperator : ArithmeticOperator.values()) {
                symbols.append(arithmeticOperator.getSymbol());
            }
            return symbols.toString();
        }
    }
}

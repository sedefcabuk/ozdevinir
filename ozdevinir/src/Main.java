import java.util.Scanner;
import java.util.Stack;

public class Main {
    // İfadeyi kontrol et
    public static boolean checkExpression(String expression) {
        Stack<Character> stack = new Stack<>();

        for (char ch : expression.toCharArray()) {
            if (ch == '(') {
                stack.push(ch);
            } else if (ch == ')') {
                if (stack.isEmpty() || stack.pop() != '(') {
                    return false; // Parantezler dengeli değil
                }
            }
        }

        // Parantezlerin dengeli olduğunu kontrol et
        if (!stack.isEmpty()) {
            return false;
        }

        try {
            // İfadeyi bir matematiksel ifade olarak değerlendir ve sonucu döndür
            double result = eval(expression);
            return true;
        } catch (ArithmeticException e) {
            return false; // Sıfıra bölme hatası
        } catch (Exception e) {
            return false;
        }
    }

    // İfadeyi hesapla
    public static double eval(String expression) {
        return new Object() {
            int pos = -1, ch;

            void nextChar() {
                ch = (++pos < expression.length()) ? expression.charAt(pos) : -1;
            }

            boolean eat(int charToEat) {
                while (ch == ' ') nextChar();
                if (ch == charToEat) {
                    nextChar();
                    return true;
                }
                return false;
            }

            double parse() {
                nextChar();
                double x = parseExpression();
                if (pos < expression.length()) throw new RuntimeException("Unexpected: " + (char) ch);
                return x;
            }

            // İfadeyi değerlendir
            double parseExpression() {
                double x = parseTerm();
                for (;;) {
                    if (eat('+')) x += parseTerm();
                    else if (eat('-')) x -= parseTerm();
                    else return x;
                }
            }

            // İfadeyi çözümle
            double parseTerm() {
                double x = parseFactor();
                for (;;) {
                    if (eat('*')) x *= parseFactor();
                    else if (eat('/')) {
                        double divisor = parseFactor();
                        if (divisor == 0) {
                            throw new ArithmeticException("Division by zero");
                        }
                        x /= divisor;
                    } else return x;
                }
            }

            // İfadeyi işlem yapılabilir parçalara ayır
            double parseFactor() {
                if (eat('+')) return parseFactor();
                if (eat('-')) return -parseFactor();

                double x;
                int startPos = this.pos;
                if (eat('(')) {
                    x = parseExpression();
                    eat(')');
                } else if ((ch >= '0' && ch <= '9') || ch == '.') {
                    while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
                    x = Double.parseDouble(expression.substring(startPos, this.pos));
                } else {
                    throw new RuntimeException("Unexpected: " + (char) ch);
                }

                return x;
            }
        }.parse();
    }

    public static void main(String[] args) {
    	
    	for(;;) {
	        // Kullanıcıdan ifadeyi al
	        Scanner scanner = new Scanner(System.in);
	        System.out.print("Lütfen bir matematiksel ifade giriniz: ");
	        String expression = scanner.nextLine();
	
	        // İfadeyi kontrol et ve sonucu yazdır
	        boolean isValid = checkExpression(expression);
	        if (isValid) {
	            System.out.println("Girilen ifade matematiksel bir ifadedir.");
	        } else {
	            System.out.println("Girilen ifade matematiksel bir ifade değildir.");
	        }
    	}
        
    }
}

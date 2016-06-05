import java.util.Scanner;

public class finalTest {

	public static void main(String[] args) {

//		String test;
//		Scanner scan = new Scanner(System.in);
//		 
//		System.out.println("Testing Scanner, write something: ");
//		test = scan.nextLine();
		convertToPostFix("3*(4+5)");
//		convertToPostFix("2*((3+5)*(3+2))");
//		convertToPostFix("6*(3+(7*8)*(5+2))");

	}
	
	// Takes a equation as string in infix notation and converts
	// it to post fix notation
	public static void convertToPostFix(String infix){
		
		//DEBUG: prints our received input
		System.out.println(infix);
		
		// Converts our input string into an array
		String[] infixArr = infix.split("(?!^)");
	    StringBuffer postfixString = new StringBuffer(infix.length());
		Stack<String> theStack = new Stack<String>(String.class);

		
		//  Loop through the infix notation
		for (int i = 0; i < infixArr.length; i++){
			
			System.out.println(infixArr[i]);
			if(!isOperator(infixArr[i])){
				System.out.println("Appending " + infixArr[i] + " to postfix string");
				postfixString.append(infixArr[i]);
			}else{
				// if ending paren
				if(infixArr[i].equals(")")){
					System.out.println("Unwinding");
					// unwind stack
					while(!theStack.isEmpty() && !theStack.peek().equals("(")){
						postfixString.append(theStack.pop());
						
//						System.out.println("the peek is " + theStack.peek());
//						if(theStack.peek().equals("(")){
//							System.out.println("This equation has ended");
//							theStack.pop();
//						}else{
//							postfixString.append(theStack.pop());
//						}
					}
					
				}
				// push operator onto the stack
				else{
					System.out.println("Appending " + infixArr[i] + " to the stack");
					theStack.push(infixArr[i]);
					
				}
				
			}

			
		}	
		if(!theStack.isEmpty()){
			while(!theStack.isEmpty()){
				if(!theStack.peek().equals("(")){
					postfixString.append(theStack.pop());	
				}else{
					theStack.pop();
				}
			}
		}
		System.out.println(postfixString);

		
	}
	
	public static boolean isOperator(String o){
		return o.equals("+") || o.equals("-") || o.equals("*") || o.equals("/") || o.equals("^") || o.equals("(") || o.equals(")");
	}
}

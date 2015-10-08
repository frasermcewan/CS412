// This example is from the book "Java in a Nutshell, Second Edition".
// Written by David Flanagan.  Copyright (c) 1997 O'Reilly & Associates.
// You may distribute this source code for non-commercial purposes only.
// You may study, modify, and use this example for any purpose, as long as
// this notice is retained.  Note that this example is provided "as is",
// WITHOUT WARRANTY of any kind either expressed or implied.

// Here we define some exception types of our own.
// Exception classes generally have constructors but no data or 
// other methods.  All these do is to call their superclass constructors.
class MyException extends Exception {
    public MyException() { super(); }
    public MyException(String s) { super(s); }
}
class MyOtherException extends Exception {
    public MyOtherException() { super(); }
    public MyOtherException(String s) { super(s); }
}
class MySubException extends MyException {
    public MySubException() { super(); }
    public MySubException(String s) { super(s); }
}

// This class demonstrates defining, throwing and handling exceptions.
// Try invoking it in the following ways and try to understand the 
// output:
//    java throwtest
//    java throwtest one
//    java throwtest 0
//    java throwtest 1
//    java throwtest 99
//    java throwtest 2
//    java throwtest 3
public class throwtest {
    // This is the main() method.  Note that it uses two
    // catch clauses to handle two standard Java exceptions.
    public static void main(String argv[]) {
        int i;

        // First, convert our argument to an integer
        // Make sure we have an argument and that it is convertible.
        try {
            i = Integer.parseInt(argv[0]);
        }
        catch (ArrayIndexOutOfBoundsException e) { // argv is empty
            System.out.println("Must specify an argument");
            return;
        }
        catch (NumberFormatException e) { // argv[0] isn't an integer
            System.out.println("Must specify an integer argument.");
            return;
        }

        // Now, pass that integer to method a().
        a(i);
    }

    // This method invokes b(), which is declared to throw
    // one type of exception.  We handle that one exception.
    public static void a(int i) {
        try {
            b(i);
        }
        catch (MyException e) {                              // Point 1.
            // Here we handle MyException and 
            // its subclass MyOtherException
            if (e instanceof MySubException)
                System.out.print("MySubException: ");
            else
                System.out.print("MyException: ");
            System.out.println(e.getMessage());
            System.out.println("Handled at point 1");
        }
    }

    // This method invokes c(), and handles one of the
    // two exception types that that method can throw.  The other 
    // exception type is not handled, and is propagated up
    // and declared in this method's throws clause.
    // This method also has a finally clause to finish up 
    // the work of its try clause.  Note that the finally clause
    // is executed after a local catch clause, but before a
    // a containing catch clause or one in an invoking procedure.
    public static void b(int i) throws MyException {
        int result;
        try {
            System.out.print("i = " + i);
            result = c(i);
            System.out.print(" c(i) = " + result);
        }
        catch (MyOtherException e) {                          // Point 2
            // Handle MyOtherException exceptions:
            System.out.println("MyOtherException: " + e.getMessage());
            System.out.println("Handled at point 2");
        }
        finally {
            // Terminate the output we printed above with a newline.
            System.out.print("\n");
        }
    }

    // This method computes a value or throws an exception.
    // The throws clause only lists two exceptions, because
    // one of the exceptions thrown is a subclass of another.
    public static int c(int i) throws MyException, MyOtherException {
        switch (i) {
            case 0: // processing resumes at point 1 above
                throw new MyException("input too low");
            case 1: // processing resumes at point 1 above
                throw new MySubException("input still too low");
            case 99:// processing resumes at point 2 above
                throw new MyOtherException("input too high");
            default:
                return i*i;
        }
    }
}

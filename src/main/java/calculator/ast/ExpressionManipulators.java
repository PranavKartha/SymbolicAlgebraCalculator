package calculator.ast;

import calculator.interpreter.Environment;

import java.util.Iterator;
import java.awt.*;
import calculator.errors.EvaluationError;
import calculator.gui.ImageDrawer;
import datastructures.concrete.DoubleLinkedList;
import datastructures.interfaces.IDictionary;
import datastructures.interfaces.IList;

/**
 * All of the public static methods in this class are given the exact same parameters for
 * consistency. You can often ignore some of these parameters when implementing your
 * methods.
 *
 * Some of these methods should be recursive. You may want to consider using public-private
 * pairs in some cases.
 */
public class ExpressionManipulators {
    /**
     * Checks to make sure that the given node is an operation AstNode with the expected
     * name and number of children. Throws an EvaluationError otherwise.
     */
    private static void assertNodeMatches(AstNode node, String expectedName, int expectedNumChildren) {
        if (!node.isOperation()
                && !node.getName().equals(expectedName)
                && node.getChildren().size() != expectedNumChildren) {
            throw new EvaluationError("Node is not valid " + expectedName + " node.");
        }
    }

    /**
     * Accepts an 'toDouble(inner)' AstNode and returns a new node containing the simplified version
     * of the 'inner' AstNode.
     *
     * Preconditions:
     *
     * - The 'node' parameter is an operation AstNode with the name 'toDouble'.
     * - The 'node' parameter has exactly one child: the AstNode to convert into a double.
     *
     * Postconditions:
     *
     * - Returns a number AstNode containing the computed double.
     *
     * For example, if this method receives the AstNode corresponding to
     * 'toDouble(3 + 4)', this method should return the AstNode corresponding
     * to '7'.
     * 
     * This method is required to handle the following binary operations
     *      +, -, *, /, ^
     *  (addition, subtraction, multiplication, division, and exponentiation, respectively) 
     * and the following unary operations
     *      negate, sin, cos
     *
     * @throws EvaluationError  if any of the expressions contains an undefined variable.
     * @throws EvaluationError  if any of the expressions uses an unknown operation.
     */
    public static AstNode handleToDouble(Environment env, AstNode node) {
        // To help you get started, we've implemented this method for you.
        // You should fill in the locations specified by "your code here"
        // in the 'toDoubleHelper' method.
        //
        // If you're not sure why we have a public method calling a private
        // recursive helper method, review your notes from CSE 143 (or the
        // equivalent class you took) about the 'public-private pair' pattern.

        assertNodeMatches(node, "toDouble", 1);
        AstNode exprToConvert = node.getChildren().get(0);
        return new AstNode(toDoubleHelper(env.getVariables(), exprToConvert));
    }

    private static double toDoubleHelper(IDictionary<String, AstNode> variables, AstNode node) {
        // There are three types of nodes, so we have three cases. 
        if (node.isNumber()) {
            return node.getNumericValue();
        } else if (node.isVariable()) {         
            String variable = node.getName();
            if (variables.containsKey(variable)) {
                return toDoubleHelper(variables, variables.get(variable));
            }
            throw new EvaluationError("unknown variable");
        } else {
            return checkToDoubleOperations(node, variables);
            // You may assume the expression node has the correct number of children.
            // If you wish to make your code more robust, you can also use the provided
            // "assertNodeMatches" method to verify the input is valid.

        }
    }

    
    // Applies the effects of the inputed operation and returns a double value based
    // on the inputed AstNode and IDictionary<String, AstNode> references
    private static double checkToDoubleOperations(AstNode node, IDictionary<String, AstNode> variables) {
        String name = node.getName();
        
        if (name.equals("+")) {
            return toDoubleHelper(variables, node.getChildren().get(0)) 
                    + toDoubleHelper(variables, node.getChildren().get(1));
        } else if (name.equals("-")) {
            return toDoubleHelper(variables, node.getChildren().get(0)) 
                    - toDoubleHelper(variables, node.getChildren().get(1));
        } else if (name.equals("*")) {
            return toDoubleHelper(variables, node.getChildren().get(0)) 
                    * toDoubleHelper(variables, node.getChildren().get(1));
        } else if (name.equals("/")) {
            return toDoubleHelper(variables, node.getChildren().get(0)) 
                    / toDoubleHelper(variables, node.getChildren().get(1));
        } else if (name.equals("^")) {
            return Math.pow(toDoubleHelper(variables, node.getChildren().get(0)),
                    toDoubleHelper(variables, node.getChildren().get(1)));
        } else if (name.equals("negate")) {
            return -1.0 * toDoubleHelper(variables, node.getChildren().get(0));
        } else if (name.equals("sin")) {
            return Math.sin(toDoubleHelper(variables, node.getChildren().get(0)));
        } else if (name.equals("cos")) {
            return Math.cos(toDoubleHelper(variables, node.getChildren().get(0)));
        }
        throw new EvaluationError("unknown operation");
    }

    /**
     * Accepts a 'simplify(inner)' AstNode and returns a new node containing the simplified version
     * of the 'inner' AstNode.
     *
     * Preconditions:
     *
     * - The 'node' parameter is an operation AstNode with the name 'simplify'.
     * - The 'node' parameter has exactly one child: the AstNode to simplify
     *
     * Postconditions:
     *
     * - Returns an AstNode containing the simplified inner parameter.
     *
     * For example, if we received the AstNode corresponding to the expression
     * "simplify(3 + 4)", you would return the AstNode corresponding to the
     * number "7".
     *
     * Note: there are many possible simplifications we could implement here,
     * but you are only required to implement a single one: constant folding.
     *
     * That is, whenever you see expressions of the form "NUM + NUM", or
     * "NUM - NUM", or "NUM * NUM", simplify them.
     */
    public static AstNode handleSimplify(Environment env, AstNode node) {
        // Try writing this one on your own!
        // Hint 1: Your code will likely be structured roughly similarly
        //         to your "handleToDouble" method
        // Hint 2: When you're implementing constant folding, you may want
        //         to call your "handleToDouble" method in some way
        // Hint 3: When implementing your private pair, think carefully about
        //         when you should recurse. Do you recurse after simplifying
        //         the current level? Or before?

        assertNodeMatches(node, "simplify", 1);

        AstNode exprToConvert = node.getChildren().get(0);
        return handleSimplifyHelper(env.getVariables(), exprToConvert);
    }
    
//    //
//    private static AstNode makeCopy(AstNode node, IList<AstNode> children) {
//        if (children.size() == 0) {
//            if (node.isNumber()) {
//                return new AstNode(node.getNumericValue());
//            } else {
//                return new AstNode(node.getName());
//            }
//        } else {
//            
//            IList<AstNode> kids = new DoubleLinkedList<AstNode>();
//            kids.add(makeCopy(children.get(0), children.get(0).getChildren()));
//            if (children.size() == 2) {
//                kids.add(makeCopy(children.get(1), children.get(1).getChildren()));
//            }
//            AstNode copy = new AstNode(node.getName(), kids);
//            
//            return copy;
//        }
//    }
    
    private static AstNode handleSimplifyHelper(IDictionary<String, AstNode> variables, AstNode node){
        if (node.isNumber()) {
            return new AstNode(node.getNumericValue());
            
        } else if (node.isVariable()) {
            if (variables.containsKey(node.getName())) {
                AstNode check = variables.get(node.getName());
//                AstNode varGetCopy;
//                if (check.isOperation()) {
//                    varGetCopy = makeCopy(check, check.getChildren());
//                } else { //possible variable case for later
//                    varGetCopy = new AstNode(check.getNumericValue());
//                    return varGetCopy;
//                }
//                varGetCopy = handleSimplifyHelper(variables, varGetCopy);
////                IDictionary<String, AstNode> newVariables = new ArrayDictionary<String, AstNode>();
////                for (int i = 0; i < variables.size(); i++) {
////                    newVariables.put(variables., value);
////                }
                return handleSimplifyHelper(variables, check);
//                // need to show results of handleSimplifyHelper
//                // on varGetCopy BUT need that to be isolated from the
//                // variables dictioanry
            }
            return new AstNode(node.getName());
            
        } else {
            // node MUST be an operation here
            if (node.getName().equals("sin") || node.getName().equals("cos") || node.getName().equals("negate")) {
                node.getChildren().set(0, 
                        handleSimplifyHelper(variables, node.getChildren().get(0)));
                return node;
            }
            if (node.getName().equals("/") || node.getName().equals("^")) {
                AstNode left = node.getChildren().get(0);
                AstNode right = node.getChildren().get(1);
                
                left = handleSimplifyHelper(variables, left);
                right = handleSimplifyHelper(variables, right);
                
                node.getChildren().set(0, left);
                node.getChildren().set(1, right);
                
                return node;
            }
            
            if (node.getName().equals("+") || node.getName().equals("-") || node.getName().equals("*")) {
                if (node.getChildren().get(0).isNumber() && node.getChildren().get(1).isNumber()) {
                    return new AstNode(toDoubleHelper(variables, node));
                } else {
                    
                    AstNode left = handleSimplifyHelper(variables, node.getChildren().get(0));
                    AstNode right = handleSimplifyHelper(variables, node.getChildren().get(1));
                    
                    IList<AstNode> children = new DoubleLinkedList<AstNode>();
                    
                    children.add(left);
                    children.add(right);
                    
                    AstNode newNode = new AstNode(node.getName(), children);
                    
//                    newNode.getChildren().add(left);
//                    newNode.getChildren().add(right);
                    
                    if (left.isNumber() && right.isNumber()) {
                        return new AstNode(toDoubleHelper(variables, newNode));
                    } else {
                        return newNode;
                    }
                }
            }
            return node;
        }
    }
    
    
    
    /**
     * Accepts an Environment variable and a 'plot(exprToPlot, var, varMin, varMax, step)'
     * AstNode and generates the corresponding plot on the ImageDrawer attached to the
     * environment. Returns some arbitrary AstNode.
     *
     * Example 1:
     *
     * >>> plot(3 * x, x, 2, 5, 0.5)
     *
     * This method will receive the AstNode corresponding to 'plot(3 * x, x, 2, 5, 0.5)'.
     * Your 'handlePlot' method is then responsible for plotting the equation
     * "3 * x", varying "x" from 2 to 5 in increments of 0.5.
     *
     * In this case, this means you'll be plotting the following points:
     *
     * [(2, 6), (2.5, 7.5), (3, 9), (3.5, 10.5), (4, 12), (4.5, 13.5), (5, 15)]
     *
     * ---
     *
     * Another example: now, we're plotting the quadratic equation "a^2 + 4a + 4"
     * from -10 to 10 in 0.01 increments. In this case, "a" is our "x" variable.
     *
     * >>> c := 4
     * 4
     * >>> step := 0.01
     * 0.01
     * >>> plot(a^2 + c*a + a, a, -10, 10, step)
     *
     * --- 
     *
     * @throws EvaluationError  if any of the expressions contains an undefined variable.
     * @throws EvaluationError  if varMin > varMax
     * @throws EvaluationError  if 'var' was already defined
     * @throws EvaluationError  if 'step' is zero or negative 
     */
    public static AstNode plot(Environment env, AstNode node) {
        
        assertNodeMatches(node, "plot", 5);

        AstNode exprToPlot = node.getChildren().get(0);
        AstNode var = node.getChildren().get(1);
        AstNode varMin = node.getChildren().get(2);
        AstNode varMax = node.getChildren().get(3);
        AstNode step = node.getChildren().get(4);
        
      
        
        if (!allVarsExist(node, env.getVariables())) {
            throw new EvaluationError("undefined expression!");
        }
        if (varMin.getNumericValue() > varMax.getNumericValue()) {
            throw new EvaluationError("varMin > varMax!");
        }
        if (step.getNumericValue() <= 0) {
            throw new EvaluationError("step is less than/equal to 0!");
        }
        if (env.getVariables().containsKey(var.getName())) {
            throw new EvaluationError("Variable " + var.getName() + " already exists!");
        }
        
        IList<Double> xValues = new DoubleLinkedList<Double>();
        IList<Double> yValues = new DoubleLinkedList<Double>();

        
        for(double i = varMin.getNumericValue(); i <= varMax.getNumericValue(); i += step.getNumericValue()) {
            xValues.add(i);
        }
        
        Iterator<Double> iterator = xValues.iterator();
        while(iterator.hasNext()) {
            yValues.add(handlePlot(env.getVariables(), exprToPlot, iterator.next(), var.getName()));            
        }
        
        env.getVariables().remove(var.getName());
        
        ImageDrawer drawer = env.getImageDrawer();
        drawer.drawScatterPlot("title", "xAxisLabel", "yAxisLabel", xValues, yValues);
        return new AstNode(1);
    }

    //
    private static Double handlePlot(IDictionary<String, AstNode> variables, AstNode exprToPlot, double next, String name) {
        variables.put(name, new AstNode(next));
        return handleSimplifyHelper(variables, exprToPlot).getNumericValue();
    }
    
    //
    private static boolean allVarsExist(AstNode node, IDictionary<String, AstNode> variables) {
        boolean varsExist = false;
        for (int i = 0; i < node.getChildren().size(); i++) {
            varsExist = checkVars(node.getChildren().get(i), variables);
        }
        
        return varsExist;
    }
    
    private static boolean checkVars(AstNode node, IDictionary<String, AstNode> variables) {
        if (node.isNumber()) {
            return true;
        } else if (node.isVariable()) {
            return variables.containsKey(node.getName());
        } else {
            if (node.getChildren().size() == 1) {
                return checkVars(node.getChildren().get(0), variables);
            } else {
                return checkVars(node.getChildren().get(0), variables) && checkVars(node.getChildren().get(1), variables);
            }
        }
    }

        // Note: every single function we add MUST return an
        // AST node that your "simplify" function is capable of handling.
        // However, your "simplify" function doesn't really know what to do
        // with "plot" functions (and what is the "plot" function supposed to
        // evaluate to anyways?) so we'll settle for just returning an
        // arbitrary number.
        //
        // When working on this method, you should uncomment the following line:
        //
        // return new AstNode(1);
}

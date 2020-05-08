package com.company.syntaxAnalysis;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class SyntaxAnalysis {
    private static TokenType currentToken;//当前TokenType
    private static String currentValue;//当前token的value

    private static List<String> tokenList=new ArrayList<>();//词法分析后的列表的将赋给他
    private static List<String> errorList=new ArrayList<>();//错误列表
    private StringBuilder treeResult=new StringBuilder();//抽象语法树遍历结果
    private  static int pointer=0;//当前指示的token，即第几行

    public void setTokenList(List<String> tokenList){
        SyntaxAnalysis.tokenList=tokenList;
    }
    /**
     * 处理函数
     */
    public void analysis() {
        nextToken();
        while (pointer < tokenList.size()) {
            treeResult = TreeNode.preOrder(Goal(), treeResult, "|-").append("\r\n");
        }
    }

    /**
     * 输出结果至文件
     * @param outputFilename 结果输出文件名
     * @throws FileNotFoundException
     * @throws UnsupportedEncodingException
     */
    public void output(String outputFilename) throws FileNotFoundException, UnsupportedEncodingException {
        PrintWriter writer = new PrintWriter(outputFilename, "UTF-8");
        writer.write(treeResult.toString());
        writer.close();
    }

    /**
     * 输出错误至文件
     * @param errorFilename 错误结果输出文件名
     * @throws UnsupportedEncodingException
     * @throws IOException
     */
    public void errorOutput(String errorFilename) throws UnsupportedEncodingException, IOException {
        PrintWriter writer = new PrintWriter(errorFilename, "UTF-8");
        for (String temp : errorList)
            writer.write(temp.toString() + "\n");

        writer.close();
    }

    private static void nextToken() {
        if(pointer<tokenList.size()){
            String temp=tokenList.get(pointer);
            String[] info=temp.split(",");
            currentToken=TokenType.valueOf(info[1]);
            currentValue=info[0];
            pointer++;
        }
    }

    private  static boolean match(TokenType need){
        if(currentToken==need){
            nextToken();
            return true;
        }else {
            String errorInfo="The " + pointer + " Token : <" +
                    currentToken + " , " + currentValue
                    + "> is wrong , The expected token type is \"" +
                    need + "\".";
            System.err.println(errorInfo);
            errorList.add(errorInfo);
            nextToken();
            return false;
        }
    }
    private static TreeNode Goal() {
        TreeNode goal=new TreeNode();
        return goal;
    }

    private static TreeNode MainClass() {
        TreeNode mainClass=new TreeNode();
        return mainClass;
    }

    private static TreeNode ClassDeclaration() {
        TreeNode classDeclaration=new TreeNode();
        return classDeclaration;
    }

    private static TreeNode VarDeclaration() {
        TreeNode varDeclaration=new TreeNode();
        return varDeclaration;
    }

    private static TreeNode MethodDeclaration() {
        TreeNode methodDeclaration=new TreeNode();
        return methodDeclaration;
    }

    private static TreeNode Type() {
        TreeNode type=new TreeNode();
        return type;
    }

    private static TreeNode Statements() {
        TreeNode statements=new TreeNode();
        return statements;
    }

    private static TreeNode ifStatement() {
        TreeNode ifStatement=new TreeNode();
        return ifStatement;
    }

    private static TreeNode WhileStatement() {
        TreeNode whileStatement=new TreeNode();
        return whileStatement;
    }

    private static TreeNode PrintStatement() {
        TreeNode printStatement=new TreeNode();
        return printStatement;
    }

    private static TreeNode AssignStatement() {
        TreeNode assignStatement=new TreeNode();
        return assignStatement;
    }

    private static TreeNode ArrayStatement() {
        TreeNode arrayStatement=new TreeNode();
        return arrayStatement;
    }

    private static TreeNode Statement() {
        TreeNode statement=new TreeNode();
        return statement;
    }

    private static TreeNode IntExpression() {
        TreeNode intExpression=new TreeNode();
        return intExpression;
    }

    private static TreeNode TrueExpression() {
        TreeNode trueExpression=new TreeNode();
        return trueExpression;
    }

    private static TreeNode FalseExpression() {
        TreeNode falseExpression=new TreeNode();
        return falseExpression;
    }

    private static TreeNode IdentifierExpression() {
        TreeNode identifierExpression=new TreeNode();
        return identifierExpression;
    }

    private static TreeNode ThisExpression() {
        TreeNode thisExpression=new TreeNode();
        return thisExpression;
    }

    private static TreeNode NewArrayExpression() {
        TreeNode newArrayExpression=new TreeNode();
        return newArrayExpression;
    }

    private static TreeNode NewExpression() {
        TreeNode newExpression=new TreeNode();
        return newExpression;
    }

    private static TreeNode NoExpression() {
        TreeNode noExpression=new TreeNode();
        return noExpression;
    }

    private static TreeNode BraceExpression() {
        TreeNode braceExpression=new TreeNode();
        return braceExpression;
    }

    private static TreeNode Expression() {
        TreeNode expression=new TreeNode();
        return expression;
    }

    private static TreeNode OPL() {
        TreeNode opL=new TreeNode();
        return opL;
    }

    private static TreeNode ExpressionL() {
        TreeNode expressionL=new TreeNode();
        return expressionL;
    }

    private static TreeNode LengthL() {
        TreeNode lengthL=new TreeNode();
        return lengthL;
    }

    private static TreeNode MethodL() {
        TreeNode methodL=new TreeNode();
        return methodL;
    }

    private static TreeNode L() {
        TreeNode L=new TreeNode();
        return L;
    }

}



}



}

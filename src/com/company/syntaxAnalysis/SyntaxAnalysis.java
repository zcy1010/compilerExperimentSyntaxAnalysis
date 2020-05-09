package com.company.syntaxAnalysis;

import jdk.nashorn.internal.parser.Token;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class SyntaxAnalysis {
    private static TokenType currentToken;//当前TokenType
    private static String currentValue;//当前token的value

    private static TokenType nextToken;//下一个TokenType
    private static String nextValue;//下一个token的value

    private static List<String> tokenList = new ArrayList<>();//词法分析后的列表的将赋给他
    private static List<String> errorList = new ArrayList<>();//错误列表
    private StringBuilder treeResult = new StringBuilder();//抽象语法树遍历结果
    private static int pointer = 0;//当前指示的token，即第几行

    public void setTokenList(List<String> tokenList) {
        SyntaxAnalysis.tokenList = tokenList;
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
     *
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
     *
     * @param errorFilename 错误结果输出文件名
     * @throws UnsupportedEncodingException
     * @throws IOException
     */
    public void errorOutput(String errorFilename) throws UnsupportedEncodingException, IOException {
        PrintWriter writer = new PrintWriter(errorFilename, "UTF-8");
        for (String temp : errorList)
            writer.write(temp + "\n");

        writer.close();
    }

    private static void nextToken() {
        if (pointer < tokenList.size()) {
            String temp = tokenList.get(pointer);
            String[] info = temp.split(",");
            currentToken = TokenType.valueOf(info[1]);
            currentValue = info[0];
            pointer++;
        }
    }

    private static void getNextWord() {
        if (pointer < tokenList.size()) {
            String temp = tokenList.get(pointer + 1);
            String[] info = temp.split(",");
            nextToken = TokenType.valueOf(info[1]);
            nextValue = info[0];
        }
    }

    private static boolean match(TokenType need) {
        if (currentToken == need) {
            nextToken();
            return true;
        } else {
            String errorInfo = "The " + pointer + " Token : <" +
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
        TreeNode goal = new TreeNode();
        goal.setStatement(Statement.GOAL);
        goal.children.add(MainClass());
        match(TokenType.LBRACE);
        while (currentToken == TokenType.CLASS) {
            goal.children.add(ClassDeclaration());
        }
        match(TokenType.RBRACE);
        goal.setTokenType(TokenType.EOF);
        goal.setValue("EOF");
        match(TokenType.EOF);
        return goal;
    }

    private static TreeNode MainClass() {
        TreeNode mainClass = new TreeNode();
        mainClass.setStatement(Statement.MAINCLASS);
        match(TokenType.CLASS);

        mainClass.setTokenType(TokenType.IDENTIFIER);
        mainClass.setValue(currentValue);
        match(TokenType.IDENTIFIER);

        match(TokenType.LBRACE);
        match(TokenType.PUBLIC);
        match(TokenType.STATIC);
        match(TokenType.VOID);
        match(TokenType.MAIN);
        match(TokenType.LPAREN);
        match(TokenType.STRING);
        match(TokenType.LBRACKET);
        match(TokenType.RBRACKET);

        mainClass.setValue(mainClass.getValue() + "%" + currentValue);
        match(TokenType.IDENTIFIER);

        match(TokenType.RPAREN);
        match(TokenType.LBRACE);
        mainClass.children.add(Statement());
        match(TokenType.RBRACE);
        match(TokenType.RBRACE);
        return mainClass;
    }

    private static TreeNode ClassDeclaration() {
        TreeNode classDeclaration = new TreeNode();
        classDeclaration.setStatement(Statement.CLASSDECLARATION);
        match(TokenType.CLASS);

        classDeclaration.setTokenType(TokenType.IDENTIFIER);
        classDeclaration.setValue(currentValue);
        match(TokenType.IDENTIFIER);

        if (currentToken == TokenType.EXTENDS) {
            match(TokenType.EXTENDS);

            classDeclaration.setValue(classDeclaration.getValue() + "%" + currentValue);
            match(TokenType.IDENTIFIER);

        }
        match(TokenType.LBRACE);
        while (currentToken == TokenType.INT || currentToken == TokenType.BOOLEAN || currentToken == TokenType.IDENTIFIER) {
            classDeclaration.children.add(VarDeclaration());
        }
        while (currentToken == TokenType.PUBLIC) {
            classDeclaration.children.add(MethodDeclaration());
        }
        match(TokenType.RBRACE);
        return classDeclaration;
    }

    private static TreeNode VarDeclaration() {
        TreeNode varDeclaration = new TreeNode();
        varDeclaration.setStatement(Statement.VARDECLARATION);
        varDeclaration.children.add(Type());

        varDeclaration.setTokenType(TokenType.IDENTIFIER);
        varDeclaration.setValue(currentValue);
        match(TokenType.IDENTIFIER);

        match(TokenType.SEMICOLON);
        return varDeclaration;
    }

    private static TreeNode MethodDeclaration() {
        TreeNode methodDeclaration = new TreeNode();
        methodDeclaration.setStatement(Statement.METHODDECLARATION);
        match(TokenType.PUBLIC);
        methodDeclaration.children.add(Type());

        methodDeclaration.setTokenType(TokenType.IDENTIFIER);
        methodDeclaration.setValue(currentValue);
        match(TokenType.IDENTIFIER);

        match(TokenType.LBRACKET);
        if (currentToken == TokenType.INT || currentToken == TokenType.BOOLEAN || currentToken == TokenType.IDENTIFIER) {
            methodDeclaration.children.add(Type());

            methodDeclaration.setValue(methodDeclaration.getValue() + "%" + currentValue);
            match(TokenType.IDENTIFIER);

            while (currentToken == TokenType.COMMA) {
                match(TokenType.COMMA);
                methodDeclaration.children.add(Type());

                methodDeclaration.setValue(methodDeclaration.getValue() + "%" + currentValue);
                match(TokenType.IDENTIFIER);
            }
        }
        match(TokenType.RPAREN);
        match(TokenType.LBRACE);

        getNextWord();
        while (currentToken == TokenType.INT || currentToken == TokenType.BOOLEAN || (currentToken == TokenType.IDENTIFIER && nextToken == TokenType.IDENTIFIER)) {
            methodDeclaration.children.add(VarDeclaration());
            getNextWord();
        }
        getNextWord();
        while (currentToken == TokenType.LBRACE
                || currentToken == TokenType.IF
                || currentToken == TokenType.WHILE
                || currentToken == TokenType.SYSTEMOUTPRINTLN
                || (currentToken == TokenType.IDENTIFIER && nextToken == TokenType.EQUAL)
                || (currentToken == TokenType.IDENTIFIER && nextToken == TokenType.LBRACKET)) {
            methodDeclaration.children.add(Statement());
            getNextWord();
        }
        match(TokenType.RETURN);
        methodDeclaration.children.add(Expression());
        match(TokenType.SEMICOLON);
        match(TokenType.RBRACE);
        return methodDeclaration;
    }

    private static TreeNode Type() {
        TreeNode type = new TreeNode();
        type.setStatement(Statement.TYPE);
        getNextWord();
        if (currentToken == TokenType.INT && nextToken == TokenType.LBRACKET) {
            match(TokenType.INT);
            match(TokenType.LBRACKET);
            match(TokenType.RBRACKET);
        }
        if (currentToken == TokenType.BOOLEAN) {
            match(TokenType.BOOLEAN);
        }
        if (currentToken == TokenType.INT) {
            match(TokenType.INT);
        }
        if (currentToken == TokenType.IDENTIFIER) {
            type.setTokenType(TokenType.IDENTIFIER);
            type.setValue(currentValue);
            match(TokenType.IDENTIFIER);
        }
        return type;
    }

    private static TreeNode Statements() {
        TreeNode statements = new TreeNode();
        statements.setStatement(Statement.STATEMENTS);
        match(TokenType.LBRACE);
        getNextWord();
        while (currentToken == TokenType.LBRACE
                || currentToken == TokenType.IF
                || currentToken == TokenType.WHILE
                || currentToken == TokenType.SYSTEMOUTPRINTLN
                || (currentToken == TokenType.IDENTIFIER && nextToken == TokenType.EQUAL)
                || (currentToken == TokenType.IDENTIFIER && nextToken == TokenType.LBRACKET)) {
            statements.children.add(Statement());
            getNextWord();
        }
        match(TokenType.RBRACE);
        return statements;
    }

    private static TreeNode IfStatement() {
        TreeNode ifStatement = new TreeNode();
        ifStatement.setStatement(Statement.IFSTATEMENT);
        match(TokenType.IF);
        match(TokenType.LPAREN);
        ifStatement.children.add(Expression());
        match(TokenType.RPAREN);
        ifStatement.children.add(Statement());
        match(TokenType.ELSE);
        ifStatement.children.add(Statement());
        return ifStatement;
    }

    private static TreeNode WhileStatement() {
        TreeNode whileStatement = new TreeNode();
        whileStatement.setStatement(Statement.WHILESTATEMENT);
        match(TokenType.WHILE);
        match(TokenType.LPAREN);
        whileStatement.children.add(Expression());
        match(TokenType.RPAREN);
        whileStatement.children.add(Statement());
        return whileStatement;
    }

    private static TreeNode PrintStatement() {
        TreeNode printStatement = new TreeNode();
        printStatement.setStatement(Statement.PRINTSTATEMENT);
        match(TokenType.SYSTEMOUTPRINTLN);
        match(TokenType.RPAREN);
        printStatement.children.add(Expression());
        match(TokenType.RPAREN);
        match(TokenType.SEMICOLON);
        return printStatement;
    }

    private static TreeNode AssignStatement() {
        TreeNode assignStatement = new TreeNode();
        assignStatement.setStatement(Statement.ASSIGNSTATEMENT);

        assignStatement.setTokenType(TokenType.IDENTIFIER);
        assignStatement.setValue(currentValue);
        match(TokenType.IDENTIFIER);

        match(TokenType.EQUAL);
        assignStatement.children.add(Expression());
        match(TokenType.SEMICOLON);

        return assignStatement;
    }

    private static TreeNode ArrayStatement() {
        TreeNode arrayStatement = new TreeNode();
        arrayStatement.setStatement(Statement.ARRAYSTATEMENT);

        arrayStatement.setTokenType(TokenType.IDENTIFIER);
        arrayStatement.setValue(currentValue);
        match(TokenType.IDENTIFIER);

        match(TokenType.LBRACKET);
        arrayStatement.children.add(Expression());
        match(TokenType.RBRACKET);
        match(TokenType.EQUAL);
        arrayStatement.children.add(Expression());
        match(TokenType.SEMICOLON);

        return arrayStatement;
    }

    private static TreeNode Statement() {
        TreeNode statement = new TreeNode();
        statement.setStatement(Statement.STATEMENT);
        getNextWord();
        if (currentToken == TokenType.LBRACE) {
            statement.children.add(Statements());
        }
        if (currentToken == TokenType.IF) {
            statement.children.add(IfStatement());
        }
        if (currentToken == TokenType.WHILE) {
            statement.children.add(WhileStatement());
        }
        if (currentToken == TokenType.SYSTEMOUTPRINTLN) {
            statement.children.add(PrintStatement());
        }
        if (currentToken == TokenType.IDENTIFIER && nextToken == TokenType.EQUAL) {
            statement.children.add(AssignStatement());
        }
        if (currentToken == TokenType.IDENTIFIER && nextToken == TokenType.LBRACKET) {
            statement.children.add(AssignStatement());
        }
        return statement;
    }

    private static TreeNode IntExpression() {
        TreeNode intExpression = new TreeNode();
        intExpression.setStatement(Statement.INTEXPRESSION);

        intExpression.setTokenType(TokenType.INTEGERLITERAL);
        intExpression.setValue(currentValue);
        match(TokenType.INTEGERLITERAL);

        intExpression.children.add(L());

        return intExpression;
    }

    private static TreeNode TrueExpression() {
        TreeNode trueExpression = new TreeNode();
        trueExpression.setStatement(Statement.TRUEEXPRESSION);
        match(TokenType.TRUE);
        trueExpression.children.add(L());
        return trueExpression;
    }

    private static TreeNode FalseExpression() {
        TreeNode falseExpression = new TreeNode();
        falseExpression.setStatement(Statement.FALSEEXPRESSION);
        match(TokenType.FALSE);
        falseExpression.children.add(L());
        return falseExpression;
    }

    private static TreeNode IdentifierExpression() {
        TreeNode identifierExpression = new TreeNode();
        identifierExpression.setStatement(Statement.IDENTIFIEREXPRESSION);

        identifierExpression.setTokenType(TokenType.IDENTIFIER);
        identifierExpression.setValue(currentValue);
        match(TokenType.IDENTIFIER);

        identifierExpression.children.add(L());

        return identifierExpression;
    }

    private static TreeNode ThisExpression() {
        TreeNode thisExpression = new TreeNode();
        thisExpression.setStatement(Statement.THISEXPRESSION);
        match(TokenType.THIS);
        thisExpression.children.add(L());
        return thisExpression;
    }

    private static TreeNode NewArrayExpression() {
        TreeNode newArrayExpression = new TreeNode();
        newArrayExpression.setStatement(Statement.NEWARRAYEXPRESSION);
        match(TokenType.NEW);
        match(TokenType.INT);
        match(TokenType.LBRACKET);
        newArrayExpression.children.add(Expression());
        match(TokenType.RBRACKET);
        newArrayExpression.children.add(L());
        return newArrayExpression;
    }

    private static TreeNode NewExpression() {
        TreeNode newExpression = new TreeNode();
        newExpression.setStatement(Statement.NEWEXPRESSION);
        match(TokenType.NEW);

        newExpression.setTokenType(TokenType.IDENTIFIER);
        newExpression.setValue(currentValue);
        match(TokenType.IDENTIFIER);

        match(TokenType.LPAREN);
        match(TokenType.RPAREN);
        newExpression.children.add(L());

        return newExpression;
    }

    private static TreeNode NoExpression() {
        TreeNode noExpression = new TreeNode();
        noExpression.setStatement(Statement.NOEXPRESSION);
        match(TokenType.EXCLAMATION);
        noExpression.children.add(Expression());
        noExpression.children.add(L());
        return noExpression;
    }

    private static TreeNode BraceExpression() {
        TreeNode braceExpression = new TreeNode();
        braceExpression.setStatement(Statement.BRACEEXPREESION);
        match(TokenType.LPAREN);
        braceExpression.children.add(Expression());
        match(TokenType.RPAREN);
        braceExpression.children.add(L());
        return braceExpression;
    }

    private static TreeNode Expression() {
        TreeNode expression = new TreeNode();
        expression.setStatement(Statement.EXPRESSION);
        getNextWord();
        if (currentToken == TokenType.INTEGERLITERAL) {
            expression.children.add(IntExpression());
        }
        if (currentToken == TokenType.TRUE) {
            expression.children.add(TrueExpression());
        }
        if (currentToken == TokenType.FALSE) {
            expression.children.add(FalseExpression());
        }
        if (currentToken == TokenType.IDENTIFIER) {
            expression.children.add(IdentifierExpression());
        }
        if (currentToken == TokenType.THIS) {
            expression.children.add(ThisExpression());
        }
        if (currentToken == TokenType.NEW && nextToken == TokenType.INT) {
            expression.children.add(NewArrayExpression());
        }
        if (currentToken == TokenType.NEW && nextToken == TokenType.IDENTIFIER) {
            expression.children.add(NewExpression());
        }
        if (currentToken == TokenType.EXCLAMATION) {
            expression.children.add(NoExpression());
        }
        if (currentToken == TokenType.LPAREN) {
            expression.children.add(BraceExpression());
        }
        return expression;
    }

    private static TreeNode OPL() {
        TreeNode opL = new TreeNode();
        opL.setStatement(Statement.OPL);
        switch (currentToken) {
            case DOUBLEAND:
                match(TokenType.DOUBLEAND);
                break;
            case LESSTHEN:
                match(TokenType.LESSTHEN);
                break;
            case PLUS:
                match(TokenType.PLUS);
                break;
            case HYPHEN:
                match(TokenType.HYPHEN);
                break;
            case MULTIPLY:
                match(TokenType.MULTIPLY);
                break;
        }
        opL.children.add(Expression());
        opL.children.add(L());
        return opL;
    }

    private static TreeNode ExpressionL() {
        TreeNode expressionL = new TreeNode();
        expressionL.setStatement(Statement.EXPRESSIONL);
        match(TokenType.LBRACKET);
        expressionL.children.add(Expression());
        match(TokenType.RBRACKET);
        expressionL.children.add(L());
        return expressionL;
    }

    private static TreeNode LengthL() {
        TreeNode lengthL = new TreeNode();
        lengthL.setStatement(Statement.LENGTHL);
        match(TokenType.FULLSTOP);
        match(TokenType.LENGTH);
        lengthL.children.add(L());
        return lengthL;
    }

    private static TreeNode MethodL() {
        TreeNode methodL = new TreeNode();
        methodL.setStatement(Statement.METHODL);
        match(TokenType.FULLSTOP);

        methodL.setTokenType(TokenType.IDENTIFIER);
        methodL.setValue(currentValue);
        match(TokenType.IDENTIFIER);

        match(TokenType.LPAREN);
        getNextWord();
        if (currentToken == TokenType.INTEGERLITERAL
                || currentToken == TokenType.TRUE
                || currentToken == TokenType.FALSE
                || currentToken == TokenType.IDENTIFIER
                || currentToken == TokenType.THIS
                || currentToken == TokenType.NEW && nextToken == TokenType.INT
                || currentToken == TokenType.NEW && nextToken == TokenType.IDENTIFIER
                || currentToken == TokenType.EXCLAMATION
                || currentToken == TokenType.LPAREN) {
            methodL.children.add(Expression());
            while (currentToken == TokenType.COMMA) {
                match(TokenType.COMMA);
                methodL.children.add(Expression());
            }
        }
        match(TokenType.RPAREN);
        methodL.children.add(L());
        return methodL;
    }

    private static TreeNode NullL() {
        TreeNode nullL = new TreeNode();
        nullL.setStatement(Statement.NULLL);
        return nullL;
    }

    private static TreeNode L() {
        TreeNode L = new TreeNode();
        L.setStatement(Statement.L);
        getNextWord();
        if (currentToken == TokenType.DOUBLEAND
                || currentToken == TokenType.LESSTHEN
                || currentToken == TokenType.PLUS
                || currentToken == TokenType.HYPHEN
                || currentToken == TokenType.MULTIPLY) {
            L.children.add(OPL());
        } else if (currentToken == TokenType.LBRACKET) {
            L.children.add(ExpressionL());
        } else if (currentToken == TokenType.FULLSTOP && nextToken == TokenType.LENGTH) {
            L.children.add(LengthL());
        } else if (currentToken == TokenType.FULLSTOP && nextToken == TokenType.IDENTIFIER) {
            L.children.add(MethodL());
        } else {
            L.children.add(NullL());
        }
        return L;
    }

}



package com.company.wordAnalysis;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WordAnalysis {
    private static TokenType currentToken;
    private static State state = State.START;
    private static String rowInfo = new String();

    private static List<String> rowList = new ArrayList<>();
    private static List<String> errorList = new ArrayList<>();
    private static List<String> tokenList=new ArrayList<>();
    private static boolean isIdentifierLetter(char c) {
        if (('a' <= c && c <= 'z') || ('A' <= c && c <= 'Z')) {
            return true;
        } else {
            return false;
        }
    }

    private static boolean isDigit(char c) {
        if ('0' <= c && c <= '9') {
            return true;
        } else {
            return false;
        }
    }

    private static boolean isUnderline(char c) {
        if (c == '_') {
            return true;
        } else {
            return false;
        }
    }

    private static TokenType getReservedWord(String identifier) {
        TokenType reservedWord = TokenType.NULL;
        switch (identifier) {
            case "class":
                reservedWord = TokenType.CLASS;
                break;
            case "public":
                reservedWord = TokenType.PUBLIC;
                break;
            case "String":
                reservedWord = TokenType.STRING;
                break;
            case "static":
                reservedWord = TokenType.STATIC;
                break;
            case "void":
                reservedWord = TokenType.VOID;
                break;
            case "main":
                reservedWord = TokenType.MAIN;
                break;
            case "extends":
                reservedWord = TokenType.EXTENDS;
                break;
            case "return":
                reservedWord = TokenType.RETURN;
                break;
            case "int":
                reservedWord = TokenType.INT;
                break;
            case "boolean":
                reservedWord = TokenType.BOOLEAN;
                break;
            case "if":
                reservedWord = TokenType.IF;
                break;
            case "else":
                reservedWord = TokenType.ELSE;
                break;
            case "while":
                reservedWord = TokenType.WHILE;
                break;
            case "System.out.println":
                reservedWord = TokenType.SYSTEMOUTPRINTLN;
                break;
            case "length":
                reservedWord = TokenType.LENGTH;
                break;
            case "true":
                reservedWord = TokenType.TRUE;
                break;
            case "false":
                reservedWord = TokenType.FALSE;
                break;
            case "this":
                reservedWord = TokenType.THIS;
                break;
            case "new":
                reservedWord = TokenType.NEW;
                break;
            default:
                break;
        }
        return reservedWord;
    }

    public void handle(String info, int lineNum) {
        int start = 0;
        int currentPosition = 0;
        int infoLength = info.length();
        while (currentPosition < infoLength) {
            char c = info.charAt(currentPosition);
            switch (state) {
                case START:
                    start = currentPosition;
                    if (isIdentifierLetter(c)) {
                        state = State.IN;
                        currentToken = TokenType.IDENTIFIER;
                    } else if (isDigit(c)) {
                        state = State.DN;
                        currentToken = TokenType.INTEGERLITERAL;
                    } else {
                        switch (c) {
                            case '[':
                                currentToken = TokenType.LBRACKET;
                                state = State.DONE;
                                break;
                            case ']':
                                currentToken = TokenType.RBRACKET;
                                state = State.DONE;
                                break;
                            case '(':
                                currentToken = TokenType.LPAREN;
                                state = State.DONE;
                                break;
                            case ')':
                                currentToken = TokenType.RPAREN;
                                state = State.DONE;
                                break;
                            case '{':
                                currentToken = TokenType.LBRACE;
                                state = State.DONE;
                                break;
                            case '}':
                                currentToken = TokenType.RBRACE;
                                state = State.DONE;
                                break;
                            case ',':
                                currentToken = TokenType.COMMA;
                                state = State.DONE;
                                break;
                            case ';':
                                currentToken = TokenType.SEMICOLON;
                                state = State.DONE;
                                break;
                            case '=':
                                currentToken = TokenType.EQUAL;
                                state = State.DONE;
                                break;
                            case '<':
                                currentToken = TokenType.LESSTHEN;
                                state = State.DONE;
                                break;
                            case '+':
                                currentToken = TokenType.PLUS;
                                state = State.DONE;
                                break;
                            case '-':
                                currentToken = TokenType.HYPHEN;
                                state = State.DONE;
                                break;
                            case '*':
                                currentToken = TokenType.MULTIPLY;
                                state = State.DONE;
                                break;
                            case '.':
                                currentToken = TokenType.FULLSTOP;
                                state = State.DONE;
                                break;
                            case '!':
                                currentToken = TokenType.EXCLAMATION;
                                state = State.DONE;
                                break;
                            case '&':
                                currentToken = TokenType.AND;
                                state = State.AN;
                                break;
                            case ' ':
                                currentToken = TokenType.SPACE;
                                state = State.START;
                                break;
                            case '\t':
                                currentToken = TokenType.TABLE;
                                state = State.START;
                                break;
                            case '\n':
                                currentToken = TokenType.ENTER;
                                state = State.START;
                                break;
                            default:
                                state = State.DONE;
                                currentToken = TokenType.ERROR;
                                String thisError = "Line" + lineNum + ":" + info.substring(0, info.length()) + "%"
                                        + currentPosition + "%" + "invalid symbol'" + c + "'.";
                                errorList.add(thisError);
                                tokenList.add(c+","+TokenType.ERROR);
                        }
                    }
                    break;
                case DN:
                    if(isDigit(c)){
                        state=State.DN;
                        currentToken=TokenType.INTEGERLITERAL;
                    }else {
                        state=State.DONE;
                    }
                    break;
                case IN:
                    if (isIdentifierLetter(c) || isDigit(c)) {
                        state = State.IN;
                        currentToken = TokenType.IDENTIFIER;
                    } else if (isUnderline(c)) {
                        state = State.UN;
                        currentToken = TokenType.IDENTIFIER;
                    } else {
                        state = State.DONE;
                    }
                    break;
                case UN:
                    if (isIdentifierLetter(c) || isDigit(c)) {
                        state = State.IN;
                        currentToken = TokenType.IDENTIFIER;
                    } else {
                        state = State.DONE;
                        currentToken = TokenType.ERROR;
                        String thisError = "Line " + lineNum + ": " + info.substring(0, info.length()) + "%"
                                + currentPosition + "%" + "invalid symbol'" + c + "',there should be a letter or a digit.";
                        errorList.add(thisError);
                        tokenList.add(info.substring(start,currentPosition+1)+","+TokenType.ERROR);
                    }
                    break;
                case AN:
                    if (currentToken == TokenType.AND && c == '&') {
                        state = State.DONE;
                        currentToken = TokenType.DOUBLEAND;
                    } else {
                        state = State.DONE;
                        currentToken = TokenType.ERROR;
                        //注意回退
                        currentPosition--;
                        String thisError = "Line " + lineNum + ": " +
                                info.substring(0, info.length()) +
                                "%" + (currentPosition + 1) + "%" +
                                "invalid symbol'" + c + "',there should be &";
                        errorList.add(thisError);
                        tokenList.add(info.substring(start,currentPosition+1)+","+TokenType.ERROR);
                    }
                    break;
                case DONE:
                    break;
                default:
                    break;
            }

            if (state == State.DONE) {
                if (currentToken == TokenType.IDENTIFIER) {
                    String tempIdentifier = info.substring(start, currentPosition);
                    if(tempIdentifier.equals("System")){
                        if(info.substring(start,currentPosition+12).equals("System.out.println")){
                            currentToken=TokenType.SYSTEMOUTPRINTLN;
                            currentPosition=currentPosition+12;
                        }
                    }
                    if (getReservedWord(tempIdentifier) != TokenType.NULL) {
                        currentToken = getReservedWord(tempIdentifier);
                    }
                    tokenList.add(info.substring(start,currentPosition)+","+currentToken);
                    rowInfo = rowInfo + currentToken + " ";
                    currentPosition--;
                } else if (currentToken == TokenType.INTEGERLITERAL) {
                    tokenList.add(info.substring(start,currentPosition)+","+currentToken);
                    rowInfo = rowInfo + currentToken + " ";
                    currentPosition--;
                } else if (currentToken == TokenType.ERROR) {
                    currentPosition = info.length();//直接结束
                    rowInfo = rowInfo + "第" + lineNum + "行发生错误！";
                } else {
                    tokenList.add(info.substring(start,currentPosition+1)+","+currentToken);
                    rowInfo = rowInfo + currentToken + " ";
                }
                state = State.START;
            }
            currentPosition++;
        }
        rowList.add(rowInfo);
        rowInfo = new String();

    }

    public void output(String outputFilename) throws IOException {
        File outputFile = new File(outputFilename);
        FileOutputStream out = new FileOutputStream(outputFile, false);
        for (String temp : tokenList) {
            out.write((temp + "\r\n").getBytes("utf-8"));
        }
        out.write(("end,EOF" + "\r\n").getBytes("utf-8"));
        out.close();
    }

    public void errorOutput(String outputFilename) throws IOException {
        File outputFile = new File(outputFilename);
        FileOutputStream error = new FileOutputStream(outputFile, false);
        for (String temp : errorList) {
            String[] errorInfo = temp.split("%");
            //添加指示标
            String[] pointInfo = errorInfo[0].split(":");
            String pointPos = new String();
            int point = Integer.parseInt(errorInfo[1]) + pointInfo[0].length() + 2;
            for(int i = 0; i < point; i++) {
                if(errorInfo[0].charAt(i) != '\t') {
                    pointPos = pointPos + " ";
                } else {
                    pointPos = pointPos + "\t";
                }
            }
            pointPos = pointPos + "^";

            error.write((" "+errorInfo[0].substring(0, point + 1) + "\r\n").getBytes("utf-8"));
            error.write((pointPos + "\r\n").getBytes("utf-8"));
            error.write(("\tError: " + errorInfo[2] + "(At Position: " + (Integer.parseInt(errorInfo[1]) + 1) + ".)" + "\r\n").getBytes("utf-8"));
            error.write(("\r\n").getBytes("utf-8"));
        }
        error.close();
    }
}

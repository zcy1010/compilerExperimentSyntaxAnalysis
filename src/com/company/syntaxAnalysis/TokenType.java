package com.company.syntaxAnalysis;

/**
 * 词的类型
 *
 * @author zcy10
 * @version 1.0
 */
public enum TokenType {
    //类型
    IDENTIFIER,         //标识符
    INTEGERLITERAL,     //整数

    //保留字
    CLASS,
    PUBLIC,
    STATIC,
    VOID,
    MAIN,
    STRING,
    EXTENDS,
    RETURN,
    INT,
    BOOLEAN,
    IF,
    ELSE,
    WHILE,
    SYSTEMOUTPRINTLN,
    LENGTH,
    TRUE,
    FALSE,
    THIS,
    NEW,


    //专有符号
//    [ ]    ( )  { }	  ,	 ;   =     &&
//    <  +  -  *  .    !
    /**
     * [
     */
    LBRACKET,//[
    /**
     * ]
     */
    RBRACKET,//]
    /**
     * (
     */
    LPAREN,//(
    /**
     * )
     */
    RPAREN,//)
    /**
     * {
     */
    LBRACE,//{
    /**
     * }
     */
    RBRACE,//}
    /**
     * ,
     */
    COMMA,//,
    /**
     * ;
     */
    SEMICOLON,//;
    /**
     * =
     */
    EQUAL,//=
    /**
     * &&
     */
    DOUBLEAND,//&&
    /**
     * &
     */
    AND,
    /**
     * <
     */
    LESSTHEN,//<
    /**
     * +
     */
    PLUS,//+
    /**
     * -
     */
    HYPHEN,//-
    /**
     * *
     */
    MULTIPLY,//*
    /**
     * .
     */
    FULLSTOP,
    /**
     * !
     */
    EXCLAMATION,//!


    //空白符
    SPACE,
    TABLE,
    ENTER,

    EOF,
    ERROR,
    NULL,

}

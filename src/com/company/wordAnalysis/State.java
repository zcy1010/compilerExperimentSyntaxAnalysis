package com.company.wordAnalysis;

public enum State {


    //开始
    START,          //START


    //整数
    DN,             //Digit Next


    //标识符
    IN,             //Identifier Letter Next
    UN,             //Underline Next


    //专有符号中间状态
    AN,             //And Next
    //结束


    DONE,           //Done


}

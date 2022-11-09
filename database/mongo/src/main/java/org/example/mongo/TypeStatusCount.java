package org.example.mongo;

/**
 * @Author: JDragon
 * @Data:2022/11/7 20:54
 * @Description:
 */
public class TypeStatusCount {

    private Long userId;

    private int count;

    private int codeRight;

    private int wordRight;

    private int codeWrong;

    private int wordWrong;

    private int perfect;


    public TypeStatusCount(long userId, int count, int codeRight, int wordRight, int codeWrong, int wordWrong, int perfect) {
        this.userId = userId;
        this.count = count;
        this.codeRight = codeRight;
        this.wordRight = wordRight;
        this.codeWrong = codeWrong;
        this.wordWrong = wordWrong;
        this.perfect = perfect;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getCodeRight() {
        return codeRight;
    }

    public void setCodeRight(int codeRight) {
        this.codeRight = codeRight;
    }

    public int getWordRight() {
        return wordRight;
    }

    public void setWordRight(int wordRight) {
        this.wordRight = wordRight;
    }

    public int getCodeWrong() {
        return codeWrong;
    }

    public void setCodeWrong(int codeWrong) {
        this.codeWrong = codeWrong;
    }

    public int getWordWrong() {
        return wordWrong;
    }

    public void setWordWrong(int wordWrong) {
        this.wordWrong = wordWrong;
    }

    public int getPerfect() {
        return perfect;
    }

    public void setPerfect(int perfect) {
        this.perfect = perfect;
    }
}

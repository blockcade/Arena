package net.blockcade.pit.Enums;

public class Levels {
    public static class level {
        int level = 0;
        int req_exp = 0;
        char chatcolor = 'f';
        public level(int level, char ChatColor){this.level=level;this.chatcolor=ChatColor;}
        public level(int level, char ChatColor,int req_exp){this.level=level;this.chatcolor=ChatColor;this.req_exp=req_exp;}

        public int getLevel() {
            return level;
        }

        public int getReq_exp() {
            return req_exp;
        }

        public char getChatcolor() {
            return chatcolor;
        }
    }
    public static level getLevel(int level, int exp){
        int i = (int) (exp+(Math.sin(level)*15));
        System.out.println(level+" / "+exp+" / "+i);
        return new level(level,'f', (int) (exp+(Math.sin(level)*75)));
    }
}
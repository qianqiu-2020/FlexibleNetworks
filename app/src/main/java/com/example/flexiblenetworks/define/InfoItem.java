package com.example.flexiblenetworks.define;
/*聊天对象类，记录好友相关信息
* 如昵称，头像等...
* */
public class InfoItem {
    private String title;
    private String reply;


    public InfoItem(String title, String reply) {
        this.title = title;
        this.reply = reply;
    }

    public String getTitle() {
        return title;
    }

    public String getReply() {
        return reply;
    }
}

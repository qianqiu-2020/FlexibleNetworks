package com.example.flexiblenetworks.define;
/*好友类，记录好友相关信息
* 如昵称，头像等...
* */
public class Friend {
    private String name;
    private int imageId;
    private long id;
    private String ip;
    private int port;
    public Friend(String name,int imageId)
    {
        this.name=name;
        this.imageId=imageId;
    }
    public Friend(long id, String name, int imageId)
    {
        this.id = id;
        this.name = name;
        this.imageId = imageId;
    }
    public Friend(long id,String name,int imageId,String ip,int port)
    {
        this.id=id;
        this.name=name;
        this.imageId=imageId;
        this.ip=ip;
        this.port=port;
    }
    public String getName()
    {
        return name;
    }
    public int getImageId()
    {
        return imageId;
    }
    public String getIp()
    {
        return ip;
    }
    public long getId()
    {
        return id;
    }
    public int getPort()
    {
        return port;
    }
}

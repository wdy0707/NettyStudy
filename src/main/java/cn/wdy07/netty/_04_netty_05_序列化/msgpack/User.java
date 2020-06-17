package cn.wdy07.netty._04_netty_05_序列化.msgpack;

import org.msgpack.annotation.Message;

/**
 * @author wdy
 * @create 2020-06-17 15:38
 */
@Message //MessagePack提供的注解，表明这是一个需要序列化的实体类
public class User {

    private String id;
    private String name;
    private int age;
    private Contact contact;

    //一定保留一个空构造！
    public User() {
    }

    public User(String id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", contact=" + contact +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }
}

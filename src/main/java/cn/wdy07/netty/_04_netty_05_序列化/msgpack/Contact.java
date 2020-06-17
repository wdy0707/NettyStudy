package cn.wdy07.netty._04_netty_05_序列化.msgpack;

import org.msgpack.annotation.Message;

/**
 * @author wdy
 * @create 2020-06-17 15:35
 */
@Message //MessagePack提供的注解，表明这是一个需要序列化的实体类
public class Contact {

    private String phone;
    private String email;

    public Contact() {
    }

    public Contact(String phone, String email) {
        this.phone = phone;
        this.email = email;
    }

    @Override
    public String toString() {
        return "Contact{" +
                "phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

package cn.itcast.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@AllArgsConstructor
public class LoginRequestMessage extends Message {
    public String username;
    public String password;



    @Override
    public int getMessageType() {
        return LoginRequestMessage;
    }
}

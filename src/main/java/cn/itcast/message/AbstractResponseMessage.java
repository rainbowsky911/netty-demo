package cn.itcast.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public abstract class AbstractResponseMessage extends Message {
    public boolean success;
    public String reason;


}

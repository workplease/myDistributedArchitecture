package zzz.ares.remoting.framework.serialization.common;

import com.fasterxml.jackson.databind.JsonDeserializer;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.DeserializationContext;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Author: milkteazzz
 * @Data: 2020-12-06 11:52
 * @Version: 1.0
 *
 * 对日期类进行反序列化
 */
public class FDateJsonDeserializer extends JsonDeserializer<Date> {

    @Override
    public Date deserialize(com.fasterxml.jackson.core.JsonParser jsonParser, com.fasterxml.jackson.databind.DeserializationContext deserializationContext) throws IOException, com.fasterxml.jackson.core.JsonProcessingException {
        String date = jsonParser.getText();
        if (StringUtils.isEmpty(date)){
            return null;
        }
        if (StringUtils.isNumeric(date)){
            return new Date(Long.valueOf(date));
        }
        try{
            SimpleDateFormat format = new SimpleDateFormat("yyyy--MM-dd HH:mm;ss");
            return format.parse(date);
        }catch (Exception e){
            throw new IOException(e);
        }
    }
}

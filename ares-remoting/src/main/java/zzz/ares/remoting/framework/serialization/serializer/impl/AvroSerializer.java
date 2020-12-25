package zzz.ares.remoting.framework.serialization.serializer.impl;


import org.apache.avro.specific.SpecificRecordBase;
import zzz.ares.remoting.framework.serialization.serializer.ISerializer;
import org.apache.avro.io.*;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.avro.specific.SpecificDatumWriter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * @Author: milkteazzz
 * @Data: 2020-12-11 15:08
 * @Version: 1.0
 *
 * Avro 实现序列化/反序列化
 */
public class AvroSerializer implements ISerializer {

    @Override
    public <T> byte[] serialize(T obj) {
        try {
            if (!(obj instanceof SpecificRecordBase)){
                throw new UnsupportedOperationException("not supported obj type");
            }
            DatumWriter userDatumWriter = new SpecificDatumWriter(obj.getClass());
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BinaryEncoder binaryEncoder = EncoderFactory.get().directBinaryEncoder(outputStream, null);
            userDatumWriter.write(obj, binaryEncoder);
            return outputStream.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T> T deserialize(byte[] data, Class<T> clazz) {
        try {
            if (!SpecificRecordBase.class.isAssignableFrom(clazz)){
                throw new UnsupportedOperationException("not supported obj type");
            }
            DatumReader userDatumReader = new SpecificDatumReader(clazz);
            BinaryDecoder binaryDecoder = DecoderFactory.get().directBinaryDecoder(new ByteArrayInputStream(data), null);
            return (T) userDatumReader.read(clazz.newInstance(), binaryDecoder);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }


}

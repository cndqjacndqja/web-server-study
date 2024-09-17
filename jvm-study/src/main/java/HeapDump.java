import java.util.ArrayList;
import java.util.List;

public class HeapDump {

    static class OOMObject {
    }

    public static void main(String[] args) {
        List<OOMObject> list = new ArrayList<>();
        while (true) {
            list.add(new OOMObject());
        }
    }
}

/**
* 자바 VM 옵션 -Xms20m -Xmx20m -XX:+HeapDumpOnOutOfMemoryError -XX:+ShowCodeDetailsInExceptionMessages*/

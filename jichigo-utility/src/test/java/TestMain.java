import java.io.File;
import java.io.IOException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import org.jichigo.utility.regex.PatternCache;
import org.jichigo.utility.text.DatePattern;
import org.jichigo.utility.timer.StopWatch;
import org.jichigo.utility.xml.validation.SchemaCache;
import org.xml.sax.SAXException;

public class TestMain {

    public static void main(String[] args) throws SAXException, IOException, JAXBException {
        JAXBContext c = null;

        DatePattern.getPattern("yyyyMMdd");
        DatePattern.getPattern("yyyyMMd");
        DatePattern.getPattern("yyyyMd");

        StopWatch sw = StopWatch.newInstance();

        sw.start();

        DatePattern.getPattern("yyyy/MM/dd HH:mm:ss.SSS");
        sw.split();

        DatePattern.getPattern("yyyy/MM/dd HH:mm:ss.SSS");
        sw.split();

        DatePattern.getPattern("yyyy/MM/dd HH:mm:ss.SSS");
        sw.split();

        DatePattern.getPattern("yyyy/MM/dd HH:mm:ss.SSS");
        sw.split();

        DatePattern.getPattern("yyyy/MM/dd HH:mm:ss.SSS");
        sw.split();

        DatePattern.getPattern("yyyy/MM/dd HH:mm:ss.SS");
        sw.split();

        sw.stop();

        sw.print();
    }

}

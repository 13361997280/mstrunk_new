package kafka;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.qbao.search.logging.ESLogger;
import com.qbao.search.logging.Loggers;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;
import kafka.serializer.StringEncoder;

public class KafkaProduce {
	
	private static KafkaProduce kafkaService;
	
	private static Properties properties;
	
    private final Producer<String, String> producer = new Producer<String, String>(new ProducerConfig(properties));
    
	private static final ESLogger logger = Loggers.getLogger(KafkaProduce.class);

	public static final KafkaProduce getInstance(){
		try {
			if (kafkaService == null) {
				synchronized (KafkaProduce.class) {
					properties = new Properties();
					String path = System.getProperty("user.dir") + "/conf/qbao/kafka.properties";
					try {
						FileInputStream fis = new FileInputStream(new File(path));
						properties.load(fis);
						properties.put("serializer.class", StringEncoder.class.getName());
					} catch (Exception e) {
						logger.error(e.toString());
						e.printStackTrace();
					}
					kafkaService = new KafkaProduce();
				}
			}
		}catch (Exception ex){
			ex.printStackTrace();
		}
		return kafkaService;
	}

	/**
	 * 发送单条数据
	 * @param data
	 */
	public void sendSingleData(String topic, String data) {		
		try {
			producer.send(new KeyedMessage<String, String>(topic, data));
		} catch (Exception e) {
			logger.error("kafka:sendSingleData" + e.toString());
			e.printStackTrace();			
		}

	}
	
	/**
	 * 发送多条数据
	 * @param data
	 */
	public void sendListData(String topic, List<String> dataList) {	
		if (null == dataList || dataList.size() <= 0) {
			return;
		}
		List<KeyedMessage<String, String>>  list = new ArrayList<KeyedMessage<String, String>>();
		for (String data : dataList) {
			list.add(new KeyedMessage<String, String>(topic, data));
		}
		try {			
			producer.send(list);
		} catch (Exception e) {
			logger.error("kafka:sendListData" + e.toString());
			e.printStackTrace();			
		}		
	}

	public static void main(String args[]) {
    	KafkaProduce producer = new KafkaProduce();   	
	    //producer.sendSingleData("111");	
	    List<String> dataList = new ArrayList<String>();
	    dataList.add("1");
	    dataList.add("2");
	    producer.sendListData("lbs", dataList);
	    System.out.println("success");
    }
}

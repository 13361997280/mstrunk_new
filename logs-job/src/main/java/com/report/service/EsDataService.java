package com.report.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.report.dto.DataDTO;
import com.utils.DateUtils;
import com.utils.HttpUtils;
import org.apache.commons.lang.StringUtils;

import java.io.*;
import java.util.*;

/**
 * @author song.j
 * @create 2017-08-09 09:09:36
 **/
public class EsDataService {

    public static final String url = "http://192.168.7.214:21037/qbaolog/_search";

    public String sendEs(Integer sec) {

        JSONObject jsonObject = new JSONObject();


        String param = "{\"query\":{\"bool\":{\"must\":[{\"range\":{\"stamp\":{\"gt\":\"%s\"," +
                "\"lt\":\"%s\"}}}]}},\"aggs\":{\"productId\":{\"terms\":{\"field\":\"productId\",\"order\": [\n" +
                "          {\n" +
                "            \"_term\": \"asc\"\n" +
                "          }\n" +
                "        ],\"size\":20}," +
                "\"aggs\":{\t\"uniq_streets\":{\"cardinality\":{\"field\":\"uid\"}}}}},\"size\":0}";
        long startSec = 0;
        long endSec = 0;
        if (sec == 0 ){
            endSec = 24 * 3600000;
        }else {
            startSec = (sec - 1) * 3600000;
            endSec = sec * 3600000;
        }
//        long currDate = DateUtils.getCurrentDate(new Date());
        long yestDate = DateUtils.getCurrentDate(DateUtils.getYestDate());

        param = String.format(param, yestDate + startSec, yestDate + endSec);

        jsonObject = JSON.parseObject(param);

        try {
            System.out.println(JSON.parse(param));
            String response = HttpUtils.postByBody(url, jsonObject);
//            System.out.println(response);
            return response;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public List<DataDTO> handPvUvData(Integer sec) {

        String response = sendEs(sec);

        Map resultMap = JSON.parseObject(response, Map.class);

        JSONObject root = (JSONObject) resultMap.get("aggregations");

        JSONObject elmRoot = (JSONObject) root.get("productId");

        JSONArray buckRoot = (JSONArray) elmRoot.get("buckets");


        List<DataDTO> resultList = new ArrayList<>();

        for (Object buck : buckRoot) {
            JSONObject proObject = (JSONObject) buck;
            //uv
            Integer uv = Integer.valueOf(((JSONObject) proObject.get("uniq_streets")).get("value").toString());
            //pv
            Integer pv = Integer.valueOf(proObject.get("doc_count").toString());
            //productId
            Integer productId = Integer.valueOf(proObject.get("key").toString());

            resultList.add(new DataDTO(productId, pv, uv));
        }

        return resultList;
    }

    public String getContent(){

        StringBuffer content = new StringBuffer();
        content.append("hi all:");
        content.append("\n");
        content.append("    附件是");
        Date cur = new Date();
        System.out.println(cur);
        content.append(DateUtils.getDateByFormat(DateUtils.getYestDate(),"MM") + "月"+DateUtils.getDateByFormat(DateUtils.getYestDate(),
                "dd")+"日");
        content.append("oneday项目pv/uv");

        return  content.toString();
    }

    public String getFilePath() {
        StringBuffer title = new StringBuffer();
        StringBuffer headDate = new StringBuffer();
        StringBuffer headPvUv = new StringBuffer();
        StringBuffer newsData = new StringBuffer("新闻资讯");
        StringBuffer weatData = new StringBuffer("天气预报");
        StringBuffer shopData = new StringBuffer("购物（有好货)");
        StringBuffer baoyData = new StringBuffer("宝约");
        StringBuffer signData = new StringBuffer("签到");
        StringBuffer searchData = new StringBuffer("问卷调查");
        StringBuffer expressData = new StringBuffer("快递");
        StringBuffer ratesData = new StringBuffer("汇率");
        StringBuffer goodThingsData = new StringBuffer("我有好物");

        initTitle(title);
        initHeadDate(headDate);
        initHeadPvUv(headPvUv);

        Map<Integer, StringBuffer> dataMap = new HashMap();
        dataMap.put(10101, newsData);
        dataMap.put(10102, weatData);
        dataMap.put(10103, shopData);
        dataMap.put(10104, baoyData);
        dataMap.put(10105, signData);
        dataMap.put(10106, searchData);
        dataMap.put(10107, expressData);
        dataMap.put(10108, ratesData);
        dataMap.put(10109, goodThingsData);

        //封装数据24小时的pv / uv数据
        for (int i = 0; i < 25; i++) {
            //每小时取一次数据
            List<DataDTO> dataList = handPvUvData(i );
            for (DataDTO dto : dataList) {

                for (Integer key : dataMap.keySet()) {
                    if (key.equals(dto.getProductId())) {
                        dataMap.get(key).append(",");
                        dataMap.get(key).append(dto.getPv());
                        dataMap.get(key).append(",");
                        dataMap.get(key).append(dto.getUv());

                    }
                }

                if (dto.getProductId() == 10107){
                    dataMap.get(10107).append(",0,0");
                }
            }
        }

        StringBuffer allData = new StringBuffer();
        for (Integer key : dataMap.keySet()) {
            if (StringUtils.isNotEmpty(dataMap.get(key).toString())) {
                allData.append(dataMap.get(key));
                allData.append("\n");
            }
        }

        title.append(headDate);
        title.append("\n");
        title.append(headPvUv);
        title.append("\n");
        title.append(allData);

        String content = title.toString();
        String filePath = exportFile(content);
        return filePath;
    }

    /**
     * 表格头
     *
     * @param title
     */
    public void initTitle(StringBuffer title) {


    }

    /**
     * 表格时间
     *
     * @param headDate
     */
    public void initHeadDate(StringBuffer headDate) {
        headDate.append(",");
        headDate.append("1天");
        headDate.append(",,");
        for (int i = 1; i < 25; i++) {
            headDate.append(i);
            headDate.append("h");
            headDate.append(",,");
        }
        headDate.append(",");
    }


    /**
     * 表格PV/UV头信息
     *
     * @param headPvUv
     */
    public void initHeadPvUv(StringBuffer headPvUv) {
        headPvUv.append("模块名称");

        for (int i = 1; i < 26; i++) {
            headPvUv.append(",");
            headPvUv.append("pv");
            headPvUv.append(",");
            headPvUv.append("uv");
        }
        headPvUv.append(",");
    }

    public String exportFile(String filecontent){

        String filenameTemp = System.getProperty("user.dir")+"/file/"+"oneday报表.csv";//文件路径+名称+文件类型
        File file = new File(filenameTemp);
        try {
            //如果文件不存在，则创建新的文件
            if(!file.exists()){
                file.createNewFile();
                System.out.println("success create file,the file is "+filenameTemp);
                //创建文件成功后，写入内容到文件里
                writeFileContent(filenameTemp, filecontent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return filenameTemp;
    }

    /**
     * 向文件中写入内容
     * @param filepath 文件路径与名称
     * @param newstr  写入的内容
     * @return
     * @throws IOException
     */
    public static boolean writeFileContent(String filepath,String newstr) throws IOException{
        Boolean bool = false;
        String filein = newstr+"\r\n";//新写入的行，换行
//        String temp  = "";

//        FileInputStream fis = null;
//        InputStreamReader isr = null;
//        BufferedReader br = null;
        FileOutputStream fos  = null;
        PrintWriter pw = null;
        OutputStreamWriter outstream = null;
        try {
            File file = new File(filepath);//文件路径(包括文件名称)
            //将文件读入输入流
//            fis = new FileInputStream(file);
//            isr = new InputStreamReader(fis);
//            br = new BufferedReader(isr);
//            StringBuffer buffer = new StringBuffer();

            //文件原有内容
//            for(int i=0;(temp =br.readLine())!=null;i++){
//                buffer.append(temp);
//                 行与行之间的分隔符 相当于“\n”
//                buffer = buffer.append(System.getProperty("line.separator"));
//            }
//            buffer.append(filein);

            fos = new FileOutputStream(file);
            outstream = new OutputStreamWriter(fos, "UTF-8");
            pw = new PrintWriter(outstream);
            pw.write(new String(newstr.getBytes(),"utf-8"));
            pw.flush();
            bool = true;
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }finally {
            //不要忘记关闭
            if (pw != null) {
                pw.close();
            }
            if (fos != null) {
                fos.close();
            }
            if (outstream!=null){
                outstream.close();
            }
        }
        return bool;
    }
}

package tfidf;

/**
 * Created by Administrator on 2015/6/23.
 */

import jeasy.analysis.MMAnalyzer;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReadFiles {
    //定义文件列表对象
    private static List<String> fileList = new ArrayList<String>();
    //定义分词词频lv map散列对象
    private static HashMap<String, HashMap<String, Float>> allTheTf = new HashMap<String, HashMap<String, Float>>();
    //定义分词次数  map散列对象
    private static HashMap<String, HashMap<String, Integer>> allTheNormalTF = new HashMap<String, HashMap<String, Integer>>();

    public static List<String> readDirs(String filepath) throws FileNotFoundException, IOException {
        try {
            File file = new File(filepath);
            if (!file.isDirectory()) {
                System.out.println("xxxxxx");
                System.out.println("filepath: " + file.getAbsolutePath());
            } else if (file.isDirectory()) {
                String[] filelist = file.list();
                for (int i = 0; i < filelist.length; i++) {
                    File readfile = new File(filepath + "\\" + filelist[i]);
                    if (!readfile.isDirectory()) {
                        //System.out.println("filepath: " + readfile.getAbsolutePath());
                        fileList.add(readfile.getAbsolutePath());
                    } else if (readfile.isDirectory()) {
                        readDirs(filepath + "\\" + filelist[i]);
                    }
                }
            }

        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
        return fileList;
    }

    public static String readFiles(String file) throws FileNotFoundException, IOException {
        StringBuffer sb = new StringBuffer();
        InputStreamReader is = new InputStreamReader(new FileInputStream(file), "gbk");
        BufferedReader br = new BufferedReader(is);
        String line = br.readLine();
        while (line != null) {
            sb.append(line).append("\r\n");
            line = br.readLine();
        }
        br.close();
        return sb.toString();
    }
//分词
    public static String[] cutWord(String file) throws IOException {
        String[] cutWordResult = null;
        String text = ReadFiles.readFiles(file);
        MMAnalyzer analyzer = new MMAnalyzer();
        //System.out.println("file content: "+text);
        //System.out.println("cutWordResult: "+analyzer.segment(text, " "));
        String tempCutWordResult = analyzer.segment(text, " ");
        cutWordResult = tempCutWordResult.split(" ");
        return cutWordResult;
    }
 // 求每个分词的TF
    public static HashMap<String, Float> tf(String[] cutWordResult) {
        HashMap<String, Float> tf = new HashMap<String, Float>();//正规化
        int wordNum = cutWordResult.length;
        int wordtf = 0;
        for (int i = 0; i < wordNum; i++) {
            wordtf = 0;
            for (int j = 0; j < wordNum; j++) {
                if (cutWordResult[i] != " " && i != j) {
                    if (cutWordResult[i].equals(cutWordResult[j])) {
                        cutWordResult[j] = " ";
                        wordtf++;
                    }
                }
            }
            if (cutWordResult[i] != " ") {
                tf.put(cutWordResult[i], (new Float(++wordtf)) / wordNum);
                cutWordResult[i] = " ";
            }
        }
        return tf;
    }
//分词计数
    public static HashMap<String, Integer> normalTF(String[] cutWordResult) {
        HashMap<String, Integer> tfNormal = new HashMap<String, Integer>();//没有正规化
        int wordNum = cutWordResult.length;
        int wordtf = 0;
        for (int i = 0; i < wordNum; i++) {
            wordtf = 0;
            if (cutWordResult[i] != " ") {
                for (int j = 0; j < wordNum; j++) {
                    if (i != j) {
                        if (cutWordResult[i].equals(cutWordResult[j])) {
                            cutWordResult[j] = " ";
                            wordtf++;

                        }
                    }
                }
                tfNormal.put(cutWordResult[i], ++wordtf);
                cutWordResult[i] = " ";
            }
        }
        return tfNormal;
    }

    public static Map<String, HashMap<String, Float>> tfOfAll(String dir) throws IOException {
        List<String> fileList = ReadFiles.readDirs(dir);
        for (String file : fileList) {
            HashMap<String, Float> dict = new HashMap<String, Float>();
            dict = ReadFiles.tf(ReadFiles.cutWord(file));
            allTheTf.put(file, dict);
        }
        return allTheTf;
    }
 //对应每个分词的次数
    public static Map<String, HashMap<String, Integer>> NormalTFOfAll(String dir) throws IOException {
        List<String> fileList = ReadFiles.readDirs(dir);
        for (int i = 0; i < fileList.size(); i++) {
            HashMap<String, Integer> dict = new HashMap<String, Integer>();
            dict = ReadFiles.normalTF(ReadFiles.cutWord(fileList.get(i)));
            allTheNormalTF.put(fileList.get(i), dict);
        }
        return allTheNormalTF;
    }
//求分词对应的idf
    public static Map<String, Float> idf(String aaaa) throws FileNotFoundException, UnsupportedEncodingException, IOException {
        //公式IDF＝log((1+|D|)/|Dt|)，其中|D|表示文档总数，|Dt|表示包含关键词t的文档数量。
        Map<String, Float> idf = new HashMap<String, Float>();
        List<String> located = new ArrayList<String>();

        float Dt = 1;
        float D = allTheNormalTF.size();//文档总数
        List<String> key = fileList;//存储各个文档名的List
        Map<String, HashMap<String, Integer>> tfInIdf = allTheNormalTF;//存储各个文档tf的Map

        for (int i = 0; i < D; i++) {
            HashMap<String, Integer> temp = tfInIdf.get(key.get(i));
            for (String word : temp.keySet()) {
                Dt = 1;
                if (!(located.contains(word))) {
                    for (int k = 0; k < D; k++) {
                        if (k != i) {
                            HashMap<String, Integer> temp2 = tfInIdf.get(key.get(k));
                            if (temp2.keySet().contains(word)) {
                                located.add(word);
                                Dt = Dt + 1;
                                continue;
                            }
                        }
                    }
                    idf.put(word, Log.log((1 + D) / Dt, 10));//调用log方法计算idf并放入map中
                }
            }
        }
        return idf;
    }
//这一步是将分词加权啦
    public static Map<String, HashMap<String, Float>> tfidf(String dir) throws IOException {

        Map<String, Float> idf = ReadFiles.idf(dir);
        Map<String, HashMap<String, Float>> tf = ReadFiles.tfOfAll(dir);

        for (String file : tf.keySet()) {
            Map<String, Float> singelFile = tf.get(file);
            for (String word : singelFile.keySet()) {
                singelFile.put(word, (idf.get(word)) * singelFile.get(word));
            }
        }
        return tf;
    }
}

package org.example.javabase;

import cn.hutool.core.io.IoUtil;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.util.*;

public class CreateZSJJob {
    public static void main(String[] args) throws IOException {
        createTableSql();
    }

    public static void createTableSql() throws IOException {
        String jobFile = "性能分组测试";
        String pkg = "性能测试" + jobFile;
        Map<Integer, String> engineIdMap = new LinkedHashMap<Integer, String>() {{
            put(1, "批量采集引擎");
            put(2, "批量采集引擎-48");
            put(3, "批量采集引擎-49");
            put(4, "批量采集引擎-50");
            put(5, "批量采集引擎-52");
            put(6, "批量采集引擎155");
            put(7, "批量采集引擎156");
            put(8, "批量采集引擎169");
        }};
        int[] limits = new int[]{100, 200, 300, 400, 500, 600};

        List<String> sourceList = Arrays.asList("mysql", "pg", "oracle");


        Map<Integer, BufferedWriter> bufferedWriterMap = new HashMap<>(engineIdMap.size());
        for (int id : engineIdMap.keySet()) {
            bufferedWriterMap.put(id, new BufferedWriter(new OutputStreamWriter(new BufferedOutputStream(FileUtils.openOutputStream(new File("C:\\Users\\admin\\Desktop\\" + pkg + "\\作业引擎分组\\" + engineIdMap.get(id) + ".txt"), false)))));
        }

        Map<Integer, BufferedWriter> limitWriterMap = new HashMap<>(limits.length);
        for (int limit : limits) {
            limitWriterMap.put(limit, new BufferedWriter(new OutputStreamWriter(new BufferedOutputStream(FileUtils.openOutputStream(new File("C:\\Users\\admin\\Desktop\\" + pkg + "\\并发上限\\" + limit + ".txt"), false)))));
        }

        Map<String, BufferedReader> sourceReaderMap = new HashMap<>(sourceList.size());
        for (String source : sourceList) {
            sourceReaderMap.put(source, new BufferedReader(new InputStreamReader(new BufferedInputStream(FileUtils.openInputStream(new File("C:\\Users\\admin\\Desktop\\源表\\" + source + ".txt"))))));
        }

        int count = 0;
        int bufferedIndex = 0;
        try (FileOutputStream fileOutputStream = new FileOutputStream("C:\\Users\\admin\\Desktop\\" + pkg + "\\所有语句.txt", false);
             BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
             BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(bufferedOutputStream))) {

            List<Integer> idsSet = new ArrayList<>(engineIdMap.keySet());
            String line;
            while ((line = sourceReaderMap.get(sourceList.get(count++ % sourceList.size())).readLine()) != null) {
                Integer engineId = idsSet.get(bufferedIndex);
                bufferedWriter.write("update dc_collect_job set engine_id=" + engineId + " where name = '" + line + "';");
                bufferedWriter.newLine();
                bufferedWriter.flush();

                bufferedWriterMap.get(engineId).write(line);
                bufferedWriterMap.get(engineId).newLine();
                bufferedWriterMap.get(engineId).flush();
                bufferedIndex = (bufferedIndex == bufferedWriterMap.size() - 1) ? 0 : bufferedIndex + 1;
                for (Integer limit : limitWriterMap.keySet()) {
                    if (count <= limit) {
                        limitWriterMap.get(limit).write(line);
                        limitWriterMap.get(limit).newLine();
                        limitWriterMap.get(limit).flush();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        bufferedWriterMap.values().forEach(IoUtil::close);
        limitWriterMap.values().forEach(IoUtil::close);
        sourceReaderMap.values().forEach(IoUtil::close);
    }

//    public static void main(String[] args) throws Exception {
//
//        int[] ids = new int []{1,2,3,4};
//
//        AtomicInteger i = new AtomicInteger();
//        for (String name : IOUtils.readLines(Files.newInputStream(new File("/Users/simon/spaces/data-collection-platform/data-collect-platform/pom.xml").toPath()), "UTF-8")) {
//            System.out.println("update xxx set id='" + ids[i.getAndIncrement() / 5] + "' where name = '" + name + "'");
//        }
//
//
//    }
}

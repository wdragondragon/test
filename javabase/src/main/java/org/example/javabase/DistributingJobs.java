package org.example.javabase;

import cn.hutool.core.io.IoUtil;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class DistributingJobs {
    public static void main(String[] args) throws IOException {
        createTableSql();
    }

    public static class EngineInfo {
        private final Integer id;

        private final String name;
        private final int limit;

        private int count = 0;

        private final List<String> ignoreEngineIds;

        public EngineInfo(Integer id, String name, int limit, List<String> ignoreEngineIds) {
            this.id = id;
            this.name = name;
            this.limit = limit;
            this.ignoreEngineIds = ignoreEngineIds;
        }

        public String getName() {
            return name;
        }

        public int getLimit() {
            return limit;
        }

        public Integer getId() {
            return id;
        }

        public int getCount() {
            return count;
        }

        public void addCount() {
            count++;
        }

        public boolean isIgnore(String source) {
            return ignoreEngineIds.contains(source);
        }

        public boolean isEOF() {
            return count >= limit;
        }
    }

    public static class FileReader {
        private final BufferedReader reader;

        private boolean eof = false;

        public FileReader(BufferedReader reader) {
            this.reader = reader;
        }

        public BufferedReader getReader() {
            return reader;
        }

        public boolean isEof() {
            return eof;
        }

        public void setEof(boolean eof) {
            this.eof = eof;
        }
    }

    public static void createTableSql() throws IOException {
        String jobFile = "test1";
        String pkg = "性能测试" + jobFile;


        Map<Integer, EngineInfo> engineIdMap = new LinkedHashMap<Integer, EngineInfo>() {{
            put(0, new EngineInfo(1, "批量采集引擎", 25, Collections.emptyList()));
            put(1, new EngineInfo(2, "批量采集引擎-48", 25, Collections.emptyList()));
            put(2, new EngineInfo(3, "批量采集引擎-49", 25, Collections.emptyList()));
            put(3, new EngineInfo(4, "批量采集引擎-50", 25, Collections.emptyList()));
            put(4, new EngineInfo(5, "批量采集引擎-52", 25, Collections.emptyList()));
//            put(5, new EngineInfo(6, "批量采集引擎155", 80, Arrays.asList("mft-1", "mft-2", "mft-3")));
//            put(6, new EngineInfo(7, "批量采集引擎156", 80, Arrays.asList("mft-1", "mft-2", "mft-3")));
//            put(7, new EngineInfo(8, "批量采集引擎169", 80, Arrays.asList("mft-1", "mft-2", "mft-3")));

            put(6, new EngineInfo(7, "批量采集引擎156", 41, Collections.singletonList("mft")));
            put(5, new EngineInfo(6, "批量采集引擎155", 42, Collections.singletonList("mft")));
            put(7, new EngineInfo(8, "批量采集引擎169", 42, Collections.singletonList("mft")));
        }};
        int[] limits = new int[]{100, 200, 300, 400, 500, 600};


        Map<Integer, BufferedWriter> bufferedWriterMap = new HashMap<>(engineIdMap.size());
        for (EngineInfo engineInfo : engineIdMap.values()) {
            bufferedWriterMap.put(engineInfo.getId(), new BufferedWriter(new OutputStreamWriter(new BufferedOutputStream(FileUtils.openOutputStream(new File("C:\\Users\\admin\\Desktop\\" + pkg + "\\作业引擎分组\\" + engineInfo.getName() + ".txt"), false)))));
        }

        Map<Integer, BufferedWriter> limitWriterMap = new HashMap<>(limits.length);
        for (int limit : limits) {
            limitWriterMap.put(limit, new BufferedWriter(new OutputStreamWriter(new BufferedOutputStream(FileUtils.openOutputStream(new File("C:\\Users\\admin\\Desktop\\" + pkg + "\\并发上限\\" + limit + ".txt"), false)))));
        }

        List<String> sourceList = Arrays.asList("mysql", "pg", "oracle", "mft");
        Map<String, FileReader> sourceReaderMap = new HashMap<>(sourceList.size());
        for (String source : sourceList) {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new BufferedInputStream(FileUtils.openInputStream(new File("C:\\Users\\admin\\Desktop\\源表\\" + source + ".txt")))));

            sourceReaderMap.put(source, new FileReader(bufferedReader));
        }

        Map<String, Integer> noEngineMap = new HashMap<>();
        Map<String, Integer> sourceCountMap = new HashMap<>();
        int fileCount = 0;
        int fileIndex = 0;
        int fileAll = 0;
        String line = null;
        try (FileOutputStream fileOutputStream = new FileOutputStream("C:\\Users\\admin\\Desktop\\" + pkg + "\\所有语句.txt", false);
             BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
             BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(bufferedOutputStream))) {
            boolean finish;
            while (true) {
                int sourceIndex;
                String source = null;
                FileReader fileReader;

                do {
                    finish = sourceReaderMap.values().stream().allMatch(FileReader::isEof);
                    if (finish) {
                        break;
                    }
                    sourceIndex = fileIndex % sourceList.size();
                    source = sourceList.get(sourceIndex);
                    fileReader = sourceReaderMap.get(source);
                    BufferedReader bufferedReader = fileReader.getReader();
                    line = bufferedReader.readLine();
                    if (line == null) {
                        fileReader.setEof(true);
                    } else {
                        fileAll++;
                    }
                    fileIndex++;
                } while (line == null);

                if (finish) {
                    break;
                }

                EngineInfo engineInfo = randomEngineId(engineIdMap, source);
                if (engineInfo == null) {
                    System.out.println(fileCount + "-" + line + "作业无可容纳的引擎");
                    noEngineMap.put(source, noEngineMap.getOrDefault(source, 0) + 1);
                    continue;
                } else {
                    sourceCountMap.put(source, sourceCountMap.getOrDefault(source, 0) + 1);
                }
                Integer engineId = engineInfo.getId();
                bufferedWriter.write("update dc_collect_job set engine_id=" + engineId + " where name = '" + line + "';");
                bufferedWriter.newLine();
                bufferedWriter.flush();

                bufferedWriterMap.get(engineId).write(line);
                bufferedWriterMap.get(engineId).newLine();
                bufferedWriterMap.get(engineId).flush();

                engineInfo.addCount();
                fileCount++;

                for (Integer limit : limitWriterMap.keySet()) {
                    if (fileCount <= limit) {
                        limitWriterMap.get(limit).write(line);
                        limitWriterMap.get(limit).newLine();
                        limitWriterMap.get(limit).flush();
                    }
                }
            }
            noEngineMap.forEach((k, v) -> System.out.println(k + "源无法分配引擎作业数：" + v));
            System.out.println("总作业数：" + fileAll);
            System.out.println("分配作业数：" + fileCount);
            sourceCountMap.forEach((k, v) -> System.out.println(k + "源已分配作业数：" + v));
        } catch (IOException e) {
            e.printStackTrace();
        }

        bufferedWriterMap.values().forEach(IoUtil::close);
        limitWriterMap.values().forEach(IoUtil::close);
        sourceReaderMap.values().forEach(fileReader -> IoUtil.close(fileReader.getReader()));
    }

    public static EngineInfo randomEngineId(Map<Integer, EngineInfo> engineIdMap, String source) {
        List<EngineInfo> engineInfos = engineIdMap.values().stream().filter(e -> !e.isEOF() && !e.isIgnore(source)).collect(Collectors.toList());
        if (engineInfos.isEmpty()) {
            return null;
        }
        return engineInfos.get(new Random().nextInt(engineInfos.size()));
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

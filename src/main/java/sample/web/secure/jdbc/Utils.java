package sample.web.secure.jdbc;

import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by wuyihao on 4/23/17.
 */
public class Utils {
    private org.apache.hadoop.conf.Configuration config;
    private FileSystem fileSystem;
    public Utils() {
        config = new org.apache.hadoop.conf.Configuration();
        config.addResource(new Path("/usr/lib/hadoop/etc/hadoop/core-site.xml"));
        config.addResource(new Path("/usr/lib/hadoop/etc/hadoop/hdfs-site.xml"));
        config.set("fs.hdfs.impl", org.apache.hadoop.hdfs.DistributedFileSystem.class.getName());
        config.set("fs.file.impl",org.apache.hadoop.fs.LocalFileSystem.class.getName());
        try {
            fileSystem = FileSystem.get(config);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void finalize() {
        try {
            fileSystem.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    FileStatus[] listFiles(String path) {
        try {
            Path p = new Path("hdfs://localhost:9000" + path);
            return fileSystem.listStatus(p);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    void mkdir(String path) {
        try {
            fileSystem.mkdirs(new Path(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getFile(String path, OutputStream os) {
        try {
            Path p = new Path(path);
            if (!fileSystem.exists(p)){
                throw new IOException();
            }

            InputStream is = fileSystem.open(p);
            org.apache.commons.io.IOUtils.copy(is, os);
            is.close();
            return p.getName();
        } catch (IOException ex){
            throw new RuntimeException("IOError writing file to output stream.");
        }
    }

}

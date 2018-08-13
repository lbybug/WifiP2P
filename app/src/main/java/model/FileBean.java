package model;

/**
 * Created by LLB on 2018/8/13.
 */

public class FileBean {

    private final String fileName;
    private final long fileSize;

    public String getFileName() {
        return fileName;
    }

    public long getFileSize() {
        return fileSize;
    }

    private FileBean(Builder builder) {
        fileName = builder.fileName;
        fileSize = builder.fileSize;
    }
    public static final class Builder {
        private final String fileName;
        private final long fileSize;

        public Builder(String fileName, long fileSize) {
            this.fileName = fileName;
            this.fileSize = fileSize;
        }

        public FileBean build() {
            return new FileBean(this);
        }
    }
}

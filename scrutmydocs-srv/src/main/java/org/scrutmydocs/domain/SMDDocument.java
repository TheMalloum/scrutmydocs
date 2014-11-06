package org.scrutmydocs.domain;

import java.util.Arrays;
import java.util.Date;

/**
 * This class describes what a Document means in scrutmydocs project.
 * It comes with a content (extracted text for example), metadata and file like attributes
 */
public class SMDDocument {

    /**
     * Document Unique ID
     */
	public String id;

    /**
     * Document Type
     */
    public String type;

    /**
     * Full text content
     */
    public String content;

    /**
     * Provides Metadata for the document
     */
    public Meta meta;

    /**
     * Provides information about the source document
     */
    public File file;

    public SMDDocument() {
        meta = new Meta();
        file = new File();
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("SMDDocument{");
        sb.append("id='").append(id).append('\'');
        sb.append(", type='").append(type).append('\'');
        sb.append(", content='").append(content).append('\'');
        sb.append(", meta=").append(meta);
        sb.append(", file=").append(file);
        sb.append('}');
        return sb.toString();
    }

    // Inner classes

    public class Meta {
        /**
         * Author if any
         */
        public String author;
        /**
         * Title if any
         */
        public String title;
        /**
         * Date if any
         */
        public Date date;
        /**
         * Keywords if any
         */
        public String[] keywords;

        @Override
        public String toString() {
            final StringBuffer sb = new StringBuffer("Meta{");
            sb.append("author='").append(author).append('\'');
            sb.append(", title='").append(title).append('\'');
            sb.append(", date=").append(date);
            sb.append(", keywords=").append(keywords == null ? "null" : Arrays.asList(keywords).toString());
            sb.append('}');
            return sb.toString();
        }
    }

    public class File {
        /**
         * Content Type
         */
        public String content_type;
        /**
         * Last modification date if known
         */
        public Date last_modified;
        /**
         * Indexing date
         */
        public Date indexing_date;
        /**
         * File size (binary data size) in bytes
         */
        public Long filesize;
        /**
         * Original filename
         */
        public String filename;
        /**
         * Directory
         */
        public String path;
        /**
         * Public URL where this document can be retrieved from
         */
        public String url;
        /**
         * Number of extracted characters if different than default value
         */
        public Integer indexed_chars;

        @Override
        public String toString() {
            final StringBuffer sb = new StringBuffer("File{");
            sb.append("content_type='").append(content_type).append('\'');
            sb.append(", last_modified=").append(last_modified);
            sb.append(", indexing_date=").append(indexing_date);
            sb.append(", filesize=").append(filesize);
            sb.append(", filename='").append(filename).append('\'');
            sb.append(", path='").append(path).append('\'');
            sb.append(", url='").append(url).append('\'');
            sb.append(", indexed_chars=").append(indexed_chars);
            sb.append('}');
            return sb.toString();
        }
    }
}

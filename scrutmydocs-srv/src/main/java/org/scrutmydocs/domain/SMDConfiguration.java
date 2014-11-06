package org.scrutmydocs.domain;

public class SMDConfiguration {
    public static final int INDEXED_CHARS_DEFAULT = 100000;

    private boolean nodeEmbedded = true;
    private String[] nodeAdresses = {"localhost:9300", "localhost:9301"};
    private String clusterName = "scrutmydocs";
    private String pathData = "~/.scrutmydocs/esdata";
    private boolean cleanOnStart = false;
    private int indexedChars;

    public boolean isNodeEmbedded() {
        return nodeEmbedded;
    }
    public void setNodeEmbedded(boolean nodeEmbedded) {
        this.nodeEmbedded = nodeEmbedded;
    }

    public String[] getNodeAdresses() {
        return nodeAdresses;
    }
    public void setNodeAdresses(String[] nodeAdresses) {
        this.nodeAdresses = nodeAdresses;
    }

    public String getClusterName() {
        return clusterName;
    }
    public void setClusterName(String clusterName) {
        this.clusterName = clusterName;
    }

    public String getPathData() {
        return pathData;
    }
    public void setPathData(String pathData) {
        this.pathData = pathData;
    }

    public boolean isCleanOnStart() {
        return cleanOnStart;
    }
    public void setCleanOnStart(boolean cleanOnStart) {
        this.cleanOnStart = cleanOnStart;
    }

    public int getIndexedChars() {
        return indexedChars;
    }
    public void setIndexedChars(int indexedChars) {
        this.indexedChars = indexedChars;
    }
}

package net.bingosoft.oss.imclient.model;

/**
 * 发送者/接收者类别
 * @author kael.
 */
public interface ObjectType {
    /**
     * 系统
     */
    int SYSTEM  = 0;
    /**
     * 用户
     */
    int USER    = 1;
    /**
     * 群组
     */
    int GROUP   = 2;
    /**
     * 应用
     */
    int APP     = 3;
    /**
     * 部门
     */
    int DEPT    = 4;
    /**
     * 服务号
     */
    int SNO     = 5;
}
